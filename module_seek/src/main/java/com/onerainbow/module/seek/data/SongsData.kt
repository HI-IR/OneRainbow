package com.onerainbow.module.seek.data

import com.google.gson.annotations.SerializedName

/**
 * description ： 获取歌手对应单曲信息
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/19 15:51
 */
data class SongsData(
    @SerializedName("code") val code: Int,
    @SerializedName("more") val more: Boolean,
    @SerializedName("songs") val songs: List<SongData>
)

data class SongData(
    @SerializedName("al")  val al: AlData,
    @SerializedName("ar")  val ar: List<Artist>,
    @SerializedName("id")  val id: Long,
    @SerializedName("name")  val name: String,
)

data class AlData(
    @SerializedName("id")  val id: Int,
    @SerializedName("name")  val name: String,
    @SerializedName("picUrl")  val picUrl: String,
)

