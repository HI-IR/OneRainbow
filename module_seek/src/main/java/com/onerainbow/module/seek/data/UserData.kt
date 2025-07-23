package com.onerainbow.module.seek.data

/**
 * description ： 获取歌手列表
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 21:00
 */
data class UserData(
    val code: Int,
    val result: UserResult
)

data class UserResult(
    val artists: List<ArtistUser>,
)

data class ArtistUser(
    val id: Long,
    val mvSize: Int,
    val name: String,
    val picUrl: String,
)