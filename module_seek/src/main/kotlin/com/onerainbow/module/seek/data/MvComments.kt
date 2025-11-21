package com.onerainbow.module.seek.data

import com.google.gson.annotations.SerializedName

/**
 * description ： 获取用户评论信息
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/20 20:39
 */
data class MvComments(
    @SerializedName("code") val code: Int,
    @SerializedName("comments") val comments: List<Comment>,
    @SerializedName("more") val more: Boolean,
    @SerializedName("moreHot") val moreHot: Boolean,
    @SerializedName("topComments") val topComments: List<Any?>,
    @SerializedName("total") val total: Int,
    @SerializedName("userId") val userId: Long
)

data class Comment(
    @SerializedName("id") val id: Long,
    @SerializedName("content") val content: String,
    @SerializedName("likedCount") val likedCount: Int,
    @SerializedName("timeStr") val timeStr: String,
    @SerializedName("user") val user: UserX,
)


data class UserX(
    @SerializedName("vipIconUrl") val vipIconUrl: String?, //vip图片
    @SerializedName("avatarUrl") val avatarUrl: String,
    @SerializedName("nickname") val nickname: String,
)
