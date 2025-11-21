package com.onerainbow.module.recommend.service

import com.onerainbow.module.recommend.bean.BannerData
import com.onerainbow.module.recommend.bean.CommunityPicks
import com.onerainbow.module.recommend.bean.CuratedData
import com.onerainbow.module.recommend.bean.SongLists
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * description ： 推荐页的网络访问接口
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 14:45
 */
interface RecommendService {

    /**
     * Banner
     */
    @GET("/banner")
    fun getBanner(
        @Query("type") types: Int
    ):Observable<BannerData>


    /**
     *甄选歌单
     */
    @GET("/personalized")
    fun getCuratedList(
        @Query("limit") limit: Int
    ):Observable<CuratedData>


    /**
     * 热歌精选
     */
    @GET("/top/playlist")
    fun getTopPlayList(
        @Query("limit") limit: Int
    ):Observable<CommunityPicks>


    /**
     * 获取歌单信息
     * 热搜榜:19723756
     * 新歌榜:3779629
     * 原创榜:2884035
     */
    @GET("/playlist/detail")
    fun getSongLists(
        @Query("id") id: Int
    ): Single<SongLists>
}