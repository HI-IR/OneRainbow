package com.onerainbow.module.account.repository

import com.onerainbow.lib.net.RetrofitClient
import com.onerainbow.module.account.data.QRData
import com.onerainbow.module.account.data.StateData
import com.onerainbow.module.account.interfaces.QRService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/15 10:44
 */
class LoginRepository {
    private var unikey: String = ""
    private val qrService = RetrofitClient.create(QRService::class.java)
    fun getQR(): Observable<QRData> {
        return qrService.getKey()
            .flatMap { keyData ->
                unikey = keyData.data.unikey
                qrService.getQR(unikey, "true")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getState(): Observable<StateData> {
        return qrService.getState(unikey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun startPollingQrState(): Observable<StateData> {
        return Observable.interval(5, 2, TimeUnit.SECONDS) // 立即执行一次，然后每 2秒执行
            .flatMap {
                getState() // 每次调用接口
            }
            .takeUntil { stateData ->
                // 满足条件时自动停止轮询
                stateData.code == 800 || stateData.code == 803
            }

    }


}