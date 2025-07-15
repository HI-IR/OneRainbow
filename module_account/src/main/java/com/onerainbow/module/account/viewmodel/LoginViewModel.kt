package com.onerainbow.module.account.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onerainbow.lib.base.utils.CookieUtils
import com.onerainbow.module.account.data.QRData
import com.onerainbow.module.account.data.StateData
import com.onerainbow.module.account.repository.LoginRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/15 10:44
 */
class LoginViewModel : ViewModel() {
    private val loginRepository: LoginRepository = LoginRepository()
    val qrDataLiveData = MutableLiveData<QRData>()
    val stateDataLiveData = MutableLiveData<StateData>()

    fun getQR() {
        loginRepository.getQR().subscribe(object : Observer<QRData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: QRData) {
                qrDataLiveData.postValue(t)
            }

            override fun onError(e: Throwable) {
                Log.d("ErrorQR", "${e.message}")
            }

            override fun onComplete() {

            }

        }

        )


    }

    fun getState() {
        loginRepository.startPollingQrState().subscribe(object : Observer<StateData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                Log.d("ErrorQRstate", "${e.message}")
            }

            override fun onComplete() {

            }

            override fun onNext(t: StateData) {

                stateDataLiveData.postValue(t)
            }

        })
    }

}