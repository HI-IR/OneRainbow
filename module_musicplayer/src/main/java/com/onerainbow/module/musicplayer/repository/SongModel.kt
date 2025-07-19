package com.onerainbow.module.musicplayer.repository

import com.onerainbow.lib.net.RetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/18 20:31
 */
object SongModel {
    private val api by lazy {
        RetrofitClient.create(MusicApi::class.java)
    }

    fun getSongById(songId: Long): Observable<String> {
        return api.getUrlById(songId)
            .subscribeOn(Schedulers.io())
            .map { response ->
                if (response.data.isNotEmpty()) {
                    val songData = response.data.first()
                    if (songData.url.isNotBlank()) {
                        return@map songData.url // 有效 url，返回
                    } else {
                        return@map "发生错误：歌曲 URL 为空" // url 为空的错误
                    }
                } else {
                    return@map "发生错误：歌曲数据为空" // data 为空的错误
                }
            }
            .onErrorReturn { error ->
                "发生错误：网络请求失败（${error.message ?: "未知错误"}）"
            }.observeOn(AndroidSchedulers.mainThread())
    }
}