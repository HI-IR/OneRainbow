package com.onerainbow.page.musicplayer.api.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * description ： 数据类，音乐
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/16 14:25
 */
@Parcelize
data class Song(
    val id: Long,
    val name: String,
    val artists: List<Artist>,
    val coverUrl: String
): Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Song) return false
        return id == other.id // 基于id判断唯一性
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

@Parcelize
data class Artist(
    val name: String, //歌曲名
    val id: Long //作者id
): Parcelable