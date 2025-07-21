package com.onerainbow.module.mv

import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.module.mv.databinding.FragmentMvBinding


/**
 * description ： MV的Fragment
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 09:34
 */
class MvFragment : BaseFragment<FragmentMvBinding>(){
    override fun getViewBinding(): FragmentMvBinding = FragmentMvBinding.inflate(layoutInflater)

    override fun initEvent() {
        //TODO("Not yet implemented")
    }

    override fun initViewModel() {
        //TODO("Not yet implemented")
    }
}