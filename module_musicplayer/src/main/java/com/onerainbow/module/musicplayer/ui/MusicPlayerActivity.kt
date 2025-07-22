package com.onerainbow.module.musicplayer.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.base.utils.CopyUtils
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.musicplayer.R
import com.onerainbow.module.musicplayer.databinding.ActivityMusicPlayerBinding
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.service.MusicManager
import com.onerainbow.module.musicplayer.viewmodel.MusicPlayerViewModel
import com.onerainbow.module.share.CustomShare
import com.onerainbow.module.share.utils.ShareUtils
import com.therouter.TheRouter
import com.therouter.router.Route

@Route(path = RoutePath.MUSIC_PLAYER)
class MusicPlayerActivity : BaseActivity<ActivityMusicPlayerBinding>() {
    private var lastIndex: Int? = null//用来和_playIndex做对比的
    private var isAnimating: Boolean = false //用来标记是否在动画中
    private var isInit: Boolean = false
    private var tempAnimator: ValueAnimator? = null
    private var isUserSeeking = false

    private val progressHandler = Handler(Looper.getMainLooper())
    private val progressRunnable = object : Runnable {
        override fun run() {
            if (!isUserSeeking && viewModel.isPlaying.value!!) {
                val position = MusicManager.getCurrentPosition()
                val duration = MusicManager.getDuration()
                val progress = (position * 1000 / duration).toInt()
                binding.musicplayerSeekbar.progress = progress
                binding.musicplayerNow.text = formatTime(position)
                binding.musicplayerEnd.text = formatTime(duration)
            }
            progressHandler.postDelayed(this, 500)
        }
    }

    //加载图片
    val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.loading)
        .fallback(R.drawable.loading)

    override fun getViewBinding(): ActivityMusicPlayerBinding =
        ActivityMusicPlayerBinding.inflate(layoutInflater)


    /**
     * CD旋转
     */
    private val cdAnimator by lazy {
        ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 20000 //20秒一圈
            interpolator = LinearInterpolator() //线性变化
            repeatCount = ValueAnimator.INFINITE //无限循环
            addUpdateListener {
                val currentRotation = it.animatedValue as Float
                binding.imgCdTool.rotation = currentRotation
                binding.imgCoverTool.rotation = currentRotation
                binding.imgCdCurrent.rotation = currentRotation
                binding.imgCoverCurrent.rotation = currentRotation
            }
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[MusicPlayerViewModel::class.java]
    }

    private val playerList by lazy {
        //初始化对话框,设置点击事件
        PlayerListDialog(this@MusicPlayerActivity) {
            viewModel.playAt(it)
            ToastUtils.makeText("点击了${it}")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //进度更新
        progressHandler.post(progressRunnable)
    }

    override fun observeData() {
        viewModel.apply {
            isPlaying.observe(this@MusicPlayerActivity) {
                //图标变化
                if (it) {
                    binding.musicplayerPlay.setImageResource(R.drawable.pause_item)//如果是播放状态下则显示暂停
                } else {
                    binding.musicplayerPlay.setImageResource(R.drawable.play_item)//如果是暂停状态下则显示暂停
                }

                //唱针动画
                val targetRotation = if (it) 23f else 0f
                binding.imgStylus.animate().rotation(targetRotation).apply {
                    duration = 300
                }

                // CD 转盘动画
                if (it) {
                    // 开始播放
                    if (!cdAnimator.isStarted) {
                        cdAnimator.start()
                    } else if (cdAnimator.isPaused) {
                        cdAnimator.resume()
                    }
                } else {
                    cdAnimator.pause()
                }
            }

            isPlayInSingle.observe(this@MusicPlayerActivity) {
                if (it) {
                    binding.musicplayerPlayMode.setImageResource(R.drawable.single)
                } else {
                    binding.musicplayerPlayMode.setImageResource(R.drawable.listplay)
                }
            }

            playlist.observe(this@MusicPlayerActivity) {
                playerList.setSongs(it)
                if (it.isEmpty()) {
                    showEmptyPlaylistState()
                    return@observe
                }

                //重新初始化一下图片
                if (!isInit) {
                    initView()
                    isInit = true
                }

            }
            currentIndex.observe(this@MusicPlayerActivity) { it ->
                //第一次加载
                if (lastIndex == null) {
                    initView()
                }
                //如果播放列表为空则直接显示空白页，并且退出

                if (it !in playlist.value!!.indices) return@observe

                if (lastIndex != it){
                    cdChangeAnimator(lastIndex!!, it, playlist.value!!)
                }

                val song = viewModel.playlist.value?.get(it)
                //更新显示信息
                binding.musicplayerTitle.text = song?.name
                binding.musicplayerCreator.text = song?.artists?.joinToString(" / ") { it.name }
                playerList.setSelectedPosition(it)
                lastIndex = it
            }

        }
    }

    override fun initEvent() {
        initView()
        initClick()
    }

    private fun initView() {
        binding.musicplayerTitle.isSelected = true
        lastIndex = viewModel.currentIndex.value

        // 检查播放列表是否为空
        val playlist = viewModel.playlist.value ?: emptyList()
        if (playlist.isEmpty()) {
            // 处理空列表情况：显示空状态UI或提示用户添加歌曲
            showEmptyPlaylistState()
            return
        }


        val currentSong = viewModel.playlist.value!![lastIndex!!]
        Glide.with(this@MusicPlayerActivity).load(currentSong.coverUrl).apply(requestOptions)
            .into(binding.imgCoverCurrent)

        binding.imgStylus.apply {
            //设置唱针旋转位置居中
            pivotX = 0.03f
            pivotY = 0.03f
        }

    }

    private fun showEmptyPlaylistState() {
        //空播放列表的话则隐藏内容
        binding.apply {
            imgCdCurrent.visibility = View.VISIBLE
            imgCdTool.visibility = View.GONE
            imgCoverCurrent.visibility = View.GONE
            imgCoverTool.visibility = View.GONE
            musicplayerTitle.visibility = View.GONE
            musicplayerCreator.visibility = View.GONE
            musicplayerCollected.visibility = View.GONE
            musicplayerComment.visibility = View.GONE
            musicplayerSeekbarLine.visibility = View.GONE
            musicplayerNow.visibility = View.GONE
            musicplayerEnd.visibility = View.GONE
            musicplayerControl.visibility = View.GONE
            musicplayerShare.visibility = View.GONE

            musicplayerEmpty.visibility = View.VISIBLE
        }


    }

    private fun initClick() {
        binding.apply {
            //播放按键
            musicplayerPlay.setOnClickListener {
                viewModel.togglePlayPause()
            }
            //切换播放模式
            musicplayerPlayMode.setOnClickListener {
                viewModel.togglePlayMode()
            }

            //打开歌单列表
            musicplayerPlayList.setOnClickListener {
                playerList.show()
            }

            //下一首歌
            musicplayerPlayNext.setOnClickListener {
                if (isAnimating) return@setOnClickListener //如果在进行下一首歌的动画则特return
                viewModel.playNext()
            }

            //上一首歌
            musicplayerPlayPrev.setOnClickListener {
                if (isAnimating) return@setOnClickListener //如果在进行下一首歌的动画则特return
                viewModel.playPrev()
            }

            //返回
            musicplayerFoldup.setOnClickListener {
                finish()
                overridePendingTransition(R.anim.hold_anim, R.anim.slide_out_bottom)
            }

            //分享
            musicplayerShare.setOnClickListener {
                //初始化分享弹窗
                initShare()
            }

            //seekBar的滑动
            musicplayerSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                    if (fromUser) {
                        val duration = MusicManager.getDuration()
                        val position = duration * progress / 1000
                        binding.musicplayerNow.text = formatTime(position)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    isUserSeeking = true // 标记进入拖动状态
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val duration = MusicManager.getDuration()
                    val seekBarProgress = seekBar.progress
                    val targetPosition = duration * seekBarProgress / 1000
                    MusicManager.seekTo(targetPosition)
                    isUserSeeking = false
                }

            })

            musicplayerComment.setOnClickListener {
                val currentIndex = viewModel.currentIndex.value!!
                if (currentIndex < 0) return@setOnClickListener
                val id = viewModel.playlist.value?.get(currentIndex)?.id
                if (id != null) {
                    TheRouter.build(RoutePath.COMMENTS)
                        .withLong("musicId", id)
                        .navigation()
                }

            }

        }
    }

    //初始化分享弹窗
    private fun initShare() {
        val playlist = viewModel.playlist.value ?: emptyList()
        if (playlist.isEmpty()) return
        val index = viewModel.currentIndex.value ?: 0
        val item = viewModel.playlist.value?.get(index)!!
        val customShare = CustomShare(this)
        customShare.setOnQqClickListener {
            ShareUtils.shareToQQ(
                this,
                "我正在听${item.name},点击链接你也来听吧！\n${viewModel.getCurrentUrl()}"
            )
            customShare.dismiss()
        }
        customShare.setOnWxClickListener {
            ShareUtils.shareToWX(
                this,
                "我正在听${item.name},点击链接你也来听吧！\n${viewModel.getCurrentUrl()}"
            )
            customShare.dismiss()
        }
        customShare.setOnBrowseClickListener{
            ShareUtils.shareToBrowser(this,viewModel.getCurrentUrl())
            customShare.dismiss()
        }
        customShare.setOnLinkClickListener() {
            CopyUtils.copy(
                this,
                "我正在听${item.name},点击链接你也来听吧\n${viewModel.getCurrentUrl()}"
            )
            Toast.makeText(this, "已复制链接", Toast.LENGTH_SHORT).show()
            customShare.dismiss()
        }

        customShare.show()
    }


    override fun onDestroy() {
        super.onDestroy()

        //释放内存

        cdAnimator.cancel()
        cdAnimator.removeAllUpdateListeners()


        binding.imgStylus.animate().cancel()
        playerList.dismiss()

        //关闭Cd跳转动画
        tempAnimator?.cancel()
        tempAnimator?.removeAllUpdateListeners()
        progressHandler.removeCallbacks(progressRunnable)
    }

    /*
    在currentIndex已经变化后调用
    实现原理，通过比较oldIndex 和 newIndex的大小，决定currentView是从哪边飘进来，tool是用来做动画的工具View，用来临时显示
     */
    private fun cdChangeAnimator(oldIndex: Int, newIndex: Int, playerlist: List<Song>) {
        if (isAnimating) return // 如果在播放动画则直接return
        tempAnimator?.cancel() // 取消可能存在的动画
        isAnimating = true
        if (oldIndex !in playerlist.indices || newIndex !in playerlist.indices) return
        val currentCover = binding.imgCoverCurrent
        val currentCd = binding.imgCdCurrent
        val toolCover = binding.imgCoverTool
        val toolCd = binding.imgCdTool
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()


        loadCover(toolCover, playerlist[oldIndex].coverUrl, requestOptions)
        loadCover(currentCover, playerlist[newIndex].coverUrl, requestOptions)

        //初始化动画
        toolCover.visibility = View.VISIBLE
        toolCd.visibility = View.VISIBLE

        //动画开始前加载图片
        if (oldIndex > newIndex) {
            currentCover.translationX = -screenWidth
            currentCd.translationX = -screenWidth
            stylusUpAnimtor()
            //新的Index < 旧的Index，说明是前面的歌曲,current需要从左边进来，tool需要从右边出去
            tempAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
                interpolator = DecelerateInterpolator()
                duration = 500L
                addUpdateListener {
                    val fraction = it.animatedFraction

                    //currentCd从左边回到中心
                    currentCover.translationX = -screenWidth * (1 - fraction)
                    currentCd.translationX = -screenWidth * (1 - fraction)

                    //toolCover从中心往右离开屏幕
                    toolCover.translationX = screenWidth * fraction
                    toolCd.translationX = screenWidth * fraction
                }

                doOnEnd {
                    //隐藏工具Cd，并且归位
                    toolCd.visibility = View.INVISIBLE
                    toolCover.visibility = View.INVISIBLE
                    toolCover.translationX = 0f
                    toolCd.translationX = 0f
                }
            }
        } else {
            currentCd.translationX = screenWidth
            currentCover.translationX = screenWidth
            stylusUpAnimtor()
            //与上面情况相反
            tempAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
                interpolator = DecelerateInterpolator()
                duration = 500L
                addUpdateListener {
                    //current需要从右边进来， tool需要从中间往左边离开
                    val fraction = it.animatedFraction
                    currentCd.translationX = screenWidth * (1 - fraction)
                    currentCover.translationX = screenWidth * (1 - fraction)

                    toolCover.translationX = -screenWidth * fraction
                    toolCd.translationX = -screenWidth * fraction
                }
                doOnEnd {
                    //隐藏工具Cd，并且归位
                    toolCd.visibility = View.INVISIBLE
                    toolCover.visibility = View.INVISIBLE
                    toolCover.translationX = 0f
                    toolCd.translationX = 0f
                }
            }
        }
        tempAnimator?.start()
        isAnimating = false
    }

    /**
     * 加载封面的函数，
     * @param coverView 需要加载图片的View
     * @param picUrl 图片URL
     */
    fun loadCover(coverView: ImageView, picUrl: String, requestOptions: RequestOptions) {
        Glide.with(this).load(picUrl).apply(requestOptions).into(coverView)
    }

    /**
     * 唱针抬起的动画
     */
    fun stylusUpAnimtor() {
        //播放CD抬杠,放杆动画
        binding.imgStylus.animate()
            .rotation(10f)
            .setDuration(250)
            .withEndAction {
                val rotation = if (viewModel.isPlaying.value!!) 23f else 0f
                binding.imgStylus.animate()
                    .rotation(rotation)
                    .setDuration(250) // 第二阶段时长
                    .start()
            }
            .start()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, R.anim.slide_out_bottom)
    }

    //格式时长
    @SuppressLint("DefaultLocale")
    private fun formatTime(ms: Long): String {
        val totalSec = ms / 1000
        val min = totalSec / 60
        val sec = totalSec % 60
        return String.format("%02d:%02d", min, sec)
    }
}