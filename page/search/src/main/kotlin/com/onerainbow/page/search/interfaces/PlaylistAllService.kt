package com.onerainbow.page.search.interfaces

import com.onerainbow.page.search.data.GetPlaylistData
import com.onerainbow.page.search.data.SongersData
import com.onerainbow.page.search.data.SongsData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/18 20:32
 */
interface PlaylistAllService {
    @GET("playlist/track/all")
    fun getPlaylistAll(@Query("id") id: Long, ): Observable<GetPlaylistData>

    @GET("artists")
    fun getSongerData(@Query("id") id: Long): Observable<SongersData>

    @GET("artist/top/song")
    fun getSongsData(@Query("id") id: Long): Observable<SongsData>
}