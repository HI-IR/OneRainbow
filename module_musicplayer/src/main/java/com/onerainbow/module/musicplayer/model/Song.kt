package com.onerainbow.module.musicplayer.model

/**
 * description ： 数据类，音乐
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/16 14:25
 */
data class Song(
    val id: Long,
    val name: String,
    val artists: List<Artist>,
    val url : String,
    val coverUrl : String
)

/**
 * 外界传入歌曲的数据列
 */
data class SongDTO(
    val id: Long,//id
    val name: String, //歌名
    val artists: List<Artist>//作者
)
data class Artist(
    val name: String, //歌曲名
    val id: Long //作者id
)