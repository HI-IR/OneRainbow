package com.onerainbow.module.home.activity

import android.animation.ValueAnimator
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.home.R
import com.onerainbow.module.home.adapter.HomeVpAdapter
import com.onerainbow.module.home.databinding.ActivityHomeBinding
import com.onerainbow.module.home.databinding.LayoutDrawerBinding
import com.onerainbow.module.home.viewmodel.HomeViewModel
import com.onerainbow.module.musicplayer.ui.PlayerListDialog
import com.onerainbow.module.mv.fragment.MvFragment
import com.onerainbow.module.recommend.ui.RecommendFragment
import com.onerainbow.module.top.view.TopFragment
import com.therouter.TheRouter
import com.therouter.router.Route

@Route(path = RoutePath.HOME)
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun getViewBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
    private val drawerBinding: LayoutDrawerBinding by lazy {
        binding.includeDrawer
    }

    private val fragments by lazy {
        listOf(
            RecommendFragment(),
            TopFragment(),
            MvFragment(),
            UserFragment()
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    //图片加载配置
    val requestOptions: RequestOptions =
        RequestOptions().placeholder(com.onerainbow.module.musicplayer.R.drawable.loading)
            .fallback(com.onerainbow.module.musicplayer.R.drawable.loading)

    private val playerList by lazy {
        //初始化对话框,设置点击事件
        PlayerListDialog(this@HomeActivity) {
            viewModel.playAt(it)
        }
    }

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
                binding.imgCover.rotation = currentRotation
                binding.imgCd.rotation = currentRotation
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //去除底部导航栏的覆盖色
        binding.navHome.itemIconTintList = null


    }

    private fun initVp2() {
        binding.apply {
            vp2Home.let {
                //配置适配器
                it.adapter = HomeVpAdapter(this@HomeActivity, fragments)

                it.isUserInputEnabled = false //拦截用户手势

                //设置滑动的事件与BottomNav的联动
                it.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        when (position) {
                            0 -> navHome.selectedItemId = R.id.menu_recommend
                            1 -> navHome.selectedItemId = R.id.menu_top
                            2 -> navHome.selectedItemId = R.id.menu_mv
                            3 -> navHome.selectedItemId = R.id.menu_user
                        }
                    }
                })
            }
        }
    }

    private fun initBottomNav() {

        //BottomNav联动Vp
        binding.navHome.apply {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_recommend -> binding.vp2Home.currentItem = 0
                    R.id.menu_top -> binding.vp2Home.currentItem = 1
                    R.id.menu_mv -> binding.vp2Home.currentItem = 2
                    R.id.menu_user -> binding.vp2Home.currentItem = 3
                }
                true
            }
        }
    }


    override fun observeData() {
        //注册ViewModel的监听
        viewModel.apply {
            isPlaying.observe(this@HomeActivity) {
                //图标变化
                if (it) {
                    binding.btnPlay.setImageResource(R.drawable.pause)//如果是播放状态下则显示暂停
                } else {
                    binding.btnPlay.setImageResource(R.drawable.play)//如果是暂停状态下则显示暂停
                }

                //CD 旋转动画
                if (it) {
                    //如果在播放状态下
                    if (!cdAnimator.isStarted) {
                        cdAnimator.start()//没启动则启动
                    } else {
                        cdAnimator.resume()//启动过了则恢复
                    }
                } else {
                    cdAnimator.pause()
                }
            }

            currentIndex.observe(this@HomeActivity) {
                if (viewModel.playlist.value == null) return@observe
                if (it in viewModel.playlist.value!!.indices) {
                    val currentSong = viewModel.playlist.value!![it]
                    Glide.with(this@HomeActivity).load(currentSong.coverUrl).apply(requestOptions)
                        .into(binding.imgCover)
                    binding.apply {
                        tvTitle.text = currentSong.name
                        tvCreator.text = currentSong.artists.joinToString("/") { it.name }
                    }
                    playerList.setSelectedPosition(it)

                }
            }

            //播放列表为空，则隐藏
            playlist.observe(this@HomeActivity) {
                if (it.isNullOrEmpty()) {
                    binding.playBar.visibility = View.GONE
                    playerList.setSongs(emptyList())
                    binding.vp2Home.setPadding(0, 0, 0, 0)
                    return@observe
                }
                //不为空则显示
                binding.vp2Home.setPadding(0, 0, 0, dpToPx(40))
                binding.vp2Home.clipToPadding = false
                binding.playBar.visibility = View.VISIBLE
                playerList.setSongs(it)
            }

            avatarData.observe(this@HomeActivity) {
                it?.let {
                    Glide.with(this@HomeActivity).load(it).apply(requestOptions)
                        .into(drawerBinding.imgAvatar)
                }?:drawerBinding.imgAvatar.setImageResource(R.drawable.avatar)
            }
            errorAvatar.observe(this@HomeActivity) {
                ToastUtils.makeText(it)
            }

            usernameData.observe(this@HomeActivity) {
                if (it.isNotBlank()) {
                    drawerBinding.apply {
                        btnLogout.visibility = View.VISIBLE
                        tvUsername.visibility = View.VISIBLE
                        tvUsername.text = it
                        tvLogin.visibility = View.GONE
                    }
                    //ToastUtils.makeText("欢迎回来~ ${it}")
                } else {
                    //返回“”，表示登出
                    drawerBinding.apply {
                        imgAvatar.setImageResource(R.drawable.avatar)
                        tvUsername.visibility = View.GONE
                        tvLogin.visibility = View.VISIBLE
                        btnLogout.visibility = View.GONE
                    }
                }
            }


        }
    }

    fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Resources.getSystem().displayMetrics
        ).toInt()
    }

    override fun initEvent() {
        initView()
        initClick()

    }

    private fun initClick() {
        binding.apply {
            btnOpenDrawer.setOnClickListener {
                drawerlayoutHome.openDrawer(GravityCompat.START)
            }
            searchBarMain.setOnClickListener {
                TheRouter.build(RoutePath.SEEK).navigation()
            }

            btnPlay.setOnClickListener {
                viewModel.togglePlayPause()
            }

            btnPlaylist.setOnClickListener {
                playerList.show()
            }

            playBar.setOnClickListener {
                TheRouter.build(RoutePath.MUSIC_PLAYER).navigation()
                overridePendingTransition(R.anim.slide_bottom, R.anim.hold)
            }
        }


        //侧边栏的事件绑定
        drawerBinding.apply {
            tvLogin.setOnClickListener {
                //跳转到登录页
                TheRouter.build(RoutePath.LOGIN).navigation()
            }
            tvUsername.setOnClickListener {
                binding.vp2Home.currentItem = 3 //去到我的页
                binding.drawerlayoutHome.close()
            }

            itemCollect.setOnClickListener {
                Toast.makeText(this@HomeActivity, "点击了我的收藏", Toast.LENGTH_SHORT).show()
            }
            itemRecentplayed.setOnClickListener {
                Toast.makeText(this@HomeActivity, "点击了最近播放", Toast.LENGTH_SHORT).show()
            }
            itemCloud.setOnClickListener {
                Toast.makeText(this@HomeActivity, "因为接口原因暂未实现", Toast.LENGTH_SHORT).show()
            }
            itemSubscription.setOnClickListener {
                Toast.makeText(this@HomeActivity, "因为接口原因暂未实现", Toast.LENGTH_SHORT).show()
            }
            btnLogout.setOnClickListener {
                viewModel.logout()
                ToastUtils.makeText("退出登录成功")
                binding.drawerlayoutHome.close()
            }
        }
    }

    private fun initView() {
        //初始化BottomNavigationView
        initBottomNav()

        //初始化VP2
        initVp2()
    }

    override fun onResume() {
        super.onResume()
        //加载头像
        viewModel.loadUserInfo()
        viewModel.loadAccount()
    }

    override fun onDestroy() {
        cdAnimator.cancel()
        cdAnimator.removeAllUpdateListeners() // 移除监听器
        super.onDestroy()
    }
}