package com.onerainbow.lib.database

import android.content.Context
import androidx.room.Room

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/14 21:09
 */
object LoginUserDatabaseClient {
    @Volatile
    private var INSTANCE: LoginDatabase? = null

    fun getDatabase(context: Context): LoginDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                LoginDatabase::class.java,
                "my_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
