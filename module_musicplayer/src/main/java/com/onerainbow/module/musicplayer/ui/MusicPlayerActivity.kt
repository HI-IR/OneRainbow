package com.onerainbow.module.musicplayer.ui

import android.animation.ValueAnimator
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
import com.onerainbow.module.musicplayer.R
import com.onerainbow.module.musicplayer.databinding.ActivityMusicPlayerBinding
import com.onerainbow.module.musicplayer.model.Artist
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.viewmodel.MusicPlayerViewModel

class MusicPlayerActivity : BaseActivity<ActivityMusicPlayerBinding>() {
    private var lastIndex: Int? = null//用来和_playIndex做对比的
    private var isAnimating : Boolean = false //用来标记是否在动画中



    override fun getViewBinding(): ActivityMusicPlayerBinding =
        ActivityMusicPlayerBinding.inflate(layoutInflater)

    //CD旋转的属性动画
    private val cdAnimator by lazy {
        ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 20000 //20秒一圈
            interpolator = LinearInterpolator() //线性变化
            repeatCount = ValueAnimator.INFINITE //无限循环
            addUpdateListener {
                val currentRotation = it.animatedValue as Float
                binding.itemCdNextRotation.rotation = currentRotation
                binding.itemCdCurrentRotation.rotation = currentRotation
                binding.itemCdPrevRotation.rotation = currentRotation
            }
        }
    }





    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(BaseApplication.context)
        )[MusicPlayerViewModel::class.java]
    }

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
                //唱针
                val targetRotation = if (it) 23f else 0f
                binding.imgStylus.animate().rotation(targetRotation).apply {
                    duration = 300
                }

                // CD 转盘动画控制
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
            pivotX = 0.01f
            pivotY = 0.01f
        }


    }

    /**
     * 绑定点击事件
     */
    private fun initClick() {
        binding.apply {

            //开始播放按键
            musicplayerPlay.setOnClickListener {

                //TODO 临时处理修改播放状态
                viewModel._isPlaying.value = !viewModel.isPlaying.value!!


                //图标变化
                if (viewModel.isPlaying.value!!) {
                    musicplayerPlay.setImageResource(R.drawable.pause)//如果是播放状态下则显示暂停
                } else {
                    musicplayerPlay.setImageResource(R.drawable.play)//如果是暂停状态下则显示暂停
                }
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
     * prevCd 是原来播放的那张CD
     */
    private fun animateToNextCD() {
        if (isAnimating) return // 动画正在执行，直接返回
        isAnimating = true // 标记动画开始
        val nextCd = binding.itemCdNext
        val currentCd = binding.itemCdCurrent
        //获取屏幕宽度
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()

        //持续时间
        val durationTime = 500L

        // 初始化 next CD
        nextCd.visibility = View.VISIBLE
        nextCd.translationX = screenWidth // 从右边开始(把nextCd归位屏幕右侧外)

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
        ValueAnimator.ofFloat(0f, 1f).apply {
            interpolator = DecelerateInterpolator()//线性的插值器
            duration = durationTime

            //更新
            addUpdateListener {
                currentCd.translationX =
                    -screenWidth * it.animatedValue as Float // 前一个Cd往左边移动 最后移动一个屏幕的距离
                //后一个Cd从右边移动到中间，最后移动一个屏幕距离(初始化在一个屏幕之外)
                nextCd.translationX = screenWidth * (1 - it.animatedValue as Float)
            }

            //动画结束时
            doOnEnd {
                //重新归位中间
                currentCd.translationX = 0f
                nextCd.translationX = 0f
                nextCd.visibility = View.INVISIBLE //把下一张重新置空


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
                    Glide.with(this@MusicPlayerActivity).load(prevSong?.coverUrl).apply(requestOptions)
                        .into(binding.imgCoverPrev)
                }

                if (index < viewModel.playerLists.value!!.size - 1) {
                    val nextSong = viewModel.playerLists.value?.get(viewModel.playIndex.value!! + 1)
                    Glide.with(this@MusicPlayerActivity).load(nextSong?.coverUrl)
                        .apply(requestOptions).into(binding.imgCoverNext)
                }
                isAnimating = false // 标记动画结束
            }

        }.start()
    }

    private fun animateToPrevCD() {
        if (isAnimating) return // 动画正在执行，直接返回
        isAnimating = true // 标记动画开始
        val prevCd = binding.itemCdPrev
        val currentCd = binding.itemCdCurrent
        //获取屏幕宽度
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()

        //持续时间
        val durationTime = 500L

        //初始化位置
        prevCd.visibility = View.VISIBLE
        prevCd.translationX = -screenWidth //移除屏幕外


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

        ValueAnimator.ofFloat(0f, 1f).apply {
            interpolator = DecelerateInterpolator()
            duration = durationTime
            addUpdateListener {
                currentCd.translationX = screenWidth * it.animatedValue as Float

                prevCd.translationX = -screenWidth * (1 - it.animatedValue as Float)

            }

            doOnEnd {
                //重新归位中间
                currentCd.translationX = 0f
                prevCd.translationX = 0f
                prevCd.visibility = View.INVISIBLE //把下一张重新置空


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

        }.start()
    }
}