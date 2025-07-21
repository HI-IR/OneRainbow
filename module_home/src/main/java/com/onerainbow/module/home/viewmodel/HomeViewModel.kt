package com.onerainbow.module.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.service.MusicManager
import com.onerainbow.module.musicplayer.service.PlaybackStateListener

/**
 * description ： 主页框架的ViewModel
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/19 15:36
 */
class HomeViewModel: ViewModel(){

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
}