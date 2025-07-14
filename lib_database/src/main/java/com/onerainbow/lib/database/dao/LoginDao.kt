package com.onerainbow.lib.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.onerainbow.lib.database.entity.LoginUser

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/14 21:02
 */
@Dao
interface LoginDao {
    @Insert
    suspend fun insertUser(user: LoginUser):Long
    @Update
    suspend fun updateUser(user: LoginUser)
    @Delete
    suspend fun deleteUser(user: LoginUser)
    @Query("SELECT * FROM user_table")
    suspend fun getAllUsers():List<LoginUser>
    @Query("SELECT * FROM user_table WHERE id = :id")
    suspend fun getUserById(id:Int): LoginUser?
}