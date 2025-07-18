package com.example.module.seek.data

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/18 16:41
 */
data class GetMvData(
    val code: Int,
    val result: ResultGetMv
)

data class ResultGetMv(
    val mvs: List<Mv>
)

data class Mv(
    val artists: List<ArtistX>,
    val cover: String,
    val duration: Long,
    val id: Int,
    val name: String,
    val playCount:Int
)
