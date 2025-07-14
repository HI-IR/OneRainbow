package com.onerainbow.module.account.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onerainbow.module.account.data.CaptchaData
import com.onerainbow.module.account.data.CaptchaDataResult
import com.onerainbow.module.account.data.LoginCaptchaData
import com.onerainbow.module.account.data.LoginCaptchaDataResult
import com.onerainbow.module.account.repository.CaptchaRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable


/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/13 19:31
 */
class LoginViewModel(application: Application):AndroidViewModel(application) {
    private val repository: CaptchaRepository = CaptchaRepository()
    private val _result= MutableLiveData<CaptchaDataResult<CaptchaData>>()
    private val _loginResult = MutableLiveData<LoginCaptchaDataResult<LoginCaptchaData>>()
    val loginResult:LiveData<LoginCaptchaDataResult<LoginCaptchaData>> = _loginResult
    val result: LiveData<CaptchaDataResult<CaptchaData>> =_result

    fun getCaptchaResult(email:String) {
        repository.getCaptchData(email).subscribe(object : Observer<CaptchaData> {
            override fun onSubscribe(d:Disposable){

            }
            override fun onError(e:Throwable){
                Log.d("Error","${e.message}")
            }

            override fun onComplete() {

            }

            override fun onNext(t:CaptchaData){
                _result.postValue(CaptchaDataResult.Success(t))

            }

        })
    }
    fun getLoginCaptchaResult(email:String, number: Int?) {
        repository.getloginCaptchData(email,number).subscribe(object : Observer<LoginCaptchaData> {
            override fun onSubscribe(d:Disposable){

            }

            override fun onNext(t: LoginCaptchaData) {
                Log.d("test", "onNext:${LoginCaptchaDataResult.Success(t)} ")
                _loginResult.postValue(LoginCaptchaDataResult.Success(t))
            }

            override fun onError(e:Throwable){
                Log.d("Error","${e.message}")
            }

            override fun onComplete() {

            }

        })
    }


}