package com.onerainbow.lib.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onerainbow.lib.database.entity.CollectEntity

/**
 * description ： 收藏的Dao层
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/23 21:03
 */
@Dao
interface CollectDao {

    /**
     * 收藏歌曲
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun collectSong(song: CollectEntity)

    /**
     * 取消收藏
     */
    @Query("delete from collect_table where username = :username and songId = :songId")
    suspend fun uncollectSong(username: String, songId: Long)

    /**
     * 查询是否有收藏
     * 如果大于0，则返回已收藏
     */
    @Query("select count(*) from collect_table where username = :username and songId = :songId")
    suspend fun hasCollectSongBySongId(username: String, songId: Long):Long


    /**
     * 查询所有我喜欢的
     */
    @Query("select * from collect_table where username =:username order by currentTime DESC")
    suspend fun getCollectionList(username: String):List<CollectEntity>?

    /**
     * 清除所有我喜欢的
     */
    @Query("delete from collect_table where username = :username")
    suspend fun clearCollection(username: String)

    /**
     * 最近收藏的一首歌
     */
    @Query("select * from collect_table where username = :username order by currentTime DESC limit 1")
    suspend fun getLastCollection(username: String):CollectEntity?

    /**
     * 查询收藏的数量
     */
    @Query("select count(*) from collect_table where username = :username")
    suspend fun getCollectCount(username: String):Long
}