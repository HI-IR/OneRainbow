package com.onerainbow.module.seek.repository

import com.onerainbow.lib.net.RetrofitClient
import com.onerainbow.module.seek.data.CommentNumber
import com.onerainbow.module.seek.data.MvComments
import com.onerainbow.module.seek.data.MvUrl
import com.onerainbow.module.seek.interfaces.MvService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.IDN

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/20 21:06
 */
class VideoRepository {
    private val viedeoService = RetrofitClient.create(MvService::class.java)

    fun getUrl(id : Long):Observable<MvUrl> {
        return viedeoService.getMvUrl(id)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    fun getCommentNumber(id: Long):Observable<CommentNumber>{
        return viedeoService.getMvCommentNumber(id)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
    }
}