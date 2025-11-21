package com.onerainbow.module.seek.data

import com.google.gson.annotations.SerializedName

/**
 * description ： 获取评论数量
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/20 21:26
 */
data class CommentNumber(
    @SerializedName("code") val code: Int,
    @SerializedName("commentCount") val commentCount: Int,
    @SerializedName("likedCount") val likedCount: Int,
    @SerializedName("shareCount") val shareCount: Int
)