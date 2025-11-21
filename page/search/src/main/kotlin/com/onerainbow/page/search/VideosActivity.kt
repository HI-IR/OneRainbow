package com.onerainbow.page.search

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.base.utils.CopyUtils
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.page.share.CustomShare
import com.onerainbow.page.share.utils.ShareUtils
import com.onerainbow.page.search.adapter.CommentAdapterMv
import com.onerainbow.page.search.databinding.ActivityVideosBinding
import com.onerainbow.page.search.databinding.LayoutBottomSheetCommentsBinding
import com.onerainbow.page.search.viewmodel.VideoViewModel
import com.therouter.router.Autowired
import com.therouter.router.Route
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Route(path = RoutePath.MVS)
class VideosActivity : BaseActivity<ActivityVideosBinding>() {

    // 路由参数
    @Autowired(name = "id")
    var id: Long? = null

    @Autowired(name = "name")
    lateinit var name: String

    // 播放器
    private lateinit var player: ExoPlayer
    private var commentsJob: Job? = null
    lateinit var bottomSheetDialog: BottomSheetDialog

    // 状态标志
    private var isFullScreen = false
    private var isUserSeeking = false

    private val videoViewModel: VideoViewModel by lazy { VideoViewModel() }


    // 监听器定义 😊

    // 播放状态监听（播放/暂停按钮更新 & SeekBar最大值设置）
    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updatePlayPauseButton()
        }

        override fun onPlaybackStateChanged(state: Int) {
            if (state == Player.STATE_READY) {
                binding.seekBar.max = player.duration.toInt()
            }
        }
    }

    // 进度更新监听（控制进度条刷新）
    private val playStateListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) {
                if (binding.seekBar.isAttachedToWindow) {
                    binding.seekBar.post(progressUpdateRunnable)
                }
            } else {
                binding.seekBar.removeCallbacks(progressUpdateRunnable)
            }
        }
    }

    // 屏幕方向监听（切换全屏/退出全屏）
    private val layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && !isFullScreen) {
            //Configuration.ORIENTATION_LANDSCAPE是 Android View 的一个属性，表示当前 SeekBar 是否已经附着（挂载）在窗口上。
            //
            //只有当视图已经显示（或者即将显示）在屏幕上，才能安全地调用 post() 方法。
            enterFullScreen()
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT && isFullScreen) {
            exitFullScreen()
        }
    }

    // 定时刷新进度条
    private val progressUpdateRunnable = object : Runnable {
        override fun run() {
            if (!isUserSeeking && player.isPlaying) {
                val currentPos = player.currentPosition
                val duration = player.duration

                binding.seekBar.progress = currentPos.toInt()

                // 更新时间文本
                binding.tvCurrentTime.text = formatTime(currentPos)
                binding.tvTotalTime.text = formatTime(duration)
            }
            // 0.5秒循环
            binding.seekBar.postDelayed(this, 500)
        }
    }
    private val hideControlsRunnable = Runnable {
        binding.overlayControls.visibility = View.GONE
    }

    private fun showControlsTemporarily() {
        // 显示控制栏
        binding.overlayControls.visibility = View.VISIBLE
        // 先移除之前的隐藏任务，避免重复调用
        binding.overlayControls.removeCallbacks(hideControlsRunnable)
        // 延迟3秒自动隐藏
        binding.overlayControls.postDelayed(hideControlsRunnable, 3000)
    }


    override fun getViewBinding(): ActivityVideosBinding =
        ActivityVideosBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 保证状态栏文字是浅色（白色）
            window.insetsController?.setSystemBarsAppearance(
                0, // 清除亮色状态
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        // 注册全局布局监听
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
        player = ExoPlayer.Builder(applicationContext).build()//先初始化后面再传入url
        binding.playerView.player = player
    }

    override fun observeData() {
        videoViewModel._getUrlLiveData.observe(this) { result ->
            initPlayer(result.data.url)
            initUI()
            videoViewModel.getCommentNumber(id!!)
        }
        videoViewModel.getCommentNumberLiveData.observe(this) { result ->
            binding.mvCommentTv.text = result.commentCount.toString()
            binding.mvShareTv.text = result.shareCount.toString()

        }

    }

    override fun initEvent() {
        val views = listOf(binding.mvCommentImg, binding.mvShareImg)
        views.forEach { view ->
            view.scaleX = 0.8f
            view.scaleY = 0.8f
            view.alpha = 0f
            view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(400)
                .setStartDelay(300)
                .start()
        }

        binding.mvShareImg.setOnClickListener{
            initShare()
        }
        binding.videoName.text = name
        binding.videoBack.setOnClickListener {
            finish()
        }
        id?.let {
            videoViewModel.getUrl(it)
        }
        binding.mvCommentImg.setOnClickListener {
            showCommentsBottomSheet()
        }

        // 给播放器视图或根布局设置点击监听，点击时显示控制栏
        binding.playerView.setOnClickListener {
            if (binding.overlayControls.visibility == View.VISIBLE) {
                // 如果已经显示，点击就隐藏
                binding.overlayControls.visibility = View.GONE
                binding.overlayControls.removeCallbacks(hideControlsRunnable)
            } else {
                // 如果隐藏，点击就显示然后3秒后隐藏
                showControlsTemporarily()
            }
        }

    }
    private fun initShare(){
        val url = videoViewModel.Url.value
        if (url != null) {
            val customShare = CustomShare(this)
            customShare.setOnQqClickListener{
                ShareUtils.shareToQQ(
                    this,
                    "${name}这个视频很有意思，分享给你好啦\n${url.data.url}"
                )
                customShare.dismiss()
            }
            customShare.setOnWxClickListener {
                ShareUtils.shareToWX(
                    this,
                    "${name}这个视频很有意思，分享给你好啦\n${url.data.url}"
                )
            }
            customShare.setOnBrowseClickListener{
                ShareUtils.shareToBrowser(this,url.data.url)
                customShare.dismiss()
            }
            customShare.setOnLinkClickListener{
                CopyUtils.copy(
                    this,
                    "${name}这个视频很有意思，分享给你好啦\n${url.data.url}"
                )
                customShare.dismiss()
            }
            customShare.show()
        }

    }

    //显示底部弹窗->这里面使用了reycleview
    private fun showCommentsBottomSheet() {
        if (::bottomSheetDialog.isInitialized && bottomSheetDialog.isShowing) {
            bottomSheetDialog.dismiss()
        }
        // 用 ViewBinding 绑定 BottomSheet 布局
        val binding = LayoutBottomSheetCommentsBinding.inflate(layoutInflater)

        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnDismissListener {
            // 关闭弹窗时取消协程，避免泄漏
            commentsJob?.cancel()
        }

        // 初始化 RecyclerView
        val adapter = CommentAdapterMv(this)
        binding.recyclerViewComments.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewComments.adapter = adapter
        // 监听评论点击
        commentsJob?.cancel()
        commentsJob = lifecycleScope.launch {
            // 👇绑定生命周期，自动在 onDestroy 取消
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                videoViewModel.getComments(id!!).collect { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
        bottomSheetDialog.show()

    }


    private fun initPlayer(url: String) {
        binding.playerView.alpha = 0f
        binding.playerView.animate()
            .alpha(1f)
            .setDuration(500)
            .start()


        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
        // 注册监听器
        player.addListener(playerListener)
        player.addListener(playStateListener)
    }


    private fun initUI() {
        // 控件适配系统窗口
        binding.root.fitsSystemWindows = false
        binding.playerView.fitsSystemWindows = false

        binding.root.setOnApplyWindowInsetsListener { _, insets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowInsets.CONSUMED
            } else {
                @Suppress("DEPRECATION")
                insets.consumeSystemWindowInsets()
            }
        }

        // 自定义控制栏显示
        binding.overlayControls.visibility = View.VISIBLE

        // 播放/暂停按钮
        binding.playPauseButton.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }

        // 全屏切换按钮
        binding.fullscreenButton.setOnClickListener {
            toggleFullScreen()
        }

        // SeekBar事件
        binding.seekBar.max = 0
        binding.seekBar.progress = 0
        binding.seekBar.setOnSeekBarChangeListener(object :
            android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: android.widget.SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {

                }
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {
                isUserSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {
                isUserSeeking = false
                seekBar?.let {
                    player.seekTo(it.progress.toLong())
                }
            }
        })

        // 初始化播放/暂停按钮状态
        updatePlayPauseButton()
    }


    //更新图标状态
    private fun updatePlayPauseButton() {
        if (player.isPlaying) {
            binding.playPauseButton.setImageResource(R.drawable.play_stop)
        } else {
            binding.playPauseButton.setImageResource(R.drawable.ic_play_arrow)
        }
    }

    //判断是否切换全屏
    private fun toggleFullScreen() {
        if (!isFullScreen) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            enterFullScreen()
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            exitFullScreen()
        }
    }


    private fun enterFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    )
        }

        binding.playerView.layoutParams = binding.playerView.layoutParams.apply {
            height = ViewGroup.LayoutParams.MATCH_PARENT
            width = ViewGroup.LayoutParams.MATCH_PARENT
        }

        binding.videoBack.visibility = View.GONE
        binding.videoName.visibility = View.GONE
        binding.mvCommentImg.visibility = View.GONE
        binding.mvShareImg.visibility = View.GONE

        binding.mvCommentTv.visibility = View.GONE
        binding.mvShareTv.visibility = View.GONE

        isFullScreen = true
    }


    private fun exitFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
            window.insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            // 强制状态栏颜色为黑色
            window.statusBarColor = Color.BLACK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // 保证状态栏文字是浅色（白色）
                window.insetsController?.setSystemBarsAppearance(
                    0, // 清除亮色状态
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }

        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            window.statusBarColor = Color.BLACK
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }

        val heightInPx = (250 * resources.displayMetrics.density).toInt()
        binding.playerView.layoutParams = binding.playerView.layoutParams.apply {
            height = heightInPx
            width = ViewGroup.LayoutParams.MATCH_PARENT
        }
        binding.videoBack.visibility = View.VISIBLE
        binding.videoName.visibility = View.VISIBLE
        binding.mvCommentImg.visibility = View.VISIBLE
        binding.mvShareImg.visibility = View.VISIBLE

        binding.mvCommentTv.visibility = View.VISIBLE
        binding.mvShareTv.visibility = View.VISIBLE

        isFullScreen = false
    }

    //转换时间
    private fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }


    override fun onPause() {
        binding.seekBar.removeCallbacks(progressUpdateRunnable)
        binding.overlayControls.removeCallbacks(hideControlsRunnable)
        super.onPause()
    }

    override fun onStop() {
        binding.seekBar.removeCallbacks(progressUpdateRunnable)
        super.onStop()
        if (player.isPlaying) player.pause()
    }

    override fun onResume() {
        super.onResume()
        if (player.isPlaying && binding.seekBar.isAttachedToWindow) {
            binding.seekBar.post(progressUpdateRunnable)
        }
    }

    override fun onDestroy() {
        binding.seekBar.removeCallbacks(progressUpdateRunnable)
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
        binding.overlayControls.removeCallbacks(hideControlsRunnable)
        if (::player.isInitialized) {
            player.removeListener(playerListener)
            player.removeListener(playStateListener)
            binding.playerView.player = null
            if (!isChangingConfigurations) {
                player.release()
            }
        }
        if (::bottomSheetDialog.isInitialized) {
            bottomSheetDialog.setOnDismissListener(null)
            if (bottomSheetDialog.isShowing) bottomSheetDialog.dismiss()
        }
        commentsJob?.cancel()
        super.onDestroy()
    }

}
