package com.onerainbow.module.home.viewmodel

import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onerainbow.lib.base.utils.UsernameUtils
import com.onerainbow.lib.database.OneRainbowDatabase
import com.onerainbow.module.musicplayer.domain.Song
import com.onerainbow.module.musicplayer.service.MusicManager
import com.onerainbow.module.musicplayer.service.PlaybackStateListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * description ： 主页框架的ViewModel
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/19 15:36
 */
class HomeViewModel: ViewModel(){

    //头像数据
    private val _avatarData = MutableLiveData<ByteArray>()
    val avatarData: LiveData<ByteArray>  = _avatarData

    //用户名数据
    private val _usernameData = MutableLiveData<String>()
    val usernameData: LiveData<String>  = _usernameData


    //头像数据报错
    private val _errorAvatar = MutableLiveData<String>()
    val errorAvatar : LiveData<String> = _errorAvatar


    //播放状态-error，停止所有控制，交给播放器控制
    private val _error = MutableLiveData<Boolean>(false)
    val error: LiveData<Boolean> = _error

    //播放状态-标志
    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    //当前播放的页数
    private val _currentIndex : MutableLiveData<Int> = MutableLiveData(-1)
    val currentIndex : LiveData<Int> = _currentIndex

    // 播放列表
    private val _playlist = MutableLiveData<List<Song>>(emptyList())
    val playlist: LiveData<List<Song>> = _playlist

    private val dao by lazy {
        OneRainbowDatabase.getDatabase().userDao()
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

    //初始化同步数据
    init {
        //注册监听器
        MusicManager.addPlaybackStateListener(playbackListener)

        // 尝试从 Manager 拉取初始状态
        _playlist.value = MusicManager.getPlaylist()
        _currentIndex.value = MusicManager.getCurrentIndex()
        _isPlaying.value = MusicManager.isPlaying()
    }


    //切换播放状态
    fun togglePlayPause(){
        if (error.value!!){
            return
        }
        MusicManager.togglePlayPause()
    }

    fun playAt(index: Int){
        MusicManager.playAt(index)
    }


    //移除监听器，避免内存泄露，状态污染
    override fun onCleared() {
        MusicManager.removePlaybackStateListener(playbackListener)//取消监听器
        super.onCleared()
    }

    fun loadUserInfo() {
        //本地没有保存用户名，则没登陆，不加载头像
        val username =UsernameUtils.getUsername()
        if (username.isNullOrBlank()) return
        _usernameData.postValue(username!!)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val base64String = dao.getAvatarResByUsername(username)
                if (base64String.isNullOrBlank()) return@launch

                val pureBase64 = base64String.substringAfter("base64,", base64String)
                val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
                _avatarData.postValue(decodedBytes)
            } catch (e: Exception) {
                _errorAvatar.postValue(e.message?:"头像加载失败")
            }
        }
    }

    /**
     * 登出
     */
    fun logout(){
        //本地没有保存用户名，则没登陆，不能登出
        val username =UsernameUtils.getUsername()
        if (username.isNullOrBlank()) return
        UsernameUtils.clearUsername()
        //传递一个空字符串，代表已经登出
        _usernameData.postValue("")
    }

}