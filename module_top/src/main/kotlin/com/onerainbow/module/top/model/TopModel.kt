package com.onerainbow.module.top.model

import com.onerainbow.lib.net.RetrofitClient
import com.onerainbow.module.top.bean.ArtistData
import com.onerainbow.module.top.bean.DetailData
import com.onerainbow.module.top.net.TopService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： 推荐页的网络访问
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 15:12
 */
object TopModel {
    private val api by lazy {
        RetrofitClient.create(TopService::class.java)
    }

    fun getDetailData():Observable<DetailData>{
        return api.getDetail()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getArtistsRank():Observable<ArtistData>{
        return api.getArtistsRank()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}