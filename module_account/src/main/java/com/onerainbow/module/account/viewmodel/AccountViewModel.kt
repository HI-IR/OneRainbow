package com.onerainbow.module.account.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onerainbow.lib.base.utils.UsernameUtils
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.account.model.AccountModel
import com.therouter.router.Route
import kotlinx.coroutines.launch

/**
 * description ： 账户的ViewModel
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/23 11:56
 */

class AccountViewModel: ViewModel() {
    private val model by lazy {
        AccountModel()
    }

    private val _result: MutableLiveData<String> = MutableLiveData()
    val result: LiveData<String> = _result

    private val _error: MutableLiveData<String> = MutableLiveData()
    val  error: LiveData<String> = _error



    fun doRegistered(username: String,password: String){
        if (username.isEmpty() || password.isEmpty()){
            _error.postValue("账号、密码不能为空")
            return
        }
        viewModelScope.launch {
            model.registeredAccount(username,
                password,
                onSuccess = {
                    _result.postValue(it)
                    UsernameUtils.saveUsername(it)
                }
                ,
                onError = {_error.postValue(it)}
            )
        }
    }

    fun doLogin(username: String,password: String){
        if (username.isEmpty() || password.isEmpty()){
            _error.postValue("账号、密码不能为空")
            return
        }
        viewModelScope.launch {
            model.verifyPassword(username,password,
                onSuccess = {
                    _result.postValue(it)
                    UsernameUtils.saveUsername(it)
                },
                onError = {
                    _error.postValue(it)
                }
            )
        }
    }

}