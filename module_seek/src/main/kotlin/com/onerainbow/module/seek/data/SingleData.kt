package com.onerainbow.module.seek.data

import com.google.gson.annotations.SerializedName

/**
 * description ： 获取单曲信息
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 14:31
 */
data class SingleData(
    @SerializedName("code") val code: Int,
    @SerializedName("result") val result: Result
)

data class Result(
    @SerializedName("songCount") val songCount: Int,
    @SerializedName("songs") val songs: List<Songi>
)

data class Songi(
    @SerializedName("album") val album: Album,
    @SerializedName("artists") val artists: List<Artist>,
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
)

data class Album(
    @SerializedName("artist") val artist: Artist,
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("img1v1Url") val img1v1Url :String
)

data class Artist(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("img1v1Url") val img1v1Url :String
)