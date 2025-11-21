package com.onerainbow.module.recommend.bean

import com.google.gson.annotations.SerializedName

/**
 * description ： 甄选歌单的数据类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 19:40
 */
data class CuratedData(
    @SerializedName("result") val result: List<Curated>
)

data class Curated(
    @SerializedName("copywriter") val copywriter: String,//歌单文案
    @SerializedName("id") val id: Long,//歌单ID
    @SerializedName("name") val name: String,//歌单名
    @SerializedName("picUrl") val picUrl: String,//图片URL
    @SerializedName("playCount") val playCount: Long,//播放量
    @SerializedName("trackCount") val trackCount: Long,//歌单中歌曲的数量
    @SerializedName("trackNumberUpdateTime") val trackNumberUpdateTime: Long,//歌单最后更新歌曲数量的时间
    @SerializedName("type") val type: Long//类型
)