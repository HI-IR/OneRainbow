package com.onerainbow.module.seek.interfaces

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/19 16:56
 */
interface GetImgUrl {
    fun getGetImgUrl(id :Long, callback: (String) -> Unit)
}