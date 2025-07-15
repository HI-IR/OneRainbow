package com.onerainbow.module.account.interfaces

import com.onerainbow.module.account.data.KeyData
import com.onerainbow.module.account.data.QRData
import com.onerainbow.module.account.data.StateData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/15 11:13
 */
interface QRService {
    @GET("login/qr/key")
    fun getKey(): Observable<KeyData>

    @GET("login/qr/create")
    fun getQR(@Query("key") key: String, @Query("qrimg") qrimg: String): Observable<QRData>

    @GET("login/qr/check?")
    fun getState(@Query("key") key: String): Observable<StateData>
}