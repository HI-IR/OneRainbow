package com.onerainbow.module.home.activity

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.home.R
import com.onerainbow.module.home.adapter.HomeVpAdapter
import com.onerainbow.module.home.databinding.ActivityHomeBinding
import com.onerainbow.module.home.databinding.LayoutDrawerBinding
import com.onerainbow.module.musicplayer.model.Artist
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.service.MusicManager
import com.onerainbow.module.recommend.ui.RecommendFragment
import com.onerainbow.module.top.TopFragment
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
}