package com.onerainbow.lib.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * description ： 用户表
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/22 17:17
 */
@Entity(
    tableName = "user_table"
)
data class UserEntity(

    @PrimaryKey
    @ColumnInfo(name = "username") val username : String,

    @ColumnInfo(name = "password") val password : String,

    @ColumnInfo(name = "avatarRes") val avatarRes : String


)
