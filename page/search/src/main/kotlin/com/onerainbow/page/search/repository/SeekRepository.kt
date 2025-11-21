package com.onerainbow.page.search.repository

import com.onerainbow.lib.net.RetrofitClient
import com.onerainbow.page.search.data.PopmusicData
import com.onerainbow.page.search.interfaces.PopmusicService
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
    private val popMusicService = RetrofitClient.create(PopmusicService::class.java)

    fun getPopMusic(): Observable<PopmusicData> {
        val ids = listOf(19723756, 3779629, 2884035)

        return Observable.fromIterable(ids)
            .concatMap { id ->
                popMusicService.getPopmusic(id)
                    .subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                println("返回数据成功：$it")
            }
            .doOnError {
                println("请求错误：${it.message}")
            }
    }

}