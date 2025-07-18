package com.example.module.seek.data

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 14:31
 */
data class SingleData(
    val code: Int,
    val result: Result
)

data class Result(
    val songCount: Int,
    val songs: List<Song>
)

data class Song(
    val album: Album,
    val artists: List<ArtistX>,
    val id: Long,
    val name: String,
)

data class Album(
    val artist: ArtistX,
    val id: Long,
    val name: String,
)

data class ArtistX(
    val id: Long,
    val name: String,
)