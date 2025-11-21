package com.onerainbow.module.top.net

import com.onerainbow.module.top.bean.ArtistData
import com.onerainbow.module.top.bean.DetailData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

/**
 * description ： 热门页的ApiService
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 14:21
 */
interface TopService {

    @GET("/toplist/detail")
    fun getDetail(): Observable<DetailData>

    @GET("/toplist/artist")
    fun getArtistsRank(): Observable<ArtistData>
}