package com.onerainbow.module.top.bean

import com.onerainbow.module.seek.data.Creator
import com.onerainbow.module.seek.data.Playlists

/**
 * description ： 排行榜概要的数据类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 14:22
 */
data class DetailData(
    val code: Long,
    val list: List<DetailRank>,
)

data class DetailRank(
    val coverImgUrl: String,//封面图片
    val description: String,//描述
    val id: Long,//歌单ID
    val name: String,//榜单名
    val tracks: List<DetailTrack?>,//歌曲
    val updateFrequency: String,//更新速率
    val trackCount: Long
)

data class DetailTrack(
    val first: String,//歌名
    val second: String //创作者
)


fun DetailRank.toPlaylists(): Playlists {
    return Playlists(
        coverImgUrl = this.coverImgUrl,
        creator = Creator(
            avatarUrl = this.coverImgUrl,
            nickname = "帅哥",
            userId = -1
        ),
        description = this.description ?: " ",
        id = this.id,
        name = this.name,
        userId = -1,
        trackCount = this.trackCount.toInt()
    )
}