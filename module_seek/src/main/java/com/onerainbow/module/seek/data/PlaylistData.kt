package com.onerainbow.module.seek.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 19:44
 */

data class PlaylistData(
    val code: Int,
    val result: ResultPlaylist
)

data class ResultPlaylist(
    val playlists: List<Playlists>,
)
@Parcelize
data class Playlists(
    val coverImgUrl: String,
    val creator: Creator,
    val description: String,
    val id: Long,
    val name: String,
    val userId: Long,
    val trackCount :Int
): Parcelable
@Parcelize
data class Creator(
    val avatarUrl: String,
    val nickname: String,
    val userId: Long,
):Parcelable
