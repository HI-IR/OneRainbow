package com.onerainbow.module.account.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.onerainbow.module.account.CaptchaFragment
import com.onerainbow.module.account.LoginActivity
import com.onerainbow.module.account.UsernameFragment

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/14 11:06
 */


class FragmentAdapter(activity: LoginActivity):FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CaptchaFragment()
            1 -> UsernameFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}