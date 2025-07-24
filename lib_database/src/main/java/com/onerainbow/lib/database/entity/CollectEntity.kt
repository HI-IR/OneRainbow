package com.onerainbow.lib.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * description ： 我的收藏的Entity
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/23 20:48
 */
@Entity(
    tableName = "collect_table",
    primaryKeys = ["username","songId"]
)
data class CollectEntity (
    @ColumnInfo(name = "username") val username : String,
    @ColumnInfo(name = "songId") val songId : Long,
    @ColumnInfo(name = "songName") val songName : String,
    @ColumnInfo(name = "coverUrl") val coverUrl : String,
    @ColumnInfo(name = "artistsJson") val artistsJson: String,//将数组的Artist数据转化为Json存储
    @ColumnInfo(name = "currentTime") val currentTime: Long = System.currentTimeMillis()
)