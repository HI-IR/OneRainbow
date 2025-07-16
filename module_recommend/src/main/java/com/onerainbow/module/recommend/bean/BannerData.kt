package com.onerainbow.module.recommend.bean

/**
 * description ： 轮播图的数据类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 11:48
 */
data class BannerData(
    val banners: List<Banner>,
    val code: Long,
    val trp: Trp
)

data class Banner(
    val alg: String,
    val bannerBizType: String,
    val bannerId: String,
    val encodeId: String,
    val exclusive: Boolean,
    val monitorClickList: List<Any>,
    val monitorImpressList: List<Any>,
    val pic: String,
    val requestId: String,
    val s_ctrp: String,
    val scm: String,
    val showAdTag: Boolean,
    val targetId: Long,
    val targetType: Long,
    val titleColor: String,
    val typeTitle: String,
    val url: String?
)

data class Trp(
    val rules: List<String>
)