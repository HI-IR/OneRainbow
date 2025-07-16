package com.onerainbow.module.recommend.bean

/**
 * description ： 网友精选数据类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/15 11:42
 */
data class CommunityPicks(
    val cat: String,
    val code: Long,
    val more: Boolean,
    val playlists: List<Playlists>,
    val total: Long
)

data class Playlists(
    val adType: Long,
    val alg: String,
    val algType: Any,
    val anonimous: Boolean,
    val cloudTrackCount: Long,
    val commentCount: Long,
    val commentThreadId: String,
    val coverImgId: Long,
    val coverImgId_str: String,
    val coverImgUrl: String,//封面URL
    val coverStatus: Long,
    val coverText: Any,
    val createTime: Long,
    val creator: Creator,
    val description: String,//描述
    val highQuality: Boolean,
    val iconImgUrl: Any,
    val id: Long,//歌单id
    val name: String, //title
    val newImported: Boolean,
    val ordered: Boolean,
    val playCount: Long,
    val playlistType: String,
    val privacy: Long,
    val recommendInfo: Any,
    val recommendText: Any,
    val relateResId: Any,
    val relateResType: Any,
    val shareCount: Long,
    val socialPlaylistCover: Any,
    val specialType: Long,
    val status: Long,
    val subscribed: Boolean,
    val subscribedCount: Long,
    val subscribers: List<Subscriber>,
    val tags: List<String>,
    val totalDuration: Long,
    val trackCount: Long,
    val trackNumberUpdateTime: Long,
    val trackUpdateTime: Long,
    val tracks: Any,
    val tsSongCount: Long,
    val updateTime: Long,
    val userId: Long
)

data class Creator(
    val accountStatus: Long,
    val anchor: Boolean,
    val authStatus: Long,
    val authenticationTypes: Long,
    val authority: Long,
    val avatarDetail: AvatarDetail,
    val avatarImgId: Long,
    val avatarImgIdStr: String,
    val avatarUrl: String,
    val backgroundImgId: Long,
    val backgroundImgIdStr: String,
    val backgroundUrl: String,
    val birthday: Long,
    val city: Long,
    val defaultAvatar: Boolean,
    val description: String,
    val detailDescription: String,
    val djStatus: Long,
    val expertTags: Any,
    val experts: Any,
    val followed: Boolean,
    val gender: Long,
    val mutual: Boolean,
    val nickname: String,//名称
    val province: Long,
    val remarkName: Any,
    val signature: String,
    val userId: Long,
    val userType: Long,
    val vipType: Long
)

data class Subscriber(
    val accountStatus: Long,
    val anchor: Boolean,
    val authStatus: Long,
    val authenticationTypes: Long,
    val authority: Long,
    val avatarDetail: Any,
    val avatarImgId: Long,
    val avatarImgIdStr: String,
    val avatarUrl: String,
    val backgroundImgId: Long,
    val backgroundImgIdStr: String,
    val backgroundUrl: String,
    val birthday: Long,
    val city: Long,
    val defaultAvatar: Boolean,
    val description: String,
    val detailDescription: String,
    val djStatus: Long,
    val expertTags: Any,
    val experts: Any,
    val followed: Boolean,
    val gender: Long,
    val mutual: Boolean,
    val nickname: String,
    val province: Long,
    val remarkName: Any,
    val signature: String,
    val userId: Long,
    val userType: Long,
    val vipType: Long
)

data class AvatarDetail(
    val identityIconUrl: String,
    val identityLevel: Long,
    val userType: Long
)