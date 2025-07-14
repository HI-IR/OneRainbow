package com.onerainbow.module.top

import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.module.top.databinding.FragmentTopBinding

/**
 * description ： 热门页的Fragment
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 09:44
 */
class TopFragment : BaseFragment<FragmentTopBinding>(){
    override fun getViewBinding(): FragmentTopBinding = FragmentTopBinding.inflate(layoutInflater)

    override fun initEvent() {
        //TODO("Not yet implemented")
    }

    override fun initViewModel() {
        //TODO("Not yet implemented")
    }
}