package com.onerainbow.module.seek.data

import com.google.gson.annotations.SerializedName

/**
 * description ： 获取歌单对应歌曲
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/18 20:47
 */
data class GetPlaylistData(
    @SerializedName("code") val code: Int,
    @SerializedName("songs") val songs: List<SongGetPlay>
)

data class SongGetPlay(
    @SerializedName("al") val al: Al,
    @SerializedName("ar") val ar: List<Artist>,
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
)

data class Al(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("picUrl") val picUrl: String,
)
