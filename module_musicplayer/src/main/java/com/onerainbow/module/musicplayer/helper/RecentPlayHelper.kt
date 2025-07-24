package com.onerainbow.module.musicplayer.helper

import com.google.gson.Gson
import com.onerainbow.lib.base.utils.UsernameUtils
import com.onerainbow.lib.database.OneRainbowDatabase
import com.onerainbow.lib.database.entity.RecentPlayedEntity
import com.onerainbow.module.musicplayer.domain.Song
import com.onerainbow.module.musicplayer.domain.toArtistLite
import com.onerainbow.module.musicplayer.domain.toSong

/**
 * description ： 一个用于添加最近播放的帮助类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/22 20:22
 */
object RecentPlayHelper {

    private val dao by lazy {
        OneRainbowDatabase.getDatabase().recentPlayDao()
    }
    val gson by lazy {
        Gson()
    }

    /**
     * 添加歌单到最近播放
     */
    suspend fun addPlaylistToRecent(songs: List<Song>) {
        val now = System.currentTimeMillis()
        val username = UsernameUtils.getUsername()
        if (username.isNullOrBlank()) return

        if (songs.isEmpty()) return

        dao.insertPlaylist(songs.mapIndexed() { index, it ->
            RecentPlayedEntity(
                username = username,
                songId = it.id,
                songName = it.name,
                coverUrl = it.coverUrl,
                artistsJson = gson.toJson(it.artists.map { artist ->
                    artist.toArtistLite()
                }),
                currentTime = now + index
            )
        })
    }

    /**
     * 添加歌曲到最近播放
     */
    suspend fun addSongToRecent(song: Song) {
        val now = System.currentTimeMillis()
        val username = UsernameUtils.getUsername()
        if (username.isNullOrBlank()) return

        dao.insertSong(
            RecentPlayedEntity(
                username = username,
                songId = song.id,
                songName = song.name,
                coverUrl = song.coverUrl,
                artistsJson = gson.toJson(song.artists.map { artist ->
                    artist.toArtistLite()
                }),
                currentTime = now
            )
        )
    }

    //清空历史记录
    suspend fun clearHistory() {
        val username = UsernameUtils.getUsername()
        if (username.isNullOrBlank()) return
        dao.clearHistory(username)
    }

    /**
     * 获取历史记录
     */
    suspend fun getHistory(): List<Song>? {
        val username = UsernameUtils.getUsername()
        if (username.isNullOrBlank()) return null
        return dao.getRecentPlayList(username)?.map { it.toSong() }
    }
}