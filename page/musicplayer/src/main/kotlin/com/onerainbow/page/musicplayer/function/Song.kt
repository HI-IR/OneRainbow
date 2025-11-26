package com.onerainbow.page.musicplayer.function

import android.net.Uri
import android.os.Parcelable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.google.gson.Gson
import com.onerainbow.lib.database.ArtistLite
import com.onerainbow.lib.database.Converter
import com.onerainbow.lib.database.entity.RecentPlayedEntity
import com.onerainbow.page.musicplayer.api.bean.Artist
import com.onerainbow.page.musicplayer.api.bean.Song
import kotlinx.parcelize.Parcelize


/**
 * description ： 数据类，音乐 的一些处理方法
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/16 14:25
 */
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