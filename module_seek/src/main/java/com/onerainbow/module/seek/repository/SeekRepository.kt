package com.onerainbow.module.seek.repository

import com.onerainbow.module.seek.data.Playlist
import com.onerainbow.module.seek.data.PopmusicData
import com.onerainbow.module.seek.interfaces.PopmusicService
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
        return Observable
            .mergeDelayError(
                popmusicService.getPopmusic(19723756),
                popmusicService.getPopmusic(3779629),
                popmusicService.getPopmusic(2884035)
            )//mergeDelayError即使有一个返回错误仍然可以返回数据
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toList() // 把3个结果收集成List<PopmusicData>
            .toObservable()
            .doOnNext {
                println("返回数据成功：$it")
            }
            .doOnError {
                println("请求错误：${it.message}")
            }
    }
}