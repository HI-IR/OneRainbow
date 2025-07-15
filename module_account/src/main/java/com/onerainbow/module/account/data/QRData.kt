package com.onerainbow.module.account.data

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/15 11:04
 */
data class QRData(
    val code: Int,
    val `data`: DataQR
)

data class DataQR(
    val qrimg: String,
    val qrurl: String
)