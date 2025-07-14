package com.onerainbow.module.recommend.bean

/**
 * description ： 甄选歌单的数据类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 19:40
 */
data class CuratedData(
    val category: Int,
    val code: Int,
    val hasTaste: Boolean,
    val result: List<Curated>
)

data class Curated(
    val alg: String,
    val canDislike: Boolean,
    val copywriter: String,//歌单文案
    val highQuality: Boolean,
    val id: Long,//歌单ID
    val name: String,//歌单名
    val picUrl: String,//图片URL
    val playCount: Int,//播放量
    val trackCount: Int,//歌单中歌曲的数量
    val trackNumberUpdateTime: Long,//歌单最后更新歌曲数量的时间
    val type: Int//类型
)