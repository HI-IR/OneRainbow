package com.onerainbow.module.recommend.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onerainbow.module.recommend.bean.Banner
import com.onerainbow.module.recommend.bean.Curated
import com.onerainbow.module.recommend.bean.Playlist
import com.onerainbow.module.recommend.bean.Playlists
import com.onerainbow.module.recommend.model.RecommendModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * description ： 推荐页的ViewModel
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 15:07
 */
class RecommendViewModel:ViewModel() {
    //轮播图数据
    private val _bannerData = MutableLiveData<List<Banner>>()
    val banner:LiveData<List<Banner>> = _bannerData

    //甄选好歌
    private val _curatedList = MutableLiveData<List<Curated>>()
    val curatedList: LiveData<List<Curated>> = _curatedList

    //网友精选
    private val _communityPicks = MutableLiveData<List<Playlists>>()
    val communityPicks: LiveData<List<Playlists>> = _communityPicks

    //热歌榜
    private val _toplist = MutableLiveData<List<Playlist>>()
    val toplist:LiveData<List<Playlist>> = _toplist

    //错误
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

    fun getTopPlayList(limit: Int = 12){
        val disposable = RecommendModel.getTopPlayList(limit).subscribe({
            _communityPicks.postValue(it.playlists)
        },{
            _error.postValue(it.message)
        })
        disposables.add(disposable)
    }

    fun fetchTopList(){
        val disposable = RecommendModel.fetchTopList(listOf(19723756,3779629,2884035)).subscribe({ it ->
            _toplist.postValue(it.map { it.playlist })
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