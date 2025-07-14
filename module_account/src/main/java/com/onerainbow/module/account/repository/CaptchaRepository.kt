package com.onerainbow.module.account.repository


import com.onerainbow.lib.net.RetrofitClient
import com.onerainbow.module.account.data.CaptchaData
import com.onerainbow.module.account.data.LoginCaptchaData
import com.onerainbow.module.account.interfaces.AccountService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/13 19:35
 */
class CaptchaRepository {
    private val accountService = RetrofitClient.create(AccountService::class.java)


    fun getCaptchData(email: String): Observable<CaptchaData> {
        return accountService.getCaptchaData(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun getloginCaptchData(email: String,number:Int?):Observable<LoginCaptchaData>{
        return  accountService.loginCatchaData(email,number)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

}