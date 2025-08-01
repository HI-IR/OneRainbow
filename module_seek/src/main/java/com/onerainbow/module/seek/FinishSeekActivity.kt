package com.onerainbow.module.seek

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.onerainbow.module.seek.adapter.SeekFinishVpAdapter
import com.onerainbow.module.seek.databinding.ActivityFinishSeekBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.route.RoutePath
import com.therouter.TheRouter
import com.therouter.router.Autowired
import com.therouter.router.Route

object LiveDataBus {
    val keywordResult = MutableLiveData<String>()
}

@Route(path = RoutePath.FINISHSEEK)
class FinishSeekActivity : BaseActivity<ActivityFinishSeekBinding>() {
    @Autowired(name = "keyword")
    var keyword: String? = null
    private var tabLayoutMediator: TabLayoutMediator? = null
    private val tabTitles = listOf(
        "歌词", "歌单", "单曲", "歌手", "视频"
    )

    override fun getViewBinding(): ActivityFinishSeekBinding =
        ActivityFinishSeekBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun observeData() {


    }

    override fun initEvent() {
        binding.etSeek.setText(keyword)
        initVp2()
        binding.btnOpenDrawer.setOnClickListener {
            finish()
        }
        binding.toolbar.setOnClickListener {
            val newKeyword = binding.etSeek.text.toString()
            LiveDataBus.keywordResult.postValue(newKeyword)
            finish()
        }

    }

    private fun initVp2() {
        binding.finishSeekVp2.adapter = SeekFinishVpAdapter(this@FinishSeekActivity, keyword)
        binding.finishSeekVp2.offscreenPageLimit = 1

        tabLayoutMediator =
            TabLayoutMediator(binding.finishSeekTabLayout, binding.finishSeekVp2) { tab, position ->
                tab.text = tabTitles[position]
            }
        tabLayoutMediator?.attach()


    }

    override fun onDestroy() {
        super.onDestroy()
        binding.finishSeekVp2.adapter = null
        tabLayoutMediator?.detach()

    }


}