package com.onerainbow.page.search.repository

import com.onerainbow.lib.net.RetrofitClient
import com.onerainbow.page.search.data.CommentNumber
import com.onerainbow.page.search.data.MvUrl
import com.onerainbow.page.search.interfaces.MvService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/20 21:06
 */
class VideoRepository {
    private val videoService = RetrofitClient.create(MvService::class.java)

    fun getUrl(id : Long):Observable<MvUrl> {
        return videoService.getMvUrl(id)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    fun getCommentNumber(id: Long):Observable<CommentNumber>{
        return videoService.getMvCommentNumber(id)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
    }
}