package com.onerainbow.module.musicplayer.utils

import android.content.Context
import android.content.Intent
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.service.MusicService

/**
 * description ： 音乐播放器的广播工具
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/18 11:35
 */
object ReceiverUtils {

    /**
     * 向MusicService添加单首歌曲
     * @param context 上下文（如Activity/Fragment）
     * @param song 待添加的歌曲（非空）
     */
    fun addSongToService(context: Context, song: Song) {
        // 参数校验：避免传递空歌曲
        if (song.id <= 0 || song.name.isBlank()) {
            throw IllegalArgumentException("传递的歌曲数据不完整，请检查Song对象")
        }

        // 构建广播Intent
        val intent = Intent(MusicService.ACTION_ADD_SONG).apply {
            putExtra(MusicService.EXTRA_SONG, song)
        }

        context.sendBroadcast(intent)
    }

    /**
     * 向MusicService添加多首歌曲
     * @param context 上下文（如Activity/Fragment）
     * @param songs 待添加的歌曲列表（非空且至少有一首歌曲）
     */
    fun addSongsToService(context: Context, songs: List<Song>) {
        // 参数校验：避免传递空列表或空歌曲
        if (songs.isEmpty()) {
            throw IllegalArgumentException("歌曲列表不能为空")
        }
        songs.forEach { song ->
            if (song.id <= 0 || song.name.isBlank()) {
                throw IllegalArgumentException("列表中存在不完整的歌曲数据：${song.name}")
            }
        }

        // 构建局部广播Intent
        val intent = Intent(MusicService.ACTION_ADD_SONGS).apply {

            putParcelableArrayListExtra(
                MusicService.EXTRA_SONGS,
                ArrayList(songs)
            )
        }
        // 发送广播
        context.sendBroadcast(intent)
    }
}