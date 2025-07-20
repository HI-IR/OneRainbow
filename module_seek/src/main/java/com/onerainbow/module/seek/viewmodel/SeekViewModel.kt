package com.onerainbow.module.seek.viewmodel

import android.util.Log
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
    val PopmusicDataLiveData = MutableLiveData<List<PopmusicData>>()

    fun getPopmusic() {
        seekRepository.getPopmusic().subscribe(object : Observer<List<PopmusicData>> {
            override fun onSubscribe(d: Disposable) {
                // 可以在这里处理订阅
            }

            override fun onNext(t: List<PopmusicData>) {
                // 更新LiveData
                Log.d("PopmusicData",t.toString())
                PopmusicDataLiveData.postValue(t)
            }

            override fun onError(e: Throwable) {
                // 处理错误
                e.printStackTrace()
            }

            override fun onComplete() {
                // 完成处理
            }
        })
    }

}