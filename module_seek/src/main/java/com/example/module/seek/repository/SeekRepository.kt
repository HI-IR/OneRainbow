package com.example.module.seek.repository

import com.example.module.seek.data.Playlist
import com.example.module.seek.data.PopmusicData
import com.example.module.seek.interfaces.PopmusicService
import com.onerainbow.lib.net.RetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/16 10:38
 */
class SeekRepository {
    private val popmusicService = RetrofitClient.create(PopmusicService::class.java)

    fun getPopmusic(): Observable<List<PopmusicData>> {
        return Observable.zip(
            popmusicService.getPopmusic(19723756),
            popmusicService.getPopmusic(3779629),
            popmusicService.getPopmusic(2884035),
            object : Function3<PopmusicData, PopmusicData, PopmusicData, List<PopmusicData>> {
                override fun invoke(
                    pop1: PopmusicData,
                    pop2: PopmusicData,
                    pop3: PopmusicData
                ): List<PopmusicData> {
                    return listOf(
                        pop1,pop2,pop3
                    )
                }
            }
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                println("返回数据成功：$it")
            }
            .doOnError {
                println("请求错误：${it.message}")
            }
    }
}