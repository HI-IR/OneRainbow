package com.onerainbow.page.musicplayer.api

import android.content.Context
import com.onerainbow.page.musicplayer.api.bean.Song

/**
 * description ：用于打开MusicPlayer相关的一些UI(如Dialog等)
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/11/26 19:34
 */
interface IMusicUIService {
	fun openPlayerListDialog(context: Context,onSongSelected: (Int) -> Unit)
	fun setSelectedPosition(position: Int)
	fun setSongs(list: List<Song>)
}