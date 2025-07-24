package com.onerainbow.module.musicplayer.bean

import com.google.gson.annotations.SerializedName

/**
 * description ： 音乐URL数据类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/18 20:29
 */
data class SongURL(
    @SerializedName("code") val code: Int,
    @SerializedName("data") val `data`: List<Data>
)

data class Data(
    @SerializedName("id") val id: Long,//id
    @SerializedName("url") val url: String,//url
)
