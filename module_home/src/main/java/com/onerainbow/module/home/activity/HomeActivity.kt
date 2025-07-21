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
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.home.R
import com.onerainbow.module.home.adapter.HomeVpAdapter
import com.onerainbow.module.home.databinding.ActivityHomeBinding
import com.onerainbow.module.home.databinding.LayoutDrawerBinding
import com.onerainbow.module.home.viewmodel.HomeViewModel
import com.onerainbow.module.musicplayer.model.Artist
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.service.MusicManager
import com.onerainbow.module.musicplayer.ui.PlayerListDialog
import com.onerainbow.module.recommend.ui.RecommendFragment
import com.onerainbow.module.top.view.TopFragment
import com.onerainbow.module.user.UserFragment
import com.onerainbow.module_mv.MvFragment
import com.therouter.TheRouter
import com.therouter.router.Route

@Route(path = RoutePath.HOME)
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun getViewBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
    private  val drawerBinding: LayoutDrawerBinding by lazy {
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

        //初始化BottomNavigationView
        initBottomNav()

        //初始化VP2
        initVp2()
    }

    private fun initVp2() {
        binding.apply {
            vp2Home.let {
                //配置适配器
                it.adapter = HomeVpAdapter(this@HomeActivity,fragments)

                it.isUserInputEnabled = false //拦截用户手势

                //设置滑动的事件与BottomNav的联动
                it.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        when(position){
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
            setOnItemSelectedListener { item->
                when(item.itemId){
                    R.id.menu_recommend -> binding.vp2Home.currentItem = 0
                    R.id.menu_top -> binding.vp2Home.currentItem = 1
                    R.id.menu_mv -> binding.vp2Home.currentItem = 2
                    R.id.menu_user -> binding.vp2Home.currentItem = 3
                }
                true
            }
        }
    }


    override fun initViewModel() {
        //注册ViewModel的监听
        viewModel.apply {
            isPlaying.observe(this@HomeActivity){
                //图标变化
                if (it) {
                    binding.btnPlay.setImageResource(R.drawable.pause)//如果是播放状态下则显示暂停
                } else {
                    binding.btnPlay.setImageResource(R.drawable.play)//如果是暂停状态下则显示暂停
                }

                //CD 旋转动画
                if (it){
                    //如果在播放状态下
                    if (!cdAnimator.isStarted){
                        cdAnimator.start()//没启动则启动
                    }else{
                        cdAnimator.resume()//启动过了则恢复
                    }
                }else{
                    cdAnimator.pause()
                }
            }

            currentIndex.observe(this@HomeActivity){
                if (viewModel.playlist.value == null) return@observe
                if (it in viewModel.playlist.value!!.indices){
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
            playlist.observe(this@HomeActivity){
                if (it.isNullOrEmpty()){
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

        }
    }
    fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Resources.getSystem().displayMetrics
        ).toInt()
    }

    //TODO 流出点击事件的接口，等待完善
    override fun initEvent() {
        binding.apply {
            btnOpenDrawer.setOnClickListener {
                drawerlayoutHome.openDrawer(GravityCompat.START)
            }
            searchBarMain.setOnClickListener {
               TheRouter.build(RoutePath.SEEK).navigation()
            }

            test.setOnClickListener {
                //TODO 测试
                MusicManager.addSongs(
                    mutableListOf(
                        Song(2722532807,"二十岁", listOf(Artist("宝石Gem",12084497)),"https://p2.music.126.net/v-h49Jow6qgEPCZkNXlY5A==/109951171462194133.jpg"),
                        Song(2721721636,"洗牌",listOf(Artist("宝石Gem",12084497),Artist("张天枢",33371675)),"https://p2.music.126.net/uermWb8sH_HYEwVScAAW8Q==/109951171392740991.jpg"),
                        Song(2724462272,"u sure u do?",listOf(Artist("张天枢",33371675)),"https://p2.music.126.net/whaVtSBYZlo-CqPRqU4Sig==/109951171439212278.jpg")
                    )
                )
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
            itemCollect.setOnClickListener {
                Toast.makeText(this@HomeActivity,"点击了我的收藏",Toast.LENGTH_SHORT).show()
            }
            itemRecentplayed.setOnClickListener {
                Toast.makeText(this@HomeActivity,"点击了最近播放",Toast.LENGTH_SHORT).show()
            }
            itemCloud.setOnClickListener {
                Toast.makeText(this@HomeActivity,"点击了我的云盘",Toast.LENGTH_SHORT).show()
            }
            itemSubscription.setOnClickListener {
                Toast.makeText(this@HomeActivity,"点击了我的关注",Toast.LENGTH_SHORT).show()
            }
            btnLogout.setOnClickListener {
                Toast.makeText(this@HomeActivity,"点击了退出登录",Toast.LENGTH_SHORT).show()
            }
            tvUsername.setOnClickListener {
                Toast.makeText(this@HomeActivity,"点击了登录",Toast.LENGTH_SHORT).show()
            }
            imgAvatar.setOnClickListener {
                Toast.makeText(this@HomeActivity,"点击了头像",Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroy() {
        cdAnimator.cancel()
        cdAnimator.removeAllUpdateListeners() // 移除监听器
        super.onDestroy()
    }
}