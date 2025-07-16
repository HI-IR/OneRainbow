package com.example.module.seek.interfaces

import com.example.module.seek.data.Playlist
import com.example.module.seek.data.PopmusicData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/16 10:43
 */

interface PopmusicService {
    @GET("playlist/detail")
    fun getPopmusic(@Query("id")id:Int):Observable<PopmusicData>
}