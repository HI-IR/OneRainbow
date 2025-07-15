package com.onerainbow.lib.net.interceptor

import com.onerainbow.lib.base.utils.CookieUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * description ： Cookie的拦截器
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/15 10:07
 */
class CookieInterceptor : Interceptor {

    // 需要拦截的URL
    private val targetUrl :List<String> = listOf("/login/status")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().encodedPath()

        // 不在其中则直接放行
        if (url !in targetUrl){
            return chain.proceed(request)
        }


        // 读取本地Cookie
        val cookie = CookieUtils.getCookie() ?: return chain.proceed(request)

        // 构造新请求，添加 Cookie
        val newRequest = request.newBuilder()
            .addHeader("Cookie", cookie)
            .build()

        return chain.proceed(newRequest)
    }
}