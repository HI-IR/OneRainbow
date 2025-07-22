package com.onerainbow.module.mv

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable


/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/21 14:48
 */
class MvViewModel : ViewModel() {
    private val mvRepository: MvRepository = MvRepository()
    val mvinlandLiveData = MutableLiveData<MvsData>()
    val mvforeignLiveData = MutableLiveData<MvsData>()

    fun getmvinland() {
        mvRepository.getMvsData("内地").subscribe(object : Observer<MvsData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: MvsData) {
                Log.d("MvinlandData", t.toString())
                mvinlandLiveData.postValue(t)
            }

        })
    }

    fun getmvForeign() {
        mvRepository.getMvsData("欧美").subscribe(object : Observer<MvsData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: MvsData) {
                Log.d("MvForeignData", t.toString())
                mvforeignLiveData.postValue(t)
            }

        })
    }

}