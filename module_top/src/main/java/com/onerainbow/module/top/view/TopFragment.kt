package com.onerainbow.module.top.view

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.module.top.adapter.TopVp2Adapter
import com.onerainbow.module.top.databinding.FragmentTopBinding

/**
 * description ： 热门页的Fragment
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 09:44
 */
class TopFragment : BaseFragment<FragmentTopBinding>(){
    override fun getViewBinding(): FragmentTopBinding = FragmentTopBinding.inflate(layoutInflater)
    private val titleList = listOf("排行榜","歌手榜")
    private val fragments by lazy {
        listOf(
            DetailFragment(),
            ArtistsFragment()
            )
    }
    override fun initEvent() {
        initView()
    }

    private fun initView() {
        binding.topVp2.adapter = TopVp2Adapter(requireActivity(),fragments)
        //tab的设置
        binding.apply {
            TabLayoutMediator(topTablayout,topVp2,object : TabLayoutMediator.TabConfigurationStrategy{
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.text = titleList[position]
                }
            }).attach()


        }

    }

    override fun initViewModel() {

    }
}