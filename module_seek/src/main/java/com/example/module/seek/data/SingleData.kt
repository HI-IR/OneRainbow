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
    val songs: List<Songi>
)

data class Songi(
    val album: Album,
    val artists: List<Artist>,
    val id: Long,
    val name: String,
)

data class Album(
    val artist: Artist,
    val id: Long,
    val name: String,
    val img1v1Url :String
)

data class Artist(
    val id: Long,
    val name: String,
    val img1v1Url :String
)