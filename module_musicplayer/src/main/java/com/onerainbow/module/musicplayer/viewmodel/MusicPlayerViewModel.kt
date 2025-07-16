package com.onerainbow.module.musicplayer.viewmodel

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.service.MusicService

/**
 * description ： 音乐播放器界面的ViewModel
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/16 17:37
 */
//因为音乐播放是应用级的行为，所以这里应该传入一个应用级的上下文，防止长生命周期的对象持有短生命周期的引用，造成内存泄露
class MusicPlayerViewModel(application: Application) : AndroidViewModel(application) {
    private var musicBinder: MusicService.MusicBinder? = null
    private var isBound = false //是否绑定了服务

    //播放状态
    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    //连接音乐播放服务的配置
    val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            musicBinder = service as MusicService.MusicBinder
            isBound = true
            _isPlaying.postValue(musicBinder?.isPlaying())

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            musicBinder = null //置空binder
            _isPlaying.postValue(false)
        }

    }

    //连接音乐播放服务
    fun bindService() {
        val intent = Intent(getApplication(), MusicService::class.java)
        //BIND_AUTO_CREATE 自动创建 Service（如果未运行），并在无客户端绑定时自动销毁
        getApplication<Application>().bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    //接触音乐播放服务的绑定
    fun unbindService() {
        if (isBound) {
            getApplication<Application>().unbindService(connection)
            isBound = false
        }
    }

    //TODO 先暂时这样写着，之后修改
    fun play(song: Song){
        musicBinder?.play(song)
        _isPlaying.postValue(true)
    }

    fun resume(){
        musicBinder?.resume()
        _isPlaying.postValue(true)
    }

    fun pause(){
        musicBinder?.pause()//暂停播放
        _isPlaying.postValue(false)
    }
}