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
                binding.imgCdNext.rotation = currentRotation
                binding.imgCoverNext.rotation = currentRotation

                binding.imgCdPrev.rotation = currentRotation
                binding.imgCoverPrev.rotation = currentRotation

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
        PlayerList(this@MusicPlayerActivity) {
            viewModel.playAt(it)
            ToastUtils.makeText("点击了${it}")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //进度更新
        progressHandler.post(progressRunnable)
    }

    override fun initViewModel() {
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
                //重新初始化一下图片
                if (!isInit) {
                    initView()
                    isInit = true
                }

            }
            currentIndex.observe(this@MusicPlayerActivity) { it ->
                //播放Index变化
                if (it > lastIndex!!) {
                    //播放下一首歌
                    lastIndex = it
                    animateToNextCD()
                } else if (it != lastIndex) {
                    //播放上一首歌
                    lastIndex = it
                    animateToPrevCD()
                }
                if (it < 0) return@observe
                val song = viewModel.playlist.value?.get(it)
                //更新显示信息
                binding.musicplayerTitle.text = song?.name
                binding.musicplayerCreator.text = song?.artists?.joinToString(" / ") { it.name }
            }

        }
    }

    override fun initEvent() {
        initView()
        initClick()
    }

    private fun initView() {
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

        if (lastIndex!! > 0) {
            val prevSong = viewModel.playlist.value!!.get(lastIndex!! - 1)
            Glide.with(this@MusicPlayerActivity).load(prevSong.coverUrl).apply(requestOptions)
                .into(binding.imgCoverPrev)

        }
        if (lastIndex!! < viewModel.playlist.value?.size!! - 1) {
            val nextSong = viewModel.playlist.value!!.get(lastIndex!! + 1)
            Glide.with(this@MusicPlayerActivity).load(nextSong.coverUrl).apply(requestOptions)
                .into(binding.imgCoverNext)

        }

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
            imgCdNext.visibility = View.GONE
            imgCdPrev.visibility = View.GONE
            imgCoverCurrent.visibility = View.GONE
            imgCoverNext.visibility = View.GONE
            imgCoverPrev.visibility = View.GONE

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
                    // 用户拖动过程中，更新 UI 显示当前时间（可选）
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
                    val targetPosition = duration * seekBarProgress / 1000  // 百分比进度映射到实际时长
                    MusicManager.seekTo(targetPosition)
                    isUserSeeking = false
                }

            })

            musicplayerComment.setOnClickListener {
                val currentIndex = viewModel.currentIndex.value!!
                if (currentIndex<0) return@setOnClickListener
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
            ShareUtils.shareToQQ(this, "我正在听${item.name},点击链接你也来听吧！")
            customShare.dismiss()
        }
        customShare.setOnWxClickListener {
            ShareUtils.shareToWX(this, "我正在听${item.name},点击链接你也来听吧！")
            customShare.dismiss()
        }
//        customShare.setOnBrowseClickListener{
//            ShareUtils.shareToBrowser(this,item.url)
//            customShare.dismiss()
//        }
        customShare.setOnLinkClickListener() {
            CopyUtils.copy(this, "我正在听${item.name},点击链接你也来听吧")
            Toast.makeText(this, "已复制链接", Toast.LENGTH_SHORT).show()
            customShare.dismiss()
        }

        //显示弹窗
        customShare.show()
    }


    override fun onDestroy() {
        super.onDestroy()

        //释放内存
        //关闭CD动画
        cdAnimator.cancel()
        cdAnimator.removeAllUpdateListeners() // 移除监听器，避免持有引用

        // 取消所有正在执行的属性动画（如唱针动画）
        binding.imgStylus.animate().cancel()
        playerList.dismiss() // 关闭对话框

        //关闭跳转动画
        tempAnimator?.cancel()
        tempAnimator?.removeAllUpdateListeners() // 移除监听器，避免持有引用
        progressHandler.removeCallbacks(progressRunnable)//移除回调
    }

    /**
     * 下一首歌换CD的属性动画
     * nextCd 是换进来的CD
     * currentCd 是原来播放的那张CD
     */
    private fun animateToNextCD() {
        if (isAnimating) return // 动画正在执行，直接返回
        tempAnimator?.cancel() // 取消可能存在的旧动画
        isAnimating = true // 标记动画开始
        val nextCd = binding.imgCdNext //下一张的CD背景
        val nextCover = binding.imgCoverNext //下一张的CD封面

        val currentCd = binding.imgCdCurrent//当前页的CD背景
        val currentCover = binding.imgCoverCurrent//当前页的CD封面

        //获取屏幕宽度
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()

        //持续时间
        val durationTime = 500L

        // 初始化 next CD
        //显示CD
        nextCd.translationX = screenWidth // 从右边开始(把nextCd归位屏幕右侧外)
        nextCover.translationX = screenWidth
        nextCd.visibility = View.VISIBLE
        nextCover.visibility = View.VISIBLE


        //播放CD抬杠,放杆动画
        binding.imgStylus.animate()
            .rotation(10f)
            .setDuration(250) // 第一阶段时长
            .withEndAction {
                // 第一阶段结束后
                val rotation = if (viewModel.isPlaying.value!!) 23f else 0f

                binding.imgStylus.animate()
                    .rotation(rotation)
                    .setDuration(250) // 第二阶段时长
                    .start()
            }
            .start()

        //配置动画
        tempAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            interpolator = DecelerateInterpolator()//减速插值器
            duration = durationTime

            //更新
            addUpdateListener {
                val fraction = it.animatedFraction

                // 前一个Cd往左边移动 最后移动一个屏幕的距离
                currentCd.translationX = -screenWidth * fraction
                currentCover.translationX = -screenWidth * fraction

                //后一个Cd从右边移动到中间，最后移动一个屏幕距离(初始化在一个屏幕之外)
                nextCd.translationX = screenWidth * (1 - fraction)
                nextCover.translationX = screenWidth * (1 - fraction)
            }

            //动画结束时
            doOnEnd {
                //重新归位中间
                currentCd.translationX = 0f
                currentCover.translationX = 0f
                nextCd.translationX = 0f
                nextCover.translationX = 0f
                nextCd.visibility = View.INVISIBLE //把下一张隐藏
                nextCover.visibility = View.INVISIBLE

                val requestOptions: RequestOptions =
                    RequestOptions().placeholder(R.drawable.loading)
                        .fallback(R.drawable.loading)
                val index = viewModel.currentIndex.value!!
                if (index !in viewModel.playlist.value?.indices!!) {
                    showEmptyPlaylistState()
                    return@doOnEnd
                }
                //因为这个动画的调用意味着已经完成了viewModel的playIndex更新，所以此时的playIndex已经是更新后的了
                val currentSong = viewModel.playlist.value?.get(index)
                Glide.with(this@MusicPlayerActivity).load(currentSong?.coverUrl)
                    .apply(requestOptions).into(binding.imgCoverCurrent)

                if (index > 0) {
                    val prevSong = viewModel.playlist.value?.get(index - 1)
                    Glide.with(this@MusicPlayerActivity).load(prevSong?.coverUrl)
                        .apply(requestOptions)
                        .into(binding.imgCoverPrev)
                }

                if (index < viewModel.playlist.value!!.size - 1) {
                    val nextSong = viewModel.playlist.value?.get(viewModel.currentIndex.value!! + 1)
                    Glide.with(this@MusicPlayerActivity).load(nextSong?.coverUrl)
                        .apply(requestOptions).into(binding.imgCoverNext)
                }
                isAnimating = false // 标记动画结束
            }

        }
        tempAnimator?.start()
    }


    /**
     * 下一首歌换CD的属性动画
     * prevCd 是换进来的CD
     * currentCd 是原来播放的那张CD
     */
    private fun animateToPrevCD() {
        if (isAnimating) return // 动画正在执行，直接返回
        tempAnimator?.cancel() // 取消可能存在的旧动画
        isAnimating = true // 标记动画开始
        val prevCd = binding.imgCdPrev
        val prevCover = binding.imgCoverPrev
        val currentCd = binding.imgCdCurrent
        val currentCover = binding.imgCoverCurrent
        //获取屏幕宽度
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()

        //持续时间
        val durationTime = 500L

        //初始化位置
        prevCd.visibility = View.VISIBLE
        prevCover.visibility = View.VISIBLE
        prevCd.translationX = -screenWidth //移除屏幕外
        prevCover.translationX = -screenWidth

        //播放CD抬杠,放杆动画
        binding.imgStylus.animate()
            .rotation(10f)
            .setDuration(250) // 第一阶段时长
            .withEndAction {
                // 第一阶段结束后
                val rotation = if (viewModel.isPlaying.value!!) 23f else 0f

                binding.imgStylus.animate()
                    .rotation(rotation)
                    .setDuration(250) // 第二阶段时长
                    .start()
            }
            .start()

        tempAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = durationTime
            addUpdateListener {
                val fraction = it.animatedFraction
                currentCd.translationX = screenWidth * fraction
                currentCover.translationX = screenWidth * fraction
                prevCd.translationX = -screenWidth * (1 - fraction)
                prevCover.translationX = -screenWidth * (1 - fraction)
            }

            doOnEnd {
                //重新归位中间
                currentCd.translationX = 0f
                currentCover.translationX = 0f
                prevCd.translationX = 0f
                prevCover.translationX = 0f
                prevCd.visibility = View.INVISIBLE //把上一张隐藏
                prevCover.visibility = View.INVISIBLE


                val requestOptions: RequestOptions =
                    RequestOptions().placeholder(R.drawable.loading)
                        .fallback(R.drawable.loading)
                val index = viewModel.currentIndex.value!!
                if (index !in viewModel.playlist.value?.indices!!) {
                    showEmptyPlaylistState()
                    return@doOnEnd
                }

                //因为这个动画的调用意味着已经完成了viewModel的playIndex更新，所以此时的playIndex已经是更新后的了
                val currentSong = viewModel.playlist.value?.get(index)
                Glide.with(this@MusicPlayerActivity).load(currentSong?.coverUrl)
                    .apply(requestOptions).into(binding.imgCoverCurrent)

                if (index > 0) {
                    val prevSong = viewModel.playlist.value?.get(index - 1)
                    Glide.with(this@MusicPlayerActivity).load(prevSong?.coverUrl)
                        .apply(requestOptions).into(binding.imgCoverPrev)
                }

                if (index < viewModel.playlist.value!!.size - 1) {
                    val nextSong = viewModel.playlist.value?.get(viewModel.currentIndex.value!! + 1)
                    Glide.with(this@MusicPlayerActivity).load(nextSong?.coverUrl)
                        .apply(requestOptions).into(binding.imgCoverNext)
                }
                isAnimating = false // 标记动画结束
            }

        }
        tempAnimator?.start()
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