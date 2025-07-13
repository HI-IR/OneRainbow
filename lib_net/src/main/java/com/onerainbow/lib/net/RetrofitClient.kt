package com.onerainbow.lib.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * description ： Retrofit客户端
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/13 12:02
 */
object RetrofitClient {
    //DEV的URL
    private val BASE_URL_DEV = "http://43.139.173.183:3000"

    val BASE_URL = BASE_URL_DEV

    private val okHttpClient = OkHttpClient.Builder()
        //.addInterceptor(AuthInterceptor())//添加拦截器
        .callTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}

