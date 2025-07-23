package com.onerainbow.module.seek.data

/**
 * description ： 获取用户评论信息
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/20 20:39
 */
data class MvComments(
    val code: Int,
    val comments: List<Comment>,
    val more: Boolean,
    val moreHot: Boolean,
    val topComments: List<Any?>,
    val total: Int,
    val userId: Long
)

data class Comment(
    val id :Long,
    val content: String,
    val likedCount: Int,
    val timeStr: String,
    val user: UserX,
)



data class UserX(
    val vipIconUrl:String?, //vip图片
    val avatarUrl: String,
    val nickname: String,
)
