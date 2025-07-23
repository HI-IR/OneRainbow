package com.onerainbow.lib.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onerainbow.lib.database.entity.RecentPlayedEntity

/**
 * description ：RecentPlay的Dao
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/22 19:41
 */
@Dao
interface RecentPlayDao {

    /**
     * 插入歌曲数据
     * 使用取代策略
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(recent: RecentPlayedEntity)

    /**
     * 批量加入
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(songList: List<RecentPlayedEntity>)

    /**
     * 获取最近听歌数据
     */
    @Query("select * from recent_played_table where username = :username order by currentTime desc ")
    suspend fun getRecentPlayList(username: String): List<RecentPlayedEntity>


    /**
     * 清除最近记录
     */
    @Query("delete from recent_played_table where username = :username")
    suspend fun clearHistory(username: String)

}