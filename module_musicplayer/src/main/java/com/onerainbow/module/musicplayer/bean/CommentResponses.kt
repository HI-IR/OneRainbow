package com.onerainbow.module.musicplayer.bean

import com.onerainbow.module.musicplayer.model.Comment
import com.onerainbow.module.musicplayer.model.User

/**
 * description ： 评论的网络访问
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/19 10:40
 */
data class CommentResponses(
    val cnum: Long,
    val code: Long,
    val commentBanner: Any,
    val comments: List<CommentInfo>,
    val hotComments: List<HotComment>,
    val isMusician: Boolean,
    val more: Boolean,
    val moreHot: Boolean,
    val topComments: List<Any?>,
    val total: Long,
    val userId: Long
)

data class CommentInfo(
    val beReplied: List<BeReplied>,
    val commentId: Long,
    val commentLocationType: Long,
    val content: String,
    val contentResource: Any,
    val decoration: Decoration,
    val expressionUrl: Any,
    val grade: Any,
    val ipLocation: IpLocationX,
    val likeAnimationMap: LikeAnimationMap,
    val liked: Boolean,
    val likedCount: Long,
    val medal: Any,
    val needDisplayTime: Boolean,
    val owner: Boolean,
    val parentCommentId: Long,
    val pendantData: Any,
    val repliedMark: Any,
    val richContent: Any,
    val showFloorComment: Any,
    val status: Long,
    val time: Long,
    val timeStr: String,
    val user: UserX,
    val userBizLevels: Any
)

data class HotComment(
    val beReplied: List<BeRepliedX>,
    val commentId: Long,
    val commentLocationType: Long,
    val content: String,
    val contentResource: Any,
    val decoration: Decoration,
    val expressionUrl: Any,
    val grade: Any,
    val ipLocation: IpLocationX,
    val likeAnimationMap: LikeAnimationMap,
    val liked: Boolean,
    val likedCount: Long,
    val medal: Any,
    val needDisplayTime: Boolean,
    val owner: Boolean,
    val parentCommentId: Long,
    val pendantData: PendantData,
    val repliedMark: Any,
    val richContent: Any,
    val showFloorComment: Any,
    val status: Long,
    val time: Long,
    val timeStr: String,
    val user: UserXXX,
    val userBizLevels: Any
)

data class BeReplied(
    val beRepliedCommentId: Long,
    val content: String,
    val expressionUrl: Any,
    val ipLocation: IpLocationX,
    val richContent: Any,
    val status: Long,
    val user: User
)

class Decoration

data class IpLocationX(
    val ip: Any,
    val location: String,
    val userId: Any
)

class LikeAnimationMap

data class UserX(
    val anonym: Long,
    val authStatus: Long,
    val avatarDetail: Any,
    val avatarUrl: String,
    val commonIdentity: Any,
    val expertTags: Any,
    val experts: Any,
    val followed: Boolean,
    val highlight: Boolean,
    val liveInfo: Any,
    val locationInfo: Any,
    val mutual: Boolean,
    val nickname: String,
    val remarkName: Any,
    val socialUserId: Any,
    val target: Any,
    val userId: Long,
    val userType: Long,
    val vipRights: VipRights,
    val vipType: Long
)

data class User(
    val anonym: Long,
    val authStatus: Long,
    val avatarDetail: Any,
    val avatarUrl: String,
    val commonIdentity: Any,
    val expertTags: Any,
    val experts: Any,
    val followed: Boolean,
    val highlight: Boolean,
    val liveInfo: Any,
    val locationInfo: Any,
    val mutual: Boolean,
    val nickname: String,
    val remarkName: Any,
    val socialUserId: Any,
    val target: Any,
    val userId: Long,
    val userType: Long,
    val vipRights: Any,
    val vipType: Long
)

data class VipRights(
    val associator: Associator?,
    val extInfo: Any,
    val memberLogo: Any,
    val musicPackage: MusicPackage,
    val redVipAnnualCount: Long,
    val redVipLevel: Long,
    val redplus: Redplus,
    val relationType: Long
)

data class Associator(
    val iconUrl: String,
    val rights: Boolean,
    val vipCode: Long
)

data class MusicPackage(
    val iconUrl: String,
    val rights: Boolean,
    val vipCode: Long
)

data class Redplus(
    val iconUrl: String,
    val rights: Boolean,
    val vipCode: Long
)

data class BeRepliedX(
    val beRepliedCommentId: Long,
    val content: String,
    val expressionUrl: Any,
    val ipLocation: IpLocationX,
    val richContent: Any,
    val status: Long,
    val user: UserXX
)

data class PendantData(
    val id: Long,
    val imageUrl: String
)

data class UserXXX(
    val anonym: Long,
    val authStatus: Long,
    val avatarDetail: AvatarDetail,
    val avatarUrl: String,
    val commonIdentity: Any,
    val expertTags: Any,
    val experts: Any,
    val followed: Boolean,
    val highlight: Boolean,
    val liveInfo: Any,
    val locationInfo: Any,
    val mutual: Boolean,
    val nickname: String,
    val remarkName: Any,
    val socialUserId: Any,
    val target: Any,
    val userId: Long,
    val userType: Long,
    val vipRights: VipRights,
    val vipType: Long
)

data class UserXX(
    val anonym: Long,
    val authStatus: Long,
    val avatarDetail: AvatarDetail,
    val avatarUrl: String,
    val commonIdentity: Any,
    val expertTags: Any,
    val experts: Any,
    val followed: Boolean,
    val highlight: Boolean,
    val liveInfo: Any,
    val locationInfo: Any,
    val mutual: Boolean,
    val nickname: String,
    val remarkName: Any,
    val socialUserId: Any,
    val target: Any,
    val userId: Long,
    val userType: Long,
    val vipRights: Any,
    val vipType: Long
)

data class AvatarDetail(
    val identityIconUrl: String,
    val identityLevel: Long,
    val userType: Long
)
//扩展函数将Response转化为Comment数据类
fun CommentInfo.toComment(): Comment{
    val iconUrl = user.vipRights?.associator?.iconUrl ?: "default_icon_url"
    return Comment(commentId,
        User(user.avatarUrl,iconUrl,user.nickname),
        content,
        timeStr,
        likedCount
    )
}

