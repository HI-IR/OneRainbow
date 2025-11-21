package com.onerainbow.module.seek.data

import com.google.gson.annotations.SerializedName

/**
 * description ： 获取视频相关信息
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/18 16:41
 */
data class GetMvData(
    @SerializedName("code") val code: Int,
    @SerializedName("result") val result: ResultGetMv
)

data class ResultGetMv(
    @SerializedName("mvs") val mvs: List<Mv>
)

data class Mv(
    @SerializedName("artists") val artists: List<ArtistX>,
    @SerializedName("cover") val cover: String,
    @SerializedName("duration") val duration: Long,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("playCount") val playCount:Int
)
