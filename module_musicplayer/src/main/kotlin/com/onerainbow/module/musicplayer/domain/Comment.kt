package com.onerainbow.module.musicplayer.domain

/**
 * description ： 评论能用的数据类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/19 10:29
 */
data class Comment(
    val id :Long,//评论的ID
    val user: User,//用户
    val content: String,//内容
    val timeStr: String,//评论时间
    val likeCount: Long
)

data class User(
    val avatarUrl: String, //头像
    val vipIconUrl:String?, //vip图片
    val nickname: String,  //nickName
)
