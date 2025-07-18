package com.onerainbow.module.musicplayer.model

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.google.gson.Gson


/**
 * description ： 数据类，音乐
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/16 14:25
 */
data class Song(
    val id: Long,
    val name: String,
    val artists: List<Artist>,
    val coverUrl: String
)


data class Artist(
    val name: String, //歌曲名
    val id: Long //作者id
)


fun Song.toMediaMetadata(): MediaMetadata {
    return MediaMetadata.Builder()
        .setTitle(name)  // 歌曲名
        .setArtist(this.artists.joinToString(" / ") { it.name })  // 艺术家（如果有该字段）
        .setArtworkUri(Uri.parse(coverUrl))  // 封面URL（如果有该字段）
        .build()
}

fun MediaItem.toSong(): Song {
    val json = this.localConfiguration?.tag as? String ?: ""
    return Gson().fromJson(json, Song::class.java)
}