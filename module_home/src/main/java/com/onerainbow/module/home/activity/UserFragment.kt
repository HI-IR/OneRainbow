package com.onerainbow.module.home.activity

import androidx.lifecycle.ViewModelProvider
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.home.databinding.FragmentUserBinding
import com.onerainbow.module.home.viewmodel.HomeViewModel
import com.therouter.TheRouter

/**
 * description ： 用户页的Fragment
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 00:45
 */
class UserFragment : BaseFragment<FragmentUserBinding>() {
    override fun getViewBinding(): FragmentUserBinding = FragmentUserBinding.inflate(layoutInflater)
    private val viewModel by lazy {
        //使用Home页的ViewModel
        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }
    override fun initEvent() {

    }

    override fun observeData() {
        viewModel.apply {

        }
    }
}