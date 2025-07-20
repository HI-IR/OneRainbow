package com.onerainbow.module.seek.data

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/18 11:57
 */
data class LyricData(
    val code: Int,
    val result: ResultLyric
)

data class ResultLyric(
    val songCount: Int,
    val songs: List<SongLyric>
)

data class SongLyric(
    val album: Album,
    val artists: List<Artist>,
    val id: Long,
    val lyrics: Lyrics,
    val name: String,
)
data class Lyrics(
    val range: List<Range>,
    val txt: String
)

data class Range(
    val first: Int,
    val second: Int
)