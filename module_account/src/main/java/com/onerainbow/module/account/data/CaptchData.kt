package com.onerainbow.module.account.data

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/13 19:43
 */
 data class CaptchaData (
     val code:String,
     val data:Boolean
)
sealed class CaptchaDataResult<out T>
{
    data class Success<out T>(val data:T):CaptchaDataResult<T>()
    data class Error(val exception: Exception):CaptchaDataResult<Nothing>()
}
data class LoginCaptchaData(
    val code:String,
    val data:Boolean,
    val message:String
)
sealed class LoginCaptchaDataResult<out T>
{
    data class Success<out T>(val data:T):LoginCaptchaDataResult<T>()
    data class Error(val exception: Exception):LoginCaptchaDataResult<Nothing>()
}