package com.onerainbow.module.seek.data

/**
 * description ： 获取歌手信息
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/19 14:37
 */
data class SongersData(
    val artist: ArtistX,
    val code: Int,
)

data class ArtistX(
    val briefDesc: String,
    val id: Int,
    val img1v1Url: String,
    val name: String,
)