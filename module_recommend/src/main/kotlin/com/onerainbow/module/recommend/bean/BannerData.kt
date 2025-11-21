package com.onerainbow.module.recommend.bean

import com.google.gson.annotations.SerializedName

/**
 * description ： 轮播图的数据类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 11:48
 */
data class BannerData(
    @SerializedName("banners") val banners: List<Banner>,
    @SerializedName("code") val code: Long,
)

data class Banner(
    @SerializedName("bannerId") val bannerId: String,
    @SerializedName("pic") val pic: String,
    @SerializedName("typeTitle") val typeTitle: String,
    @SerializedName("url") val url: String?
)
