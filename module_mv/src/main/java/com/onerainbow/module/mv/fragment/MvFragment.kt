package com.onerainbow.module.mv.fragment

import com.google.android.material.tabs.TabLayoutMediator
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.module.mv.MvVp2Adapter
import com.onerainbow.module.mv.databinding.FragmentMvBinding


/**
 * description ： MV的Fragment
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 09:34
 */
class MvFragment : BaseFragment<FragmentMvBinding>(){
    private val tabTitles = listOf(
        "内地", "外国"
    )
    private var tabLayoutMediator: TabLayoutMediator? = null
    override fun getViewBinding(): FragmentMvBinding = FragmentMvBinding.inflate(layoutInflater)

    override fun initEvent() {
        initVp2()
    }

    override fun initViewModel() {
    }
    private fun initVp2() {
        binding.mvVp2.adapter = MvVp2Adapter(this@MvFragment)
        binding.mvVp2.offscreenPageLimit = 1

        tabLayoutMediator = TabLayoutMediator(binding.mvTabLayout,binding.mvVp2){
                tab ,position ->
            tab.text =tabTitles[position]
        }
        tabLayoutMediator?.attach()


    }

}