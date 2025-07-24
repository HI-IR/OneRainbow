package com.onerainbow.module.top.bean

import com.google.gson.annotations.SerializedName

/**
 * description ： 歌手榜的数据类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 19:12
 */
data class ArtistData(
    @SerializedName("code") val code: Long,
    @SerializedName("list") val list: ResponseData
)

data class ResponseData(
    @SerializedName("artists") val artists: List<Artist>,
)

data class Artist(
    @SerializedName("id") val id: Long,//歌手Id
    @SerializedName("lastRank") val lastRank: Long,//上一次的排名
    @SerializedName("musicSize") val musicSize: Long,//音乐数
    @SerializedName("name") val name: String,//名字
    @SerializedName("picUrl") val picUrl: String,//图片URL
    @SerializedName("score") val score: Long,//分数
)
