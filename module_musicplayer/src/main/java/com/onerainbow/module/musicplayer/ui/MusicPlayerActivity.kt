package com.onerainbow.module.musicplayer.ui

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.base.BaseApplication
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.musicplayer.R
import com.onerainbow.module.musicplayer.databinding.ActivityMusicPlayerBinding
import com.onerainbow.module.musicplayer.model.Artist
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.viewmodel.MusicPlayerViewModel
import com.therouter.TheRouter
import com.therouter.router.Route

@Route(path = RoutePath.MUSIC_PLAYER)
class MusicPlayerActivity : BaseActivity<ActivityMusicPlayerBinding>() {
    private var lastIndex: Int? = null//用来和_playIndex做对比的
    private var isAnimating : Boolean = false //用来标记是否在动画中

    private var tempAnimator: ValueAnimator? = null


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
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(BaseApplication.context)
        )[MusicPlayerViewModel::class.java]
    }

    //TODO 歌单测试
    private val playerList by lazy {
        //初始化对话框
        PlayerList(this@MusicPlayerActivity) {
            ToastUtils.makeText("点击了${it.name}")
        }.apply {
            setSongs(
                listOf(
                    Song(1, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(2, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(3, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(5, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(6, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(7, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(8, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(9, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(11, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(12, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(13, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(14, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(15, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(16, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(17, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(18, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(19, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(20, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(21, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(22, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                    Song(23, "测试数据1", listOf(Artist("测试作者", 1)), "11", "11"),
                )
            )
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initViewModel() {
        viewModel.apply {
            isPlayInSingle.observe(this@MusicPlayerActivity) {
                if (it) {
                    binding.musicplayerPlayMode.setImageResource(R.drawable.single)
                } else {
                    binding.musicplayerPlayMode.setImageResource(R.drawable.listplay)
                }
            }

            playIndex.observe(this@MusicPlayerActivity) {
                if (it > lastIndex!!) {
                    //播放下一首歌
                    lastIndex = it
                    animateToNextCD()
                } else if (it != lastIndex) {
                    //播放上一首歌
                    lastIndex = it
                    animateToPrevCD()

                }
            }

            isPlaying.observe(this@MusicPlayerActivity) {

                //图标变化
                if (it) {
                    binding.musicplayerPlay.setImageResource(R.drawable.pause)//如果是播放状态下则显示暂停
                } else {
                    binding.musicplayerPlay.setImageResource(R.drawable.play)//如果是暂停状态下则显示暂停
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
        }
    }


    override fun initEvent() {
        initView()
        initClick()
    }

    //对View做一些初始化设置
    private fun initView() {
        lastIndex = viewModel.playIndex.value!! // 初始化

        //加载图片
        val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.loading)
            .fallback(R.drawable.loading)

        Log.d("动画!!!", lastIndex!!.toString());
        val currentSong = viewModel.playerLists.value!![lastIndex!!]
        Glide.with(this@MusicPlayerActivity).load(currentSong.coverUrl).apply(requestOptions)
            .into(binding.imgCoverCurrent)

        if (lastIndex!! > 0) {
            val prevSong = viewModel.playerLists.value!!.get(lastIndex!! - 1)
            Glide.with(this@MusicPlayerActivity).load(prevSong.coverUrl).apply(requestOptions)
                .into(binding.imgCoverPrev)

        }
        if (lastIndex!! < viewModel.playerLists.value?.size!! - 1) {
            val nextSong = viewModel.playerLists.value!!.get(lastIndex!! + 1)
            Glide.with(this@MusicPlayerActivity).load(nextSong.coverUrl).apply(requestOptions)
                .into(binding.imgCoverNext)

        }

        binding.imgStylus.apply {
            //设置唱针旋转位置居中
            pivotX = 0.03f
            pivotY = 0.03f
        }
    }

    //绑定点击事件
    private fun initClick() {
        binding.apply {

            //开始播放按键
            musicplayerPlay.setOnClickListener {
                viewModel.togglePlayOrPause()
            }

            //切换播放模式
            musicplayerPlayMode.setOnClickListener {
                viewModel.togglePlayMode()
            }

            //打开歌单列表
            musicplayerPlayList.setOnClickListener {
                playerList.show()
            }

            musicplayerPlayNext.setOnClickListener {
                if (isAnimating) return@setOnClickListener //如果在进行下一首歌的动画则特return
                viewModel.toggleNext()//在这里面有判断是否最后一首歌歌曲
            }
            musicplayerPlayPrev.setOnClickListener {
                if (isAnimating) return@setOnClickListener //如果在进行下一首歌的动画则特return
                viewModel.togglePrev()//在这里面有判断是否为第一首歌
            }

        }
    }


    /**
     * 初始化音乐服务
     */
    private fun initMusicService() {
        viewModel.bindService()
    }

    /**
     * 解绑
     */
    protected fun unbindMusicService() {
        viewModel.unbindService()
    }

    override fun onStart() {
        super.onStart()
        initMusicService()
        initPlayerMode()
    }

    private fun initPlayerMode() {
        viewModel.initPlayMode()
    }

    override fun onStop() {
        super.onStop()
        unbindMusicService()
        viewModel.savePlayMode()
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
        nextCd.visibility = View.VISIBLE
        nextCover.visibility = View.VISIBLE


        nextCd.translationX = screenWidth // 从右边开始(把nextCd归位屏幕右侧外)
        nextCover.translationX = screenWidth
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
        tempAnimator =ValueAnimator.ofFloat(0f, 1f).apply {
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
                val index = viewModel.playIndex.value!!
                //因为这个动画的调用意味着已经完成了viewModel的playIndex更新，所以此时的playIndex已经是更新后的了
                val currentSong = viewModel.playerLists.value?.get(index)
                Glide.with(this@MusicPlayerActivity).load(currentSong?.coverUrl)
                    .apply(requestOptions).into(binding.imgCoverCurrent)

                if (index > 0){
                    val prevSong = viewModel.playerLists.value?.get(index - 1)
                    Glide.with(this@MusicPlayerActivity).load(prevSong?.coverUrl)
                        .apply(requestOptions)
                        .into(binding.imgCoverPrev)
                }

                if (index < viewModel.playerLists.value!!.size - 1) {
                    val nextSong = viewModel.playerLists.value?.get(viewModel.playIndex.value!! + 1)
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
        prevCover.visibility = View.INVISIBLE
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
                val index = viewModel.playIndex.value!!

                //因为这个动画的调用意味着已经完成了viewModel的playIndex更新，所以此时的playIndex已经是更新后的了
                val currentSong = viewModel.playerLists.value?.get(index)
                Glide.with(this@MusicPlayerActivity).load(currentSong?.coverUrl)
                    .apply(requestOptions).into(binding.imgCoverCurrent)

                if (index > 0) {
                    val prevSong = viewModel.playerLists.value?.get(index - 1)
                    Glide.with(this@MusicPlayerActivity).load(prevSong?.coverUrl)
                        .apply(requestOptions).into(binding.imgCoverPrev)
                }

                if (index < viewModel.playerLists.value!!.size - 1) {
                    val nextSong = viewModel.playerLists.value?.get(viewModel.playIndex.value!! + 1)
                    Glide.with(this@MusicPlayerActivity).load(nextSong?.coverUrl)
                        .apply(requestOptions).into(binding.imgCoverNext)
                }
                isAnimating = false // 标记动画结束
            }

        }
        tempAnimator?.start()
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


        // 清理 Glide 未完成的加载任务
        Glide.with(this).clear(binding.imgCoverCurrent)
        Glide.with(this).clear(binding.imgCoverPrev)
        Glide.with(this).clear(binding.imgCoverNext)
    }

}