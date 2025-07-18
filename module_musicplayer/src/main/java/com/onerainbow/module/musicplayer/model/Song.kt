package com.onerainbow.module.musicplayer.model

import android.os.Parcel
import android.os.Parcelable
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
    val url : String,
    val coverUrl : String
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()?:"",
        parcel.createTypedArrayList(Artist.CREATOR)?: emptyList(),
        parcel.readString()?:"",
        parcel.readString()?:""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeTypedList(artists)
        parcel.writeString(url)
        parcel.writeString(coverUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }

}





data class Artist(
    val name: String, //歌曲名
    val id: Long //作者id
):Parcelable{
    constructor(parcel: Parcel) : this(
        name = parcel.readString()?:"",
        id = parcel.readLong()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        //写入数据
        dest.apply {
            writeString(name)
            writeLong(id)
        }
    }

    companion object CREATOR : Parcelable.Creator<Artist> {
        override fun createFromParcel(parcel: Parcel): Artist {
            return Artist(parcel)
        }

        override fun newArray(size: Int): Array<Artist?> {
            return arrayOfNulls(size)
        }
    }
}





fun Song.toMediaItem(): MediaItem {
    //构建媒体元数据
    val metadata = MediaMetadata.Builder()
        .setTitle(this.name)
        .setArtist(this.artists.joinToString(" / ") { it.name })
        .setArtworkUri(android.net.Uri.parse(this.coverUrl))
        .build()

    val json = Gson().toJson(this)
    MediaItem.Builder()
        .setUri(this.url)
        .setMediaId(this.id.toString())
        .setTag(json)
        .setMediaMetadata(metadata)
        .build()

    //构建媒体项，设置媒体元数据
    return MediaItem.Builder()
        .setUri(this.url)
        .setMediaId(this.id.toString())
        .setMediaMetadata(metadata)
        .build()
}

fun MediaItem.toSong(): Song{
    val json = this.localConfiguration?.tag as? String ?: ""
    return Gson().fromJson(json, Song::class.java)
}