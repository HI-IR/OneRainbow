package com.onerainbow.module.top.bean

/**
 * description ： 歌手榜的数据类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 19:12
 */
data class ArtistData(
    val code: Long,
    val list: ResponseData
)

data class ResponseData(
    val artists: List<Artist>,
)

data class Artist(

    val id: Long,//歌手Id
    val lastRank: Long,//上一次的排名
    val musicSize: Long,//音乐数
    val name: String,//名字
    val picUrl: String,//图片URL
    val score: Long,//分数
)
