package com.onerainbow.module.seek.data

import android.provider.MediaStore.Audio.Playlists

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/15 21:03
 */
data class PopmusicData(
    val code: Int,
    val playlist: Playlist,
)
data class Playlist(
    val tracks: List<Tracks>,
    val name:String
)
data class Tracks(
    val name:String,
    val id:String
)