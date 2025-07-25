package com.onerainbow.module.seek.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onerainbow.module.seek.data.Playlist
import com.onerainbow.module.seek.data.PopmusicData
import com.onerainbow.module.seek.repository.SeekRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/16 11:27
 */
class SeekViewModel :ViewModel() {
    private val seekRepository:SeekRepository=SeekRepository()

    val _PopmusicDataLiveData = MutableLiveData<List<PopmusicData>>()
    val PopmusicDataLiveData :LiveData<List<PopmusicData>> = _PopmusicDataLiveData

    private val tempList = mutableListOf<PopmusicData>()

    fun getPopmusic() {
        seekRepository.getPopmusic().subscribe(object : Observer<PopmusicData> {
            override fun onSubscribe(d: Disposable) {
                // 可选处理订阅
            }

            override fun onNext(data: PopmusicData) {
                Log.d("PopmusicData", "返回数据: $data")
                tempList.add(data)
                _PopmusicDataLiveData.postValue(tempList.toList()) // 每次追加后更新 UI
            }

            override fun onError(e: Throwable) {
                Log.e("PopmusicData", "请求出错: ${e.message}")
            }

            override fun onComplete() {
                Log.d("PopmusicData", "所有请求完成")
            }
        })
    }

}