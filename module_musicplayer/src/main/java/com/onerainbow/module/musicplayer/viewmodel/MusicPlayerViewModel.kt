package com.onerainbow.module.musicplayer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.onerainbow.lib.base.utils.StoreUtils
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.lib.base.utils.UsernameUtils
import com.onerainbow.lib.database.OneRainbowDatabase
import com.onerainbow.lib.database.entity.CollectEntity
import com.onerainbow.module.musicplayer.domain.Song
import com.onerainbow.module.musicplayer.domain.toArtistLite
import com.onerainbow.module.musicplayer.service.MusicManager
import com.onerainbow.module.musicplayer.service.PlaybackStateListener
import kotlinx.coroutines.launch

/**
 * description ： 音乐播放器界面的ViewModel
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/16 17:37
 */

class MusicPlayerViewModel() : ViewModel() {
    private val dao by lazy {
        OneRainbowDatabase.getDatabase().collectDao()
    }

    //用户名
    private var username: String? = null

    //有关收藏的错误提示
    private val _errorCollect = MutableLiveData<String>()
    val errorCollect: LiveData<String> = _errorCollect

    //收藏状态
    private val _isCollect = MutableLiveData<Boolean>()
    val isCollect: LiveData<Boolean> = _isCollect

    //播放状态-errorMessage
    private val _error = MutableLiveData<Boolean>(false)
    val error: LiveData<Boolean> = _error

    //播放状态-标志
    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    //播放模式-标志
    //true 单曲循环
    //false 列表播放
    private val _isPlayInSingle = MutableLiveData<Boolean>(false) //默认顺序播放
    val isPlayInSingle: LiveData<Boolean> = _isPlayInSingle

    private val _currentIndex: MutableLiveData<Int> = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    // 播放列表
    private val _playlist = MutableLiveData<List<Song>>(emptyList())
    val playlist: LiveData<List<Song>> = _playlist

    private val gson by lazy {
        Gson()
    }

    private val playbackListener = object : PlaybackStateListener {
        override fun onPlayStateChanged(isPlaying: Boolean) {
            _isPlaying.postValue(isPlaying)
        }

        override fun onPlayIndexChanged(index: Int) {
            _currentIndex.postValue(index)
        }

        override fun onPlayError(error: Boolean) {
            _error.postValue(error)
        }

        override fun onPlayerListChanged(playerList: List<Song>) {
            _playlist.postValue(playerList)
        }
    }

    //初始化数据
    init {
        MusicManager.addPlaybackStateListener(playbackListener)

        // 尝试从 Manager 拉取初始状态
        _playlist.value = MusicManager.getPlaylist()
        _currentIndex.value = MusicManager.getCurrentIndex()
        _isPlaying.value = MusicManager.isPlaying()
        _isPlayInSingle.postValue(
            StoreUtils.getBoolean(
                StoreUtils.PLAYER_DATA,
                StoreUtils.KEY_PLAYER_MODE
            )
        )

        username = UsernameUtils.getUsername()
    }

    fun getCurrentUrl(): String = MusicManager.getCurrentUrl()


    // 播放下一首
    fun playNext() {
        if (!MusicManager.playNext()) {
            ToastUtils.makeText("已经是最后一首了")
        }
    }

    // 播放上一首
    fun playPrev() {
        if (!MusicManager.playPrev()) {
            ToastUtils.makeText("已经是第一首了")
        }

    }

    //切换播放状态
    fun togglePlayPause() {
        if (error.value!!) {
            return
        }
        MusicManager.togglePlayPause()
    }


    //在onClear中调用，用于保存播放器播放状态
    fun savePlayMode() {
        Log.d("ld", "退出 ${isPlayInSingle.value}");
        StoreUtils.saveBoolean(
            StoreUtils.PLAYER_DATA,
            StoreUtils.KEY_PLAYER_MODE,
            isPlayInSingle.value ?: false
        )
    }


    fun togglePlayMode() {
        val current = _isPlayInSingle.value ?: false
        if (current) MusicManager.setOrderMode() else MusicManager.setSingleMode()
        _isPlayInSingle.value = !current
    }

    fun playAt(index: Int) {
        MusicManager.playAt(index)
    }

    //跳转位置
    fun playSeekTo(position: Long) {
        MusicManager.seekTo(position)
    }

    /**
     * 收藏
     */
    fun collectSong() {
        val currentSong = MusicManager.getCurrentSong()
        if (currentSong == null || username == null) {
            if (currentSong == null) _errorCollect.postValue("当前无歌曲")
            if (username == null) _errorCollect.postValue("还没有登录哟")
            return
        }
        viewModelScope.launch {
            try {
                dao.collectSong(
                    CollectEntity(
                        username!!,
                        currentSong.id,
                        currentSong.name,
                        currentSong.coverUrl,
                        gson.toJson(currentSong.artists.map { artist ->
                            artist.toArtistLite()
                        })
                    )
                )
                _isCollect.postValue(true)
            }catch (e:Exception){
                _errorCollect.postValue("发出错误:${e.message}")
            }
        }
    }

    /**
     * 取消收藏
     */
    fun uncollectSong(){
        val currentSong = MusicManager.getCurrentSong()
        if (currentSong == null || username == null) {
            if (currentSong == null) _errorCollect.postValue("当前无歌曲")
            if (username == null) _errorCollect.postValue("还没有登录哟")
            return
        }
        viewModelScope.launch {
            try {
                dao.uncollectSong(username!!,currentSong.id)
                _isCollect.postValue(false)
            }catch (e:Exception){
                _errorCollect.postValue("发出错误:${e.message}")
            }
        }
    }

    /**
     * 切换是否收藏
     */
    fun toggleCollection(){
        when(isCollect.value){
            true -> uncollectSong()
            false -> collectSong()
            else -> _errorCollect.postValue("你还没有登录哟")
        }
    }


    /**
     * 检查是否收藏
     */
    fun checkCollect(){
        val currentSong = MusicManager.getCurrentSong()
        if (currentSong == null || username == null) {
            return
        }
        viewModelScope.launch {
            try {
                val result = dao.hasCollectSongBySongId(username!!,currentSong.id) > 0L
                _isCollect.postValue(result)
            }catch (e:Exception){
                _errorCollect.postValue("发出错误:${e.message}")
            }
        }
    }


    override fun onCleared() {
        MusicManager.removePlaybackStateListener(playbackListener)//移除设置的监听器，防止内存泄露
        super.onCleared()
    }

}