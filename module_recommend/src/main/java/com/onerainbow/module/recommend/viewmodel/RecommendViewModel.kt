package com.onerainbow.module.recommend.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onerainbow.module.recommend.bean.Banner
import com.onerainbow.module.recommend.bean.BannerData
import com.onerainbow.module.recommend.bean.Curated
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

    private val _curatedList = MutableLiveData<List<Curated>>()
    val curatedList: LiveData<List<Curated>> = _curatedList




    private val _error = MutableLiveData<String>()
    val error:LiveData<String> = _error


    private val disposables = CompositeDisposable()

    /**
     * 获取推荐页Banner
     */
    fun getBanner(){
        val disposable = RecommendModel.getBanner().subscribe({
            _bannerData.postValue(it.banners)
        },{
            _error.postValue(it.message)
        }
        )
        disposables.add(disposable)
    }

    /**
     * 获取甄选歌单
     * @param limit 获取的数量，默认11
     */
    fun getCurateList(limit: Int = 11){
        val disposable = RecommendModel.getCurateList(limit).subscribe({
            _curatedList.postValue(it.result)
        },{
            _error.postValue(it.message)
        })
        disposables.add(disposable)
    }


    override fun onCleared() {
        super.onCleared()
        disposables.clear() // 释放所有订阅，防止内存泄漏
    }



}