package com.onerainbow.lib.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * description ： Retrofit客户端
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/13 12:02
 */
object RetrofitClient {

    private const val BASE_URL = "http://43.139.173.183:3000/"

    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(300, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()


    // 创建 Retrofit Service 接口实例
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}


