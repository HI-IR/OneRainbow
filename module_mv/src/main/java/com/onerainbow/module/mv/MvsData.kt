package com.onerainbow.module.mv

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/21 14:20
 */
data class MvsData(
    val code: Int,
    val `data`: List<MvData>
)

data class MvData(
    val artists: List<Artist>,
    val cover: String,
    val duration: Int,
    val id: Int,
    val name: String,
    val playCount: Int,
)

data class Artist(
    val id: Int,
    val name: String
)