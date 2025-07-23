package com.onerainbow.lib.base.utils

import android.content.Context
import com.onerainbow.lib.base.BaseApplication

/**
 * description ： 用本地缓存Cookie
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/15 10:03
 */
object UsernameUtils {
    //SharedPreferences的名字
    private const val PREF_NAME = "username_prefs"

    //保存username的KEY
    private const val KEY_USERNAME = "username_value"

    fun saveUsername(username: String) {
        val prefs = BaseApplication.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USERNAME, username).apply()
    }

    fun getUsername(): String? {
        val prefs = BaseApplication.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USERNAME, null)
    }

    fun clearUsername() {
        val prefs = BaseApplication.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_USERNAME).apply()
    }
}