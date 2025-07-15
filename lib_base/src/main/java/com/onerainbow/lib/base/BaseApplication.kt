package com.onerainbow.lib.base

import android.app.Application

/**
 * description ： 应用级别的上下文
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/15 01:08
 */
class BaseApplication: Application(){
    companion object {
        lateinit var context: BaseApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}