package com.onerainbow.module.top.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * description ： 热门页VP2的Adapter
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 12:28
 */
class TopVp2Adapter(
    fragmentActivity: FragmentActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}