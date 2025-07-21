package com.onerainbow.module.top.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onerainbow.module.top.bean.DetailRank
import com.onerainbow.module.top.model.TopModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * description ： TODO:类的作用
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 15:11
 */
class DetailViewModel:ViewModel() {
    //前面显示排行歌曲的数据,(官方榜)
    private val _frontData = MutableLiveData<List<DetailRank>>()
    val frontData: LiveData<List<DetailRank>> = _frontData

    //后面只显示排行榜封面的数据(精选榜)
    private val _rankData = MutableLiveData<List<DetailRank>>()
    val rankData: LiveData<List<DetailRank>> = _rankData


    private val disposables = CompositeDisposable()

    //错误
    private val _error = MutableLiveData<String>()
    val error:LiveData<String> = _error

    private val model by lazy{
        TopModel
    }

    fun getDetailData(){
        val disposable = model.getDetailData().subscribe({
            val frontDataTemp = mutableListOf<DetailRank>()
            val rankDataTemp = mutableListOf<DetailRank>()
            for (data in it.list){
                if (data.tracks.isNullOrEmpty()) rankDataTemp.add(data)
                else frontDataTemp.add(data)
            }

            _frontData.postValue(frontDataTemp)
            _rankData.postValue(rankDataTemp)
        },{
            _error.postValue(it.message)
        })
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}