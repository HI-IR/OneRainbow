package com.onerainbow.module.home.model

import com.onerainbow.lib.database.OneRainbowDatabase

/**
 * description ： Home模块的Model
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/27 11:01
 */
class HomeModel {
    private val userDao by lazy{
        OneRainbowDatabase.getDatabase().userDao()
    }

    suspend fun updateAvatarResByUsername(username: String , avatarRes:String){
        userDao.updateAvatarRes(username,avatarRes)
    }
}