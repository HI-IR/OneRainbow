package com.onerainbow.module.seek.data

/**
 * description ： 获取评论数量
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/20 21:26
 */
data class CommentNumber(
    val code: Int,
    val commentCount: Int,
    val likedCount: Int,
    val shareCount: Int
)