package com.onerainbow.lib.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.onerainbow.lib.base.BaseApplication
import com.onerainbow.lib.database.dao.CollectDao
import com.onerainbow.lib.database.dao.RecentPlayDao
import com.onerainbow.lib.database.dao.UserDao
import com.onerainbow.lib.database.entity.CollectEntity
import com.onerainbow.lib.database.entity.RecentPlayedEntity
import com.onerainbow.lib.database.entity.UserEntity

/**
 * description ： 数据库
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 21:09
 */
@Database(
    entities = [RecentPlayedEntity::class, UserEntity::class, CollectEntity::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class OneRainbowDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recentPlayDao(): RecentPlayDao
    abstract fun collectDao(): CollectDao

    companion object {
        private var instance: OneRainbowDatabase? = null

        @Synchronized
        fun getDatabase(): OneRainbowDatabase {
            instance?.let {
                return it
            } ?: return Room.databaseBuilder(
                BaseApplication.context,
                OneRainbowDatabase::class.java,
                "onerainbow_database"
            )
                .build().apply {
                    instance = this
                }
        }
    }
}