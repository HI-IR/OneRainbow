package com.onerainbow.module.recommend.bean

import com.google.gson.annotations.SerializedName

/**
 * description ： 网友精选数据类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/15 11:42
 */
data class CommunityPicks(
    @SerializedName("playlists") val playlists: List<Playlists>,
)

data class Playlists(
    @SerializedName("coverImgUrl") val coverImgUrl: String,//封面URL
    @SerializedName("creator") val creator: Creator,
    @SerializedName("description") val description: String,//描述
    @SerializedName("id") val id: Long,//歌单id
    @SerializedName("name") val name: String, //title
    @SerializedName("trackCount") val trackCount: Long,
    @SerializedName("userId") val userId: Long
)

data class Creator(
    @SerializedName("avatarUrl") val avatarUrl: String,
    @SerializedName("nickname") val nickname: String,//名称
    @SerializedName("userId") val userId: Long,
)