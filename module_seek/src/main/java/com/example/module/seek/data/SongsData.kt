package com.example.module.seek.data

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/19 15:51
 */
data class SongsData(
    val code: Int,
    val more: Boolean,
    val songs: List<SongData>
)

data class SongData(
    val al: AlData,
    val ar: List<Artist>,
    val id: Long,
    val name: String,
)

data class AlData(
    val id: Int,
    val name: String,
    val picUrl: String,
)

