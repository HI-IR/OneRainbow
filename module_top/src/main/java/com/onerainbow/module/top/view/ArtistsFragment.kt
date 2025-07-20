package com.onerainbow.module.top.view

import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.module.top.databinding.FragmentAllBinding
import com.onerainbow.module.top.databinding.FragmentArtistsBinding

/**
 * description ： TODO:类的作用
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 12:08
 */
class ArtistsFragment : BaseFragment<FragmentArtistsBinding>(){
    override fun getViewBinding(): FragmentArtistsBinding = FragmentArtistsBinding.inflate(layoutInflater)

    override fun initEvent() {
        //TODO("Not yet implemented")
    }

    override fun initViewModel() {
        //TODO("Not yet implemented")
    }
}