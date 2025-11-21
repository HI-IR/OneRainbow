package com.onerainbow.module.seek.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * description ： 获取歌单列表
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 19:44
 */

data class PlaylistData(
    @SerializedName("code") val code: Int,
    @SerializedName("result") val result: ResultPlaylist
)

data class ResultPlaylist(
    val playlists: List<Playlists>,
)

@Parcelize
data class Playlists(
    @SerializedName("coverImgUrl") val coverImgUrl: String,
    @SerializedName("creator") val creator: Creator,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("userId") val userId: Long,
    @SerializedName("trackCount") val trackCount: Int
) : Parcelable

@Parcelize
data class Creator(
    @SerializedName("avatarUrl") val avatarUrl: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("userId") val userId: Long,
) : Parcelable
