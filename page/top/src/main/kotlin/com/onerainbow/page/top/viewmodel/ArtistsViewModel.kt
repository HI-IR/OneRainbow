package com.onerainbow.page.top.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onerainbow.page.top.bean.Artist
import com.onerainbow.page.top.model.TopModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * description ： 歌手榜的推荐页面
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 20:19
 */
class ArtistsViewModel: ViewModel() {
    private val _artistsData = MutableLiveData<List<Artist>>()
    val artistsData :LiveData<List<Artist>> = _artistsData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = _isLoading

    private val disposables = CompositeDisposable()

    init {
        fetchData()
    }

    fun getArtistsRank(){
        // 如果正在加载，不重复请求
        if (_isLoading.value == true) return

        _isLoading.postValue(true)
        val disposable = TopModel.getArtistsRank().subscribe({
            _artistsData.postValue(it.list.artists)
            _isLoading.postValue(false)
       },{
           _error.postValue(it.message)
            _isLoading.postValue(false)
       })
        disposables.add(disposable)
    }

    fun fetchData(){
        getArtistsRank()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}