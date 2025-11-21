package com.onerainbow.module.seek.data

import com.google.gson.annotations.SerializedName

/**
 * description ： 获取图片Url
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/20 21:02
 */
data class MvUrl(
    @SerializedName("code") val code: Int,
    @SerializedName("data") val `data`: Data
)

data class Data(
    @SerializedName("url") val url: String
)