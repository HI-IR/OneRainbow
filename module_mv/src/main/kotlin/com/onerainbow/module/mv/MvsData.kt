package com.onerainbow.module.mv

import com.google.gson.annotations.SerializedName

/**
 * description ： MV的数据类
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/21 14:20
 */
data class MvsData(
    @SerializedName("code") val code: Int,
    @SerializedName("data") val `data`: List<MvData>
)

data class MvData(
    @SerializedName("artists") val artists: List<Artist>,
    @SerializedName("cover") val cover: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("playCount") val playCount: Int,
)

data class Artist(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)