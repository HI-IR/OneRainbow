package com.onerainbow.module.user

import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.user.databinding.FragmentUserBinding
import com.therouter.TheRouter
import com.therouter.router.Route

/**
 * description ： 用户页的Fragment
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 00:45
 */
class UserFragment : BaseFragment<FragmentUserBinding>() {
    override fun getViewBinding(): FragmentUserBinding = FragmentUserBinding.inflate(layoutInflater)

    override fun initEvent() {
        binding.myselfButton.setOnClickListener{TheRouter.build(RoutePath.LOGIN).navigation()}
    }

    override fun initViewModel() {
    }
}