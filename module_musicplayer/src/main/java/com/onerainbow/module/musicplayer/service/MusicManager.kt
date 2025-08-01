package com.onerainbow.module.musicplayer.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.onerainbow.module.musicplayer.domain.Song
import com.onerainbow.module.musicplayer.helper.RecentPlayHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * description ： 音乐管理类，用于往服务中添加音乐
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/18 12:04
 */
object MusicManager {
    private var musicBinder: NewMusicService.MusicBinder? = null
    private var isServiceConnected = false
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    //多监听器
    private val listenerList = mutableListOf<PlaybackStateListener>()

    //设置播放状态监听器
    fun addPlaybackStateListener(l: PlaybackStateListener) {
        if (!listenerList.contains(l)) {
            listenerList.add(l)
        }
    }

    //移除播放状态监听器
    fun removePlaybackStateListener(l: PlaybackStateListener) {
        listenerList.remove(l)
    }

    //清空所有监听器
    fun clearListeners() {
        listenerList.clear()
    }


    // 通知方法修改为遍历所有监听器
    fun notifyPlayState(isPlaying: Boolean) {
        listenerList.forEach { it.onPlayStateChanged(isPlaying) }
    }

    fun notifyPlayIndex(index: Int) {
        listenerList.forEach { it.onPlayIndexChanged(index) }
    }

    fun notifyPlayError(error: Boolean) {
        listenerList.forEach { it.onPlayError(error) }
    }

    fun notifyPlayerList(list: List<Song>){
        listenerList.forEach { it.onPlayerListChanged(list) }
    }

    // 服务连接回调
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            musicBinder = service as NewMusicService.MusicBinder
            isServiceConnected = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            isServiceConnected = false
            musicBinder = null
        }
    }


    // 初始化：绑定服务
    fun bindService(context: Context) {
        if (!isServiceConnected) {
            val intent = Intent(context, NewMusicService::class.java)
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun unbindService(context: Context) {
        if (isServiceConnected) {
            context.unbindService(serviceConnection)
            isServiceConnected = false
            musicBinder = null
        }
    }
    fun cleanList(){
        MusicManager.musicBinder?.clearPlaylist()
    }

    fun removeSongAt(position: Int){
        MusicManager.musicBinder?.removeSongAt(position)
    }


    //播放列表相关
    //使用这个
    fun addToPlayerList(vararg song: Song):Boolean{

        //添加到最近播放
        coroutineScope.launch {
            RecentPlayHelper.addPlaylistToRecent(song.toList())
        }
        return if (isServiceConnected){
            musicBinder?.addToPlayerList(song.toList())
            true
        }else false
    }

    //使用这个
    fun addToPlayerList(songs: List<Song>):Boolean{
        //添加到最近播放
        coroutineScope.launch {
            RecentPlayHelper.addPlaylistToRecent(songs)
        }

        return if (isServiceConnected){
            musicBinder?.addToPlayerList(songs)
            true
        }else false
    }


    /**
     * 添加歌单到播放列表
     * 废弃
     */
    fun addSongs(songs: List<Song>): Boolean {
        return if (isServiceConnected) {
            musicBinder?.addSongs(songs)
            true
        } else {
            false
        }
    }


    /**
     * 添加歌曲到播放列表
     * 废弃
     */
    fun addSong(song: Song):Boolean{
        return if (isServiceConnected){
            musicBinder?.addSong(song)
            true
        }else{
            false
        }
    }


    fun getCurrentUrl():String = musicBinder?.getCurrentUrl()?:""


    //播放相关控制
    /** 播放单首歌曲  废弃*/
    fun play(song: Song): Boolean =
        if (isServiceConnected) {
            musicBinder?.play(song)
            true
        } else false


    /** 播放整个歌单，从 startIndex 开始  */
    fun play(songs: List<Song>, startIndex: Int = 0): Boolean =
        if (isServiceConnected) {
            musicBinder?.addSongs(songs, startIndex)
            true
        } else false

    /**
     * 播放下一首歌
     */
    fun playNext():Boolean{
        return if (isServiceConnected){
            musicBinder?.playNext()?:false
        }else false
    }

    fun playPrev():Boolean {
        return if (isServiceConnected){
            musicBinder?.playPrev()?:false
        }else false
    }

    // 暂停播放
    fun pause(): Boolean =
        if (isServiceConnected) {
            musicBinder?.pause()
            true
        } else false

    /** 继续播放 */
    fun resume(): Boolean =
        if (isServiceConnected) {
            musicBinder?.resume()
            true
        } else false


    /** 切换播放/暂停
     */
    fun togglePlayPause(): Boolean {
        return if (isServiceConnected) {
            musicBinder?.togglePlayPause()
            true
        } else false
    }

    fun setSingleMode():Boolean{
        return if (isServiceConnected){
            musicBinder?.setPlayMode(NewMusicService.PlayMode.SINGLE_LOOP)
            true
        }else false
    }

    fun setOrderMode():Boolean{
        return if (isServiceConnected){
            musicBinder?.setPlayMode(NewMusicService.PlayMode.SEQUENTIAL)

            true
        }else false
    }

    fun playAt(index: Int):Boolean{
        return if (isServiceConnected){
            musicBinder?.playAt(index)
            true
        }else false
    }

    //播放信息相关
    /** 获取当前歌单列表 */
    fun getPlaylist(): List<Song> =
        musicBinder?.getSongPlaylist() ?: emptyList()

    //是否正在播放
    fun isPlaying(): Boolean =
        musicBinder?.isPlaying() == true

    //获取当前播放索引
    fun getCurrentIndex(): Int =
        musicBinder?.getCurrentIndex() ?: -1

    //获取当前播放歌曲
    fun getCurrentSong():Song? = musicBinder?.getCurrentSong()

    //获取当前播放时间
    fun getCurrentPosition():Long = musicBinder?.getCurrentPosition()?:1L

    fun getDuration():Long = musicBinder?.getDuration()?:1L

    //跳转播放位置
    fun seekTo(position: Long){
        if (isServiceConnected){
            musicBinder?.seekTo(position)

        }
    }


}
//用来监听变化的接口
interface PlaybackStateListener {
    fun onPlayStateChanged(isPlaying: Boolean)
    fun onPlayIndexChanged(index: Int)
    fun onPlayError(error: Boolean)//出现错误时回调
    fun onPlayerListChanged(playerList:List<Song>)//歌曲变化后的回调
}