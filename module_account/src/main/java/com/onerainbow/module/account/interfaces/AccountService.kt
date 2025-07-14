package com.onerainbow.module.account.interfaces



import com.onerainbow.module.account.data.CaptchaData
import com.onerainbow.module.account.data.LoginCaptchaData


import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/13 19:50
 */
interface AccountService {
    @GET("captcha/sent")
    fun getCaptchaData(@Query("phone")phone:String): Observable<CaptchaData>
    @GET("captcha/verify")
    fun loginCatchaData(@Query("phone")phone: String,@Query("captcha")captcha:Int?):Observable<LoginCaptchaData>
}