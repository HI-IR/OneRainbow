package com.onerainbow.module.seek.data

import com.google.gson.annotations.SerializedName

/**
 * description ： 获取歌手信息
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/19 14:37
 */
data class SongersData(
    @SerializedName("artist") val artist: ArtistX,
    @SerializedName("code") val code: Int,
)

data class ArtistX(
    @SerializedName("briefDesc") val briefDesc: String,
    @SerializedName("id") val id: Int,
    @SerializedName("img1v1Url") val img1v1Url: String,
    @SerializedName("name") val name: String,
)