package com.example.module.seek.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.module.seek.Fragment.LyricFragment
import com.example.module.seek.Fragment.PlaylistFragment
import com.example.module.seek.Fragment.SingleFragment
import com.example.module.seek.Fragment.UsersFragment
import com.example.module.seek.Fragment.VideoFragment

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 11:13
 */
class SeekFinishVpAdapter(
    activity: FragmentActivity,
    private val keyword:String?
) : FragmentStateAdapter(activity) {


    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LyricFragment.newInstance(keyword)
            1 -> PlaylistFragment.newInstance(keyword)
            2 -> SingleFragment.newInstance(keyword)
            3 -> UsersFragment.newInstance(keyword)
            4 -> VideoFragment.newInstance(keyword)
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}

