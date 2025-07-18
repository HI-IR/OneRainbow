package com.onerainbow.module.musicplayer.repository

import com.onerainbow.module.musicplayer.model.SongURL
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * description ： 音乐播放器的网络访问
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/18 20:27
 */
interface MusicRepo {
    @GET("/song/url")
    fun getUrlById(
        @Query("id") id:Long
    ):Observable<SongURL>
}