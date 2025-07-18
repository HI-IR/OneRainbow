package com.onerainbow.lib.base.utils

import android.content.Context
import com.onerainbow.lib.base.BaseApplication

/**
 * description ： 存储工具 的一层简单封装(SharedPreferences)
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/17 12:02
 */
object StoreUtils {

    /**
     * 关于播放器缓存数据
     */
    const val PLAYER_DATA = "player_prefs"
    const val KEY_PLAYER_MODE = "player_mode"

    fun saveBoolean(prefName: String, key: String, value: Boolean) {
        BaseApplication.context
            .getSharedPreferences(prefName, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(key, value)
            .apply()
    }

    fun getBoolean(prefName: String, key: String):Boolean{
        return BaseApplication.context.getSharedPreferences(prefName,Context.MODE_PRIVATE)
            .getBoolean(key,false)
    }


}