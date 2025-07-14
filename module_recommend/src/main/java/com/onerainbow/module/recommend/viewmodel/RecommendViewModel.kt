package com.onerainbow.module.recommend.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onerainbow.module.recommend.bean.Banner
import com.onerainbow.module.recommend.bean.BannerData
import com.onerainbow.module.recommend.model.RecommendModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * description ： 推荐页的ViewModel
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 15:07
 */
class RecommendViewModel:ViewModel() {
    private val _bannerData = MutableLiveData<List<Banner>>()
    val banner:LiveData<List<Banner>> = _bannerData

    private val _error = MutableLiveData<String>()
    val error:LiveData<String> = _error

    private val disposables = CompositeDisposable()
    fun getBanner(){
        val disposable = RecommendModel.getBanner().subscribe({
            _bannerData.postValue(it.banners)
        },{
            _error.postValue(it.message)
        }
        )
        disposables.add(disposable)
    }


}