package com.onerainbow.module.seek.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.onerainbow.module.seek.Fragment.SingerHomePageFragment
import com.onerainbow.module.seek.Fragment.SingerSingleFragment

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/22 19:33
 */
class SingerVp2Adapter (activity:FragmentActivity,private val id :Long):FragmentStateAdapter(activity){
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SingerHomePageFragment.newInstance(id)
            1 -> SingerSingleFragment.newInstance(id)
            else -> throw IllegalArgumentException("Invaild position")
        }

    }
}