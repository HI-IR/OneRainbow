package com.onerainbow.lib.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.onerainbow.lib.database.dao.LoginDao
import com.onerainbow.lib.database.entity.LoginUser

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/14 20:54
 */
@Database(
    entities = [LoginUser::class],
    version = 1,
    exportSchema = false)
abstract class LoginDatabase:RoomDatabase(){
    abstract fun LoginDao(): LoginDao

}