package com.onerainbow.module.recommend.service

import com.onerainbow.module.recommend.bean.BannerData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import java.sql.Types

/**
 * description ： 推荐页的网络访问接口
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 14:45
 */
interface RecommendService {

    //Banner
    @GET("/banner")
    fun getBanner(
        @Query("type") types: Int
    ):Observable<BannerData>
}