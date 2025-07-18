package com.onerainbow.module.musicplayer.viewmodel

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onerainbow.lib.base.utils.StoreUtils
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.module.musicplayer.model.Artist
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

    //播放状态-标志
     val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    //播放模式-标志
    //true 单曲循环
    //false 列表播放
    private val _isPlayInSingle = MutableLiveData<Boolean>(false) //默认顺序播放
    val isPlayInSingle : LiveData<Boolean> = _isPlayInSingle

    //TODO 这里先写死数据
    private val _playLists = MutableLiveData<MutableList<Song>>(
        mutableListOf(
            Song(2722532807,"二十岁", listOf(Artist("宝石Gem",12084497)),"http://m801.music.126.net/20250717165725/ad200de0a41643b58019410cbc87c9ea/jdymusic/obj/wo3DlMOGwrbDjj7DisKw/60985940154/eeb6/d2d3/9617/3585ce1c8d722fb0d9e100846ed773de.mp3?vuutv=8GcvMnNCU4tV+ogUMwNbptWUg/eoakQpwfVnTF5kyVijqF647h80yr7mHGvdaGkjwJkmW0E8Wmat3gu2n9cVN1r8CT5q+L5Uh4IqndzPkS0=","https://p2.music.126.net/v-h49Jow6qgEPCZkNXlY5A==/109951171462194133.jpg"),
            Song(2721721636,"洗牌",listOf(Artist("宝石Gem",12084497)),"http://m701.music.126.net/20250717165747/ea1c53f1f42ab10179c7ac2be5ec2dba/jdymusic/obj/wo3DlMOGwrbDjj7DisKw/60942708912/e1bf/680b/2096/3d689999a19cc6654c3971d45548bc04.mp3?vuutv=mhfaZBrJJf7opiR8kjrE4+Ph7/i1DdsZ3u4MfwNrrI6dhGO9zNpMvgQ1Ho3F/9W9l6oh+R+93KTt6VrpTjE2Pw+IJT/NPoyDAzHCQKFW3e8=","https://p2.music.126.net/uermWb8sH_HYEwVScAAW8Q==/109951171392740991.jpg"),
            Song(2724462272,"u sure u do?",listOf(Artist("张天枢",33371675)),"http://m701.music.126.net/20250717165807/0d8340db32c5a47c3e5df93a0ea2a12d/jdymusic/obj/wo3DlMOGwrbDjj7DisKw/61130105962/33be/daa6/b92f/82c721059b3a19f5a59328dcdf6641d6.mp3?vuutv=myO5ZxnKw/PkIMTSDry8buy7W9ClYpGZ2fuXaSo5eV/YmHM4l951cn+C6ZkanjgU0DlhJINANHFkYTJ88B9Iy2dzXGiDxENe8OfblxAj+B4=","https://p2.music.126.net/whaVtSBYZlo-CqPRqU4Sig==/109951171439212278.jpg")
        )
        )
    val playerLists : LiveData<MutableList<Song>> = _playLists


    val _playIndex : MutableLiveData<Int> = MutableLiveData(0)
    val playIndex : LiveData<Int> = _playIndex



    fun toggleNext() {
        val currentIndex = playIndex.value ?: 0
        val listSize = playerLists.value?.size ?: 0
        if (currentIndex >= listSize - 1) { // 严格检查边界
            ToastUtils.makeText("已经是最后一首了")
            return
        }
        _playIndex.postValue(currentIndex + 1)
    }

    fun togglePrev() {
        val currentIndex = playIndex.value ?: 0
        if (currentIndex <= 0) {
            ToastUtils.makeText("已经是第一首了")
            return
        }
        _playIndex.postValue(currentIndex - 1)
    }



    //在onClear中调用，用于保存播放器播放状态
    fun savePlayMode(){
        Log.d("ld","退出 ${isPlayInSingle.value}");
        StoreUtils.saveBoolean(StoreUtils.PLAYER_DATA,StoreUtils.KEY_PLAYER_MODE,isPlayInSingle.value?:false)
    }

    //初始化播放状态
    fun initPlayMode(){
        val mode = StoreUtils.getBoolean(StoreUtils.PLAYER_DATA,StoreUtils.KEY_PLAYER_MODE)
        Log.d("ld","初始化${mode}");
        _isPlayInSingle.postValue(mode)
    }

    fun togglePlayMode(){
        val current = _isPlayInSingle.value ?: false
        _isPlayInSingle.value = !current
    }


// ===========================关于连接音乐Service=======================================
    //连接音乐播放服务的配置
    private val connection = object : ServiceConnection {
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

    //解除音乐播放服务的绑定
    fun unbindService() {
        if (isBound) {
            getApplication<Application>().unbindService(connection)
            isBound = false
        }
    }


    fun togglePlayOrPause(){
        musicBinder?.togglePlayPause()
        _isPlaying.postValue(musicBinder?.isPlaying())
    }

}