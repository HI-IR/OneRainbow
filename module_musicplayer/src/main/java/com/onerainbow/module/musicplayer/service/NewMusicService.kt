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
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.onerainbow.module.musicplayer.R
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.model.toMediaMetadata
import com.onerainbow.module.musicplayer.repository.SongModel
import com.onerainbow.module.musicplayer.ui.MusicPlayerActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * description ： 重构音乐播放服务
 * 因为歌曲URL会过期，所以新版音乐服务播放前先进行网络访问获取最新URL ，再播放
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/18 20:44
 */
class NewMusicService : Service() {
    // 播放模式枚举
    enum class PlayMode {
        SINGLE_LOOP,  // 单曲循环
        SEQUENTIAL    // 顺序播放
    }


    //记录当前播放的 playlist 索引
    private var currentIndex = -1
    private var playMode = PlayMode.SEQUENTIAL
    private val playlist: MutableList<Song> = mutableListOf()  // 保留原有媒体数据（非URL部分）
    private lateinit var player: ExoPlayer
    private val mBinder = MusicBinder()
    private val CHANNEL_ID = "MUSIC_CHANNEL_ID"
    private val APP_NAME = "OneRainBow"

    // 管理网络请求订阅，避免内存泄漏
    private val compositeDisposable = CompositeDisposable()
    private var currentUrlRequest: Disposable? = null  // 当前正在进行的URL请求


    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this)
            .build()
            .also { it.repeatMode = Player.REPEAT_MODE_OFF }  // 禁用内置循环，使用自定义逻辑

        //注册Player事件的监听
        setupPlayerListener()
        //创建通知渠道
        createNotificationChannel()
        //开启前台服务
        startForeground(1, createNotification("准备就绪"))
    }

    // 设置播放器监听器（处理播放完成、错误等事件）
    private fun setupPlayerListener() {
        player.addListener(object : Player.Listener {
            // 处理播放错误（如URL无效、播放失败）
            override fun onPlayerError(error: PlaybackException) {
                Log.e("MusicService", "播放错误: ${error.message}", error)
                MusicManager.notifyPlayError(true)
                MusicManager.notifyPlayState(false)
                updateNotification("播放出错：${player.mediaMetadata.title}")

                // 顺序播放模式下自动切换到下一首
                if (playMode == PlayMode.SEQUENTIAL && player.hasNextMediaItem()) {
                    switchToNextSong()
                }
            }

            // 处理播放完成（播放到末尾）
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    handlePlaybackCompletion()
                }
            }
        })
    }

    // 处理播放完成逻辑（根据模式决定循环或下一首）
    private fun handlePlaybackCompletion() {
        when (playMode) {
            PlayMode.SINGLE_LOOP -> {
                // 单曲循环：播放当前索引的歌曲
                if (currentIndex != -1) mBinder.playAt(currentIndex)
            }

            PlayMode.SEQUENTIAL -> {
                if (!mBinder.playNext()) {
                    MusicManager.notifyPlayState(false)
                    updateNotification("播放已结束")
                }
            }
        }
    }

    // 切换到下一首歌曲（强制请求最新URL）
    private fun switchToNextSong() {
        val nextIndex = player.currentMediaItemIndex + 1
        if (nextIndex < playlist.size) {
            mBinder.playAt(nextIndex)  // 调用playAt触发URL请求
        }
    }

    /**
     * 给了根据ID重新查询Url播放，避免Url刷新
     */
    private fun playWithFreshUrl(song: Song) {
        // 取消当前正在进行的URL请求（避免重复请求）
        currentUrlRequest?.let { compositeDisposable.remove(it) }

        // 更新状态：正在获取资源
        updateNotification("正在获取歌曲资源：${song.name}")
        MusicManager.notifyPlayState(false)
        MusicManager.notifyPlayError(false)

        // 发起URL请求（使用SongModel的网络接口）
        currentUrlRequest = SongModel.getSongById(song.id)
            .subscribe(
                { result ->  // 请求成功回调
                    if (result.startsWith("发生错误")) {
                        // URL请求失败（如网络错误、数据为空）
                        handleUrlError(song, result)
                    } else {
                        // URL请求成功：使用最新URL播放
                        handleUrlSuccess(song, result)
                    }
                },
                { error ->  // 请求异常（如RxJava订阅错误）
                    handleUrlError(song, "网络请求异常：${error.message}")
                }
            )

        // 管理订阅生命周期
        currentUrlRequest?.let { compositeDisposable.add(it) }
    }

    // URL请求成功：初始化播放器并播放
    private fun handleUrlSuccess(song: Song, freshUrl: String) {
        try {
            // 创建媒体项（使用最新URL，保留原有媒体元数据）
            val mediaItem = MediaItem.Builder()
                .setUri(freshUrl)  // 核心：使用最新请求的URL
                .setMediaMetadata(song.toMediaMetadata())  // 复用原有媒体元数据（歌名、封面等）
                .build()

            // 更新播放器状态
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()

            // 更新通知和UI状态
            updateNotification("正在播放：${song.name}")
            MusicManager.notifyPlayState(true)
            MusicManager.notifyPlayIndex(currentIndex)   // 通知当前播放索引
        } catch (e: Exception) {
            handleUrlError(song, "播放器初始化失败：${e.message}")
        }
    }

    // URL请求失败：处理错误逻辑
    private fun handleUrlError(song: Song, errorMsg: String) {
        Log.e("MusicService", "歌曲${song.name}URL获取失败：$errorMsg")
        updateNotification("获取资源失败：${song.name}")
        MusicManager.notifyPlayError(true)
        MusicManager.notifyPlayState(false)

        // 顺序播放模式下自动切换到下一首
        if (playMode == PlayMode.SEQUENTIAL) {
            val currentIndex = playlist.indexOf(song)
            if (currentIndex + 1 < playlist.size) {
                switchToNextSong()  // 尝试播放下一首
            }
        }
    }

    // 获取当前播放的歌曲
    private fun getCurrentSong(): Song? {
        val currentIndex = player.currentMediaItemIndex
        return if (currentIndex in playlist.indices) playlist[currentIndex] else null
    }


    // 通知栏相关方法（保持不变）
    private fun createNotification(text: String): Notification {
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


    // Binder类：对外提供的播放控制接口
    inner class MusicBinder : Binder() {
        // 播放单首歌曲（强制请求最新URL）
        fun play(song: Song) {
            playlist.clear()
            playlist.add(song)
            currentIndex = 0 // 重置索引为 0
            playWithFreshUrl(song)  // 强制请求URL后播放
        }

        // 添加歌单并从指定索引播放（强制请求该索引歌曲的URL）
        fun addSongs(songs: List<Song>, startIndex: Int = 0) {
            playlist.addAll(songs)
            if (startIndex in songs.indices) {
                playAt(startIndex)  // 从指定索引播放（触发URL请求）
            }
        }

        // 播放指定索引的歌曲（强制请求最新URL）
        fun playAt(index: Int) {
            if (index in playlist.indices) {
                currentIndex = index // 显式更新当前索引
                val targetSong = playlist[index]
                playWithFreshUrl(targetSong)  // 强制请求URL后播放
            }
        }

        // 切换到下一首（强制请求下一首的URL）
        fun playNext(): Boolean {
            if (currentIndex + 1 < playlist.size) {
                currentIndex++ // 索引+1
                playAt(currentIndex)
                return true
            }
            return false
        }

        // 切换到上一首（强制请求上一首的URL）
        fun playPrev(): Boolean {
            if (currentIndex > 0) {
                currentIndex-- // 索引-1
                playAt(currentIndex)
                return true
            }
            return false
        }

        // 暂停播放
        fun pause() {
            player.pause()
            updateNotification("暂停播放：${player.mediaMetadata.title}")
            MusicManager.notifyPlayState(false)
        }

        // 继续播放（无需重新请求URL，直接恢复播放）
        fun resume() {
            player.play()
            updateNotification("继续播放：${player.mediaMetadata.title}")
            MusicManager.notifyPlayState(true)
        }

        // 切换播放/暂停
        fun togglePlayPause() {
            if (player.isPlaying) {
                pause()
            } else {
                if (player.currentMediaItem == null && playlist.isNotEmpty()) {
                    playAt(0)  // 无当前媒体项时从头播放（触发URL请求）
                } else {
                    resume()
                }
            }
        }

        // 其他原有方法（保持不变，仅调整播放相关逻辑）
        fun addSong(song: Song) {
            playlist.add(song)
        }

        fun removeSongAt(index: Int) {
            if (index in playlist.indices) {
                playlist.removeAt(index)
                player.removeMediaItem(index)
            }
        }

        fun clearPlaylist() {
            playlist.clear()
            player.clearMediaItems()
        }

        fun getSongPlaylist(): List<Song> = playlist.toList()  // 返回不可变列表

        fun getCurrentIndex(): Int = currentIndex.coerceAtLeast(-1)

        fun isPlaying(): Boolean = player.isPlaying

        fun setPlayMode(mode: PlayMode) {
            playMode = mode
            updateNotification("播放模式：${if (mode == PlayMode.SINGLE_LOOP) "单曲循环" else "顺序播放"}")
        }

        fun getCurrentPosition(): Long = player.currentPosition

        fun getDuration(): Long = player.duration

        fun seekTo(position: Long) {
            player.seekTo(position)
            MusicManager.notifyPlayState(true)
        }
    }


    // 资源释放：取消订阅和播放器
    override fun onDestroy() {
        compositeDisposable.clear()  // 清除所有网络请求订阅
        player.release()  // 释放播放器资源
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder = mBinder
}