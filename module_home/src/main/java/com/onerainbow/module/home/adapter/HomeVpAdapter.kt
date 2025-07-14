package com.onerainbow.module.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * description ： 首页的Vp2的Adapter
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 09:53
 */
class HomeVpAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: List<Fragment>
)  : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}