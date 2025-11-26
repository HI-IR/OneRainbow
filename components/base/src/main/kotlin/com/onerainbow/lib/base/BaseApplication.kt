package com.onerainbow.lib.base

import android.app.Application
import com.therouter.TheRouter

/**
 * description ： 应用级别的上下文
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/15 01:08
 */
open class BaseApplication: Application(){
    companion object {
        lateinit var context: BaseApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        TheRouter.isDebug = true
        //注入一下
        TheRouter.init(this)

    }
}