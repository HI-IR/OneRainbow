package com.onerainbow.module.seek.interfaces

import com.onerainbow.module.seek.data.CommentNumber
import com.onerainbow.module.seek.data.MvComments
import com.onerainbow.module.seek.data.MvUrl
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/20 20:33
 */
interface MvService {
    @GET("mv/url")
    fun getMvUrl(@Query("id")id:Long):Observable<MvUrl>
    @GET("comment/mv")
    fun getMvComments(@Query("id")id: Long):Observable<MvComments>
    @GET("mv/detail/info")
    fun getMvCommentNumber(@Query("mvid")mvid:Long):Observable<CommentNumber>


}