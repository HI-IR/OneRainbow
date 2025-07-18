package com.onerainbow.module.musicplayer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.onerainbow.module.musicplayer.R
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.model.toMediaItem
import com.onerainbow.module.musicplayer.ui.MusicPlayerActivity

/**
 * description ： 音乐服务
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/16 16:20
 */
class MusicService : Service() {
    // 播放模式枚举
    enum class PlayMode {
        SINGLE_LOOP,  // 单曲循环
        SEQUENTIAL    // 顺序播放
    }

    private var playMode = PlayMode.SEQUENTIAL
    private val playlist:MutableList<Song> = mutableListOf()
    private lateinit var player: ExoPlayer
    private val mBinder = MusicBinder()
    private val CHANNEL_ID = "MUSIC_CHANNEL_ID"
    private val APP_NAME = "OneRainBow"


    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this)
            .build()
            .also { it.repeatMode = Player.REPEAT_MODE_OFF }


        player.addListener(object : Player.Listener {
            //处理错误逻辑
            override fun onPlayerError(error: PlaybackException) {
                // 通知 UI 播放出错
                MusicManager.notifyPlayError(true)
                MusicManager.notifyPlayState(false)

                // 尝试自动切换到下一首（仅顺序播放模式）
                if (playMode == PlayMode.SEQUENTIAL && player.hasNextMediaItem()) {
                    switchToNextSong()
                } else {
                    // 单曲循环或没有下一首，停止播放
                    updateNotification("播放出错：${player.mediaMetadata.title}")
                }
            }


            override fun onPlaybackStateChanged(state: Int) {
                // 当播放完成时，根据模式处理下一个动作
                if (state == Player.STATE_ENDED) {
                    handlePlaybackCompletion()
                }
            }

            // 处理播放完成逻辑
            private fun handlePlaybackCompletion() {
                when (playMode) {
                    PlayMode.SINGLE_LOOP -> {
                        // 单曲循环：重新播放当前歌曲
                        player.seekTo(0)
                        player.play()
                        MusicManager.notifyPlayState(true)
                        updateNotification("单曲循环：${player.mediaMetadata.title}")
                    }
                    PlayMode.SEQUENTIAL -> {
                        // 顺序播放：如果有下一首则播放，否则停止
                        if (player.hasNextMediaItem()) {
                            switchToNextSong()
                        } else {
                            MusicManager.notifyPlayState(false)
                            updateNotification("顺序播放已结束")
                        }
                    }
                }
            }

            // 切换到下一首歌曲
            private fun switchToNextSong() {
                player.seekToNext()
                player.prepare()
                player.play()
                MusicManager.notifyPlayError(false)
                MusicManager.notifyPlayIndex(player.currentMediaItemIndex)
                MusicManager.notifyPlayState(true)
                updateNotification("自动播放：${player.mediaMetadata.title}")
            }
        })


        createNotificationChannel()

        //将当前服务修改为前台服务
        startForeground(1, createNotification("正在加载中"))

    }

    //通知栏配置
    private fun createNotification(text: String): Notification {

        //配置点击Intent
        val pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MusicPlayerActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("OneRainBow音乐播放器")
            .setContentText(text)
            .setSmallIcon(R.drawable.temp)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .build()
    }

    //构建通知渠道
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //生成一个通知渠道
            val channel = NotificationChannel(
                CHANNEL_ID, "OneRainbow音乐播放", NotificationManager.IMPORTANCE_LOW
            )

            //获取通知管理器
            val manager = getSystemService(NotificationManager::class.java)

            //创建通知渠道
            manager.createNotificationChannel(channel)
        }
    }

    //更新通知
    private fun updateNotification(text: String) {
        val notification = createNotification(text)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder = mBinder


    override fun onDestroy() {
        player.release()//释放player，避免内存泄漏
        super.onDestroy()
    }


    //用于和前台服务通信的Binder,实现基础功能
    inner class MusicBinder : Binder() {

        //播放相关
        /**
         * 播放，单首歌曲
         */
        fun play(song: Song) {
            val mediaItem = song.toMediaItem()
            player.setMediaItem(mediaItem)
            player.prepare()//准备播放器
            player.play()//播放
            playlist.clear()
            playlist.add(song)
            updateNotification("正在播放：${song.name}")
        }

        /**
         * 暂停播放
         */
        fun pause() {
            player.pause()//暂停播放
            updateNotification("暂停播放：${player.currentMediaItem?.mediaMetadata?.title}")
        }

        /**
         * 继续播放歌曲
         */
        fun resume() {
            player.play()
            updateNotification("继续播放:${player.currentMediaItem?.mediaMetadata?.title}")
        }


        //播放列表相关
        /**
         * 添加歌单到播放列表
         */
        fun addSongs(songs: List<Song>, startIndex: Int = 0) {
            val mediaItems = songs.map { it.toMediaItem() }
            player.setMediaItems(mediaItems, startIndex, 0L)
            player.prepare()
            player.play()
            playlist.addAll(songs)
        }

        /**
         * 添加歌曲到播放列表
         */
        fun addSong( song: Song) {
            val mediaItems = song.toMediaItem()
            player.addMediaItem(mediaItems)
            playlist.add(song)
        }

        /**
         * 删除某一首歌（通过 index）
         */
        fun removeSongAt(index: Int) {
            if (index in 0 until player.mediaItemCount) {
                player.removeMediaItem(index)
                playlist.removeAt(index)
            }
        }

        /**
         * 清空播放列表
         */
        fun clearPlaylist() {
            player.clearMediaItems()
            playlist.clear()
        }

        /**
         * 跳转到指定索引播放
         */
        fun playAt(index: Int) {
            if (index in 0 until player.mediaItemCount) {
                player.seekTo(index, 0)
                MusicManager.notifyPlayIndex(index)
                updateNotification("正在播放：${player.getMediaItemAt(index).mediaMetadata.title}")
            }
        }

        /**
         * 获取播放列表
         */
        fun getSongPlaylist(): List<Song> = playlist

        /**
         * 获取播放列表总数
         */
        fun getPlaylistSize(): Int = player.mediaItemCount


        //状态相关
        /**
         * 获取播放状态
         */
        fun isPlaying(): Boolean = player.isPlaying

        /**
         * 获取当前播放索引
         */
        fun getCurrentIndex(): Int = player.currentMediaItemIndex


        /**
         * 播放下一首歌
         * @return true 切换成功，false切换失败
         */
        fun playNext():Boolean {
            val currentIndex = getCurrentIndex()
            //检查边界
            return if (currentIndex < getPlaylistSize() - 1){
                playAt(currentIndex +1)
                true
            }else false
        }

        /**
         * 播放上一首歌
         * @return true 切换成功，false切换失败
         */
        fun playPrev():Boolean {
            val currentIndex = getCurrentIndex()
            return if (currentIndex > 0){
                val prevIndex = (currentIndex - 1)
                playAt(prevIndex)
                true
            }else false
        }

        /**
         * 切换音乐播放和暂替
         */
        fun togglePlayPause() {
            if (player.isPlaying) {
                player.pause()
            } else {
                if (player.currentMediaItem == null && playlist.isNotEmpty()) {
                    player.setMediaItems(playlist.map { it.toMediaItem() }, 0, 0L)
                    player.prepare()
                }
                player.play()
            }
            MusicManager.notifyPlayState(player.isPlaying)
            updateNotification("正在${if (player.isPlaying) "播放" else "暂停"}：${player.mediaMetadata.title}")
        }




        fun getCurrentPosition():Long = player.currentPosition

        fun getDuration():Long = player.duration

        // 模式切换
        fun setPlayMode(mode: PlayMode) {
            playMode = mode
            // 确保内置循环模式关闭，使用我们自己的逻辑
            player.repeatMode = Player.REPEAT_MODE_OFF
            updateNotification("播放模式：${if (mode == PlayMode.SINGLE_LOOP) "单曲循环" else "顺序播放"}")
        }

        fun seekTo(position: Long){
            MusicManager.notifyPlayState(true)
            player.seekTo(position)
        }


    }
}