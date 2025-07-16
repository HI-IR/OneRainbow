package com.onerainbow.module.musicplayer.model

/**
 * description ： 数据类，音乐
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/16 14:25
 */
data class Song(
    val id: Long,
    val title: String,
    val artists: String,
    val url : String,
    val coverUrl : String
)