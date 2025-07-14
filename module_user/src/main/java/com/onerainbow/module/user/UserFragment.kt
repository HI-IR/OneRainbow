package com.onerainbow.module.user

import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.module.user.databinding.FragmentUserBinding

/**
 * description ： 用户页的Fragment
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 00:45
 */
class UserFragment : BaseFragment<FragmentUserBinding>() {
    override fun getViewBinding(): FragmentUserBinding = FragmentUserBinding.inflate(layoutInflater)

    override fun initEvent() {
        //TODO("Not yet implemented")
    }

    override fun initViewModel() {
        //TODO("Not yet implemented")
    }
}