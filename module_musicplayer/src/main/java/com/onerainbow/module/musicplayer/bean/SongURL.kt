package com.onerainbow.module.musicplayer.bean

/**
 * description ： TODO:类的作用
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/18 20:29
 */
data class SongURL(
    val code: Int,
    val `data`: List<Data>
)

data class Data(
    val id: Long,//id
    val url: String,//url
)
