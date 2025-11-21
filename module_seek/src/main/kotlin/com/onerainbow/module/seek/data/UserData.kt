package com.onerainbow.module.seek.data

import com.google.gson.annotations.SerializedName

/**
 * description ： 获取歌手列表
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 21:00
 */
data class UserData(
    @SerializedName("code") val code: Int,
    @SerializedName("result") val result: UserResult
)

data class UserResult(
    @SerializedName("artists") val artists: List<ArtistUser>,
)

data class ArtistUser(
    @SerializedName("id") val id: Long,
    @SerializedName("mvSize") val mvSize: Int,
    @SerializedName("name") val name: String,
    @SerializedName("picUrl") val picUrl: String,
)