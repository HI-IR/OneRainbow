package com.onerainbow.lib.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onerainbow.lib.database.entity.UserEntity

/**
 * description ： 用户表的Dao层
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/22 19:52
 */
@Dao
interface UserDao {

    /**
     * 插入用户数据
     * @return -1表示冲突
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(userEntity: UserEntity): Long

    /**
     * 获取密码
     */
    @Query("select password from user_table where username = :username")
    suspend fun getPassword(username: String): String?

    /**
     * 通过username获取用户信息
     */
    @Query("select * from user_table where username = :username")
    suspend fun getUserInfoByUsername(username: String): UserEntity?

    @Query("select avatarRes from user_table where username = :username")
    suspend fun getAvatarResByUsername(username: String): String?
}