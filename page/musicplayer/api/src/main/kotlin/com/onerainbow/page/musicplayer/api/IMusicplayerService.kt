package com.onerainbow.page.musicplayer.api

import android.content.Context
import com.onerainbow.page.musicplayer.api.bean.Song

/**
 * description ： 给外界提供控制音乐播放器的接口
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/11/26 18:10
 */

interface IMusicplayerService {


	/**
	 * 关于添加，删除歌曲
	 */
	fun addToPlayerList(vararg song: Song):Boolean
	fun addToPlayerList(songs: List<Song>):Boolean
	fun removeSongAt(position: Int)


	/**
	 * 关于修改播放状态
	 */
	fun getPlaylist(): List<Song>
	fun getCurrentIndex(): Int
	fun isPlaying(): Boolean
	fun togglePlayPause(): Boolean
	fun playAt(index: Int):Boolean

	/**
	 * 关于添加、删除监听
	 */
	fun addPlaybackStateListener(l: PlaybackStateListener)
	fun removePlaybackStateListener(l: PlaybackStateListener)


	/**
	 * 绑定音乐服务
	 */
	fun bindService(context: Context)
}