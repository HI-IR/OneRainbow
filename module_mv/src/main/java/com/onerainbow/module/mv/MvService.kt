package com.onerainbow.module.mv


import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/21 14:49
 */
interface MvService {
    @GET("mv/first")
    fun getMvsData(@Query("limit")limit:Int,@Query("area")area:String) :Observable<MvsData>
}