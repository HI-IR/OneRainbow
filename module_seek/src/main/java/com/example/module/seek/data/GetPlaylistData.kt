package com.example.module.seek.data

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/18 20:47
 */
data class GetPlaylistData(
    val code: Int,
    val songs: List<SongGetPlay>
)

data class SongGetPlay(
    val al: Al,
    val ar: List<Artist>,
    val id: Long,
    val mainTitle: String,
)

data class Al(
    val id: Long,
    val name: String,
    val picUrl: String,
)
