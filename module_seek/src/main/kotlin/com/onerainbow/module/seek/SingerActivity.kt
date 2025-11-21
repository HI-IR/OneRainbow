package com.onerainbow.module.seek

import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.seek.adapter.SingerVp2Adapter
import com.onerainbow.module.seek.databinding.ActivitySingerBinding
import com.therouter.router.Autowired
import com.therouter.router.Route

@Route(path = RoutePath.SINGER)
class SingerActivity : BaseActivity<ActivitySingerBinding>() {
    @Autowired(name = "id")
    var id: Long? = null

    @Autowired(name = "url")
    var url: String? = null

    @Autowired(name = "name")
    var name: String? = null
    private val tabTitles = listOf(
        "简介", "热门歌曲"
    )
    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun getViewBinding(): ActivitySingerBinding =
        ActivitySingerBinding.inflate(layoutInflater)


    override fun observeData() {

    }

    override fun initEvent() {
        // 沉浸式状态栏
        window.statusBarColor = android.graphics.Color.TRANSPARENT // 状态栏背景透明
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        initVp2()
        Glide.with(this)
            .load(url)
            .into(binding.imgSonger)
        binding.songerName.text = name
    }

    private fun initVp2() {
        binding.songerViewpager2.adapter = SingerVp2Adapter(this@SingerActivity, id!!)
        // ViewPager2 与 AppBarLayout 联动
        binding.songerViewpager2.isUserInputEnabled = false
        binding.songerViewpager2.offscreenPageLimit = 2
        tabLayoutMediator =
            TabLayoutMediator(binding.songerTabLayout, binding.songerViewpager2) { tab, position ->
                tab.text = tabTitles[position]
            }
        tabLayoutMediator?.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator?.detach()
    }

}