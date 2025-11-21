package com.onerainbow.module.mv

import com.onerainbow.lib.net.RetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/21 14:53
 */
class MvRepository {
    private val mvService =RetrofitClient.create(MvService::class.java)

    fun getMvsData(area :String): Observable<MvsData> {
        return mvService.getMvsData(50,area)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }
}