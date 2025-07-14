package com.onerainbow.module.recommend.model

import com.onerainbow.lib.net.RetrofitClient
import com.onerainbow.module.recommend.bean.BannerData
import com.onerainbow.module.recommend.bean.CuratedData
import com.onerainbow.module.recommend.service.RecommendService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： 推荐的Model层
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 15:02
 */
object RecommendModel {
    private val api by lazy {
        RetrofitClient.create(RecommendService::class.java)
    }

    /**
     * 获取轮播图
     */
    fun getBanner(): Observable<BannerData> {
        return api.getBanner(1)
            //被观察者线程，网络请求所在线程
            .subscribeOn(Schedulers.io())
            //观察者的线程，把数据返回到主线程用来更新。
            .observeOn(AndroidSchedulers.mainThread())

    }

    /**
     * 获取甄选歌单
     * @param limit 获取的数量，默认11
     */
    fun getCurateList(limit: Int): Observable<CuratedData> {
        return api.getCuratedList(limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

}