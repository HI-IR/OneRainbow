package com.onerainbow.module.musicplayer.repository

import com.onerainbow.module.musicplayer.bean.CommentResponses
import com.onerainbow.module.musicplayer.bean.SongURL
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * description ： 音乐播放器的网络访问
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/18 20:27
 */
interface MusicApi {
    @GET("/song/url")
    fun getUrlById(
        @Query("id") id:Long
    ):Observable<SongURL>

    @GET("/comment/music")
    fun getMusicComments(
        @Query("id") id: Long,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Observable<CommentResponses>

}