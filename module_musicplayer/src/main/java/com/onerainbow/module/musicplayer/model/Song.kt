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
    val url: String,
    val coverUrl: String
)


data class Artist(
    val name: String, //歌曲名
    val id: Long //作者id
)


fun Song.toMediaItem(): MediaItem {
    //构建媒体元数据
    val metadata = MediaMetadata.Builder()
        .setTitle(this.name)
        .setArtist(this.artists.joinToString(" / ") { it.name })
        .setArtworkUri(Uri.parse(this.coverUrl))
        .build()

    val json = Gson().toJson(this)
    return MediaItem.Builder()
        .setUri(this.url)
        .setMediaId(this.id.toString())
        .setTag(json)
        .setMediaMetadata(metadata)
        .build()
}

fun MediaItem.toSong(): Song {
    val json = this.localConfiguration?.tag as? String ?: ""
    return Gson().fromJson(json, Song::class.java)
}