package com.onerainbow.page.musicplayer.api

import com.onerainbow.page.musicplayer.api.bean.Song

/**
 * description ： MusicPlayer的状态监听
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/11/26 19:20
 */
//用来监听变化的接口
interface PlaybackStateListener {
	fun onPlayStateChanged(isPlaying: Boolean)
	fun onPlayIndexChanged(index: Int)
	fun onPlayError(error: Boolean)//出现错误时回调
	fun onPlayerListChanged(playerList:List<Song>)//歌曲变化后的回调
}