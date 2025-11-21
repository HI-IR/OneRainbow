package com.onerainbow.onerainbow

import android.content.Context
import com.onerainbow.lib.base.BaseApplication
import com.onerainbow.module.musicplayer.service.MusicManager

/**
 * description ： 主应用的Application
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/18 13:05
 */
class AppApplication: BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        // 绑定音乐服务，启动时即保持 Service 连接
        MusicManager.bindService(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }
}