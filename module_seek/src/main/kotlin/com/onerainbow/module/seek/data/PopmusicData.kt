package com.onerainbow.module.seek.data

import com.google.gson.annotations.SerializedName

/**
 * description ： 获取排行榜信息
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/15 21:03
 */
data class PopmusicData(
    @SerializedName("code") val code: Int,
    @SerializedName("playlist") val playlist: Playlist,
)
data class Playlist(
    @SerializedName("tracks") val tracks: List<Tracks>,
    @SerializedName("name") val name:String
)
data class Tracks(
    @SerializedName("name") val name:String,
    @SerializedName("id") val id:String
)