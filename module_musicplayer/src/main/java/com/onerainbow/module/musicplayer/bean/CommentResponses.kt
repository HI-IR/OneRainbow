package com.onerainbow.module.musicplayer.bean

import com.google.gson.annotations.SerializedName
import com.onerainbow.module.musicplayer.domain.Comment
import com.onerainbow.module.musicplayer.domain.User

/**
 * description ： 评论的网络访问
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/19 10:40
 */
data class CommentResponses(
    @SerializedName("comments") val comments: List<CommentInfo>,
    @SerializedName("more") val more: Boolean
)

data class CommentInfo(
    @SerializedName("commentId") val commentId: Long,
    @SerializedName("content") val content: String,
    @SerializedName("likedCount") val likedCount: Long,
    @SerializedName("timeStr") val timeStr: String,
    @SerializedName("user") val user: UserX,
)

data class UserX(
    @SerializedName("avatarUrl") val avatarUrl: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("vipRights") val vipRights: VipRights,
)

data class VipRights(
    @SerializedName("associator") val associator: Associator?,
)

data class Associator(
    @SerializedName("iconUrl") val iconUrl: String,
)

//扩展函数将Response转化为Comment数据类
fun CommentInfo.toComment(): Comment {
    val iconUrl = user.vipRights?.associator?.iconUrl ?: "default_icon_url"
    return Comment(
        commentId,
        User(user.avatarUrl, iconUrl, user.nickname),
        content,
        timeStr,
        likedCount
    )
}

