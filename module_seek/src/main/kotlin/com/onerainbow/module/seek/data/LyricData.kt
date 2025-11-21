package com.onerainbow.module.seek.data

import com.google.gson.annotations.SerializedName

/**
 * description ： 获取歌词相关信息
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/18 11:57
 */
data class LyricData(
    @SerializedName("code") val code: Int,
    @SerializedName("result") val result: ResultLyric
)

data class ResultLyric(
    @SerializedName("songCount") val songCount: Int,
    @SerializedName("songs") val songs: List<SongLyric>
)

data class SongLyric(
    @SerializedName("album") val album: Album,
    @SerializedName("artists") val artists: List<Artist>,
    @SerializedName("id") val id: Long,
    @SerializedName("lyrics") val lyrics: Lyrics,
    @SerializedName("name") val name: String,
)
data class Lyrics(
    @SerializedName("range") val range: List<Range>,
    @SerializedName("txt") val txt: String
)

data class Range(
    @SerializedName("first") val first: Int,
    @SerializedName("second") val second: Int
)