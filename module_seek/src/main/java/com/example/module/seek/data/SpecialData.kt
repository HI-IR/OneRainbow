package com.example.module.seek.data

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/18 15:36
 */
data class SpecialData(
    val code: Int,
    val result: ResultSpecial
)

data class ResultSpecial(
    val albumCount: Int,
    val albums: List<AlbumSpecial>,
)
data class AlbumSpecial(
    val artists: List<ArtistX>,
    val id: Long,
    val name: String,
    val blurPicUrl :String,
    val publishTime :Long
)

