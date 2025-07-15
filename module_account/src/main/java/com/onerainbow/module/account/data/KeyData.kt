package com.onerainbow.module.account.data

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/15 11:05
 */
data class KeyData(
    val code: Int,
    val `data`: DataKey
)

data class DataKey(
    val code: Int,
    val unikey: String
)