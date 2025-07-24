package com.onerainbow.module.recommend.bean

import com.google.gson.annotations.SerializedName

/**
 * description ： 歌单详情的数据类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/15 17:35
 */
data class SongLists(
    @SerializedName("playlist") val playlist: Playlist,
)

data class Playlist(
    @SerializedName("coverImgUrl") val coverImgUrl: String,//封面URL
    @SerializedName("creator") val creator: Creator2,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,//名字
    @SerializedName("trackCount") val trackCount: Long,
    @SerializedName("tracks") val tracks: List<Track>,//歌曲
    @SerializedName("userId") val userId: Long,
)
data class Creator2(
    @SerializedName("avatarUrl") val avatarUrl: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("userId") val userId: Long,
)

data class Track(
    @SerializedName("ar") val ar: List<Ar>,
    @SerializedName("name") val name: String,//歌名
)
data class Ar(
    @SerializedName("name") val name: String,
)