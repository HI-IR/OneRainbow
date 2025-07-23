package com.onerainbow.module.seek.data

/**
 * description ： 获取图片Url
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/20 21:02
 */
data class MvUrl(
    val code: Int,
    val `data`: Data
)

data class Data(
    val url: String
)