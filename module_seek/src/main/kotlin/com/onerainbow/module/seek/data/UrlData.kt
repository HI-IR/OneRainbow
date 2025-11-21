package com.onerainbow.module.seek.data

import com.google.gson.annotations.SerializedName

/**
 * description ： 获取歌曲对应图片
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/19 17:02
 */
data class UrlData(
    @SerializedName("code") val code: Int,
    @SerializedName("songs") val songs: List<SongUrl>
)
data class SongUrl(
    @SerializedName("al") val al: Urls,
)

data class Urls(
    @SerializedName("picUrl") val picUrl: String,
)
