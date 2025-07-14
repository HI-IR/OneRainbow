package com.onerainbow.lib.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/14 21:00
 */
@Entity(
    tableName = "user_table"
)
  data class LoginUser(
      @PrimaryKey(autoGenerate = true)
      val id:Int=0,
      val name:String,
      val password:String
  )
