package com.onerainbow.module.recommend.bean

/**
 * description ： TODO:类的作用
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/15 17:54
 */
data class TopList(
    val artistToplist: ArtistToplist,
    val code: Int,
    val list: List<Top>
)

data class ArtistToplist(
    val coverUrl: String,
    val name: String,
    val position: Int,
    val upateFrequency: String,
    val updateFrequency: String
)

data class Top(
    val ToplistType: String,
    val adType: Int,
    val algType: Any,
    val anonimous: Boolean,
    val artists: Any,
    val backgroundCoverId: Int,
    val backgroundCoverUrl: Any,
    val cloudTrackCount: Int,
    val commentThreadId: String,
    val coverImageUrl: Any,
    val coverImgId: Long,
    val coverImgId_str: String,
    val coverImgUrl: String,
    val coverText: Any,
    val createTime: Long,
    val creator: Any,
    val description: String,
    val englishTitle: Any,
    val highQuality: Boolean,
    val iconImageUrl: Any,
    val id: Long,   //频率
    val name: String, //名字
    val newImported: Boolean,
    val opRecommend: Boolean,
    val ordered: Boolean,
    val originalCoverId: Int,
    val playCount: Long,
    val playlistType: String,
    val privacy: Int,
    val recommendInfo: Any,
    val socialPlaylistCover: Any,
    val specialType: Int,
    val status: Int,
    val subscribed: Any,
    val subscribedCount: Int,
    val subscribers: List<Any>,
    val tags: List<String>,
    val titleImage: Int,
    val titleImageUrl: Any,
    val topTrackIds: Any,
    val totalDuration: Int,
    val trackCount: Int,
    val trackNumberUpdateTime: Long,
    val trackUpdateTime: Long,
    val tracks: Any,
    val tsSongCount: Int,
    val updateFrequency: String, //更新频率
    val updateTime: Long,
    val userId: Long
)