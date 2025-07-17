package com.onerainbow.module.musicplayer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import com.onerainbow.module.musicplayer.R
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.ui.MusicPlayerActivity

/**
 * description ： 音乐服务
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/16 16:20
 */
class MusicService : Service(){
    private lateinit var player:ExoPlayer
    private val mBinder = MusicBinder()
    private val CHANNEL_ID = "MUSIC_CHANNEL_ID"
    private val APP_NAME = "OneRainBow"
    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()//创建Exoplayer播放器

        createNotificationChannel()

        //将当前服务修改为前台服务
        startForeground(1,createNotification("正在加载中"))
    }

    //通知栏配置
    private fun createNotification(text: String): Notification{

        //配置点击Intent
        val pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MusicPlayerActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this,CHANNEL_ID)
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

    override fun onBind(intent: Intent?): IBinder? = mBinder


    override fun onDestroy() {
        player.release()//释放player，避免内存泄漏
        super.onDestroy()
    }

    //用于和前台服务通信的Binder
    inner class MusicBinder: Binder() {

        /**
         * 播放
         */
        fun play(song: Song ){
            //构建媒体元数据
            val mediaMetadata = MediaMetadata.Builder()
                .setTitle(song.name)//歌名
                .setArtworkUri(Uri.parse(song.coverUrl))//封面
                .build()

            //构建媒体项，设置媒体元数据
            val mediaItem = MediaItem.Builder().setUri(song.url).setMediaMetadata(mediaMetadata).build()

            player.setMediaItem(mediaItem)
            player.prepare()//准备播放器
            player.play()//播放
            updateNotification("正在播放：${song.name}")
        }


        /**
         * 暂停播放
         */
        fun pause(){
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

        fun isPlaying(): Boolean = player.isPlaying
    }
}