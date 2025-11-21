package com.onerainbow.module.seek.interfaces


import com.onerainbow.module.seek.data.GetMvData
import com.onerainbow.module.seek.data.LyricData
import com.onerainbow.module.seek.data.Playlist
import com.onerainbow.module.seek.data.PlaylistData
import com.onerainbow.module.seek.data.SingleData
import com.onerainbow.module.seek.data.UrlData
import com.onerainbow.module.seek.data.UserData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 15:12
 */
interface FinishSeekDataService {
    @GET("search")
    fun getSinglekData(@Query("keywords")keyword:String,@Query("type")type:Int): Observable<SingleData>
    @GET("search")
    fun getPlaylistData(@Query("keywords")keyword:String,@Query("type")type:Int) :Observable<PlaylistData>
    @GET("search")
    fun getUserData(@Query("keywords")keyword:String,@Query("type")type:Int) :Observable<UserData>
    @GET("search")
    fun getLyricData(@Query("keywords")keyword:String,@Query("type")type:Int) :Observable<LyricData>
    @GET("search")
    fun getGetMvData(@Query("keywords")keyword:String,@Query("type")type:Int) :Observable<GetMvData>
    @GET("song/detail")
    fun getImgUrl(@Query("ids")id: Long):Observable<UrlData>
}
