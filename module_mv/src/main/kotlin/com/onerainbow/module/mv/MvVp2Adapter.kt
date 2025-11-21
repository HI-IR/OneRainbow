package com.onerainbow.module.mv

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.onerainbow.module.mv.fragment.MvForeignFragment
import com.onerainbow.module.mv.fragment.MvInlandFragment

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/21 14:37
 */
class MvVp2Adapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MvInlandFragment()
            1 -> MvForeignFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
