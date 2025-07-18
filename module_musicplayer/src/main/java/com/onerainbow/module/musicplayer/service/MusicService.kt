package com.onerainbow.module.musicplayer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
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

    companion object {
        const val ACTION_ADD_SONG = "com.onerainbow.ADD_SONG"//添加单个歌曲
        const val ACTION_ADD_SONGS = "com.onerainbow.ADD_SONGS"//添加歌单
        const val EXTRA_SONG = "song"//添加歌曲的Key
        const val EXTRA_SONGS = "songs"//添加歌单的Key
    }
    private val playlist:MutableList<Song> = mutableListOf()
    private lateinit var player: ExoPlayer
    private val mBinder = MusicBinder()
    private val CHANNEL_ID = "MUSIC_CHANNEL_ID"
    private val APP_NAME = "OneRainBow"


    //用广播接收器来接收天降音乐的广播
    private val songReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_ADD_SONG -> {
                    val song = intent.getParcelableExtra<Song>(EXTRA_SONG)
                    song?.let { addSongToPlaylist(it) }
                }
                ACTION_ADD_SONGS -> {
                    val songs = intent.getParcelableArrayListExtra<Song>(EXTRA_SONGS)
                    songs?.let { addSongsToPlaylist(it) }
                }
            }
        }
    }



    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()//创建Exoplayer播放器

        createNotificationChannel()

        //将当前服务修改为前台服务
        startForeground(1, createNotification("正在加载中"))

        // 注册广播接收器
        val filter = IntentFilter().apply {
            addAction(ACTION_ADD_SONG)
            addAction(ACTION_ADD_SONGS)
        }
        registerReceiver(songReceiver, filter)
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
            .setSmallIcon(R.drawable.temp)   //配置通知图标待确定软件图标 TODO
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
        unregisterReceiver(songReceiver) // 注销接收器
        super.onDestroy()
    }

    private fun addSongToPlaylist(song: Song) {
        mBinder.addSong(song)
        updateNotification("已添加: ${song.name}")
    }

    private fun addSongsToPlaylist(songs: List<Song>) {
        mBinder.play(songs, playlist.size) // 添加到当前播放列表末尾
        updateNotification("已添加 ${songs.size} 首歌曲")
    }



    //用于和前台服务通信的Binder
    inner class MusicBinder : Binder() {

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
         * 添加播放列表
         */
        fun play(songs: List<Song>, startIndex: Int = 0) {
            val mediaItems = songs.map { it.toMediaItem() }
            player.setMediaItems(mediaItems, startIndex, 0L)
            player.prepare()
            player.play()
            playlist.addAll(songs)
            updateNotification("正在播放：${songs[startIndex].name}")
        }

        /**
         * 添加歌曲（追加到末尾）
         */
        fun addSong(vararg songs: Song) {
            val mediaItems = songs.map { it.toMediaItem() }
            playlist.addAll(songs)
            player.addMediaItems(mediaItems)
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
                player.play()
                updateNotification("正在播放：${player.getMediaItemAt(index).mediaMetadata.title}")
            }
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

        /**
         * 切换音乐播放和暂替
         */
        fun togglePlayPause() {
            when {
                // 播放器未加载歌曲，但我们有播放列表
                player.currentMediaItem == null && playlist.isNotEmpty() -> {
                    val mediaItems = playlist.map { it.toMediaItem() }
                    player.setMediaItems(mediaItems, 0, 0L)
                    player.prepare()
                    player.play()
                    updateNotification("正在播放：${playlist[0].name}")
                }

                // 播放结束，从头开始
                player.playbackState == Player.STATE_ENDED -> {
                    player.seekTo(0)
                    player.play()
                    updateNotification("重新播放：${player.currentMediaItem?.mediaMetadata?.title}")
                }

                // 正在播放 -> 暂停
                player.isPlaying -> {
                    player.pause()
                    updateNotification("暂停播放：${player.currentMediaItem?.mediaMetadata?.title}")
                }

                // 未播放但准备好了 -> 恢复播放
                player.playbackState == Player.STATE_READY -> {
                    player.play()
                    updateNotification("继续播放：${player.currentMediaItem?.mediaMetadata?.title}")
                }

                else -> {
                    // 其他情况(空歌单)
                    updateNotification("无法播放，请先添加歌曲")
                }
            }
        }



        /**
         * 获取播放列表
         */
        fun getSongPlaylist(): List<Song> = playlist


        /**
         * 获取播放状态
         */
        fun isPlaying(): Boolean = player.isPlaying

        /**
         * 获取当前播放索引
         */
        fun getCurrentIndex(): Int = player.currentMediaItemIndex

        /**
         * 获取播放列表总数
         */
        fun getPlaylistSize(): Int = player.mediaItemCount
    }
}