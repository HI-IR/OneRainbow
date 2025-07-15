package com.onerainbow.lib.base.utils

import android.content.Context
import com.onerainbow.lib.base.BaseApplication

/**
 * description ： 用本地缓存Cookie
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/15 10:03
 */
object CookieUtils {
    //SharedPreferences的名字
    private const val PREF_NAME = "cookie_prefs"

    //CookieE的KEY
    private const val KEY_COOKIE = "cookie_value"

    fun saveCookie(cookie: String) {
        val prefs = BaseApplication.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_COOKIE, cookie).apply()
    }

    fun getCookie(): String? {
        val prefs = BaseApplication.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_COOKIE, null)
    }

    fun clearCookie() {
        val prefs = BaseApplication.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_COOKIE).apply()
    }
}