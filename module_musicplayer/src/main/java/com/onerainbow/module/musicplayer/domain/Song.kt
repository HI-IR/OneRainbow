package com.onerainbow.module.musicplayer.domain

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.google.gson.Gson
import com.onerainbow.lib.database.ArtistLite
import com.onerainbow.lib.database.Converter
import com.onerainbow.lib.database.entity.RecentPlayedEntity


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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Song) return false
        return id == other.id // 基于id判断唯一性
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}


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

//把Artist数据转化为数据库存储数据
fun Artist.toArtistLite() = ArtistLite(name, id)

//把数据库存储数据转化为Artist数据
fun ArtistLite.toArtist() = Artist(name, id)

//将数据库中存储的RecentPlayedEntity转化为Song数据
fun RecentPlayedEntity.toSong(): Song {
    val artistList = Converter().toArtistLists(artistsJson)
    return Song(songId, songName, artistList.map { it.toArtist() }, coverUrl)
}