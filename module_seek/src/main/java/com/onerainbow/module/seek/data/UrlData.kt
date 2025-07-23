package com.onerainbow.module.seek.data

/**
 * description ： 获取歌曲对应图片
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/19 17:02
 */
data class UrlData(
    val code: Int,
    val songs: List<SongUrl>
)
data class SongUrl(
    val al: Urls,

)

data class Urls(
    val picUrl: String,
)
