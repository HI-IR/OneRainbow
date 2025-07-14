package com.onerainbow.module.recommend.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.module.recommend.adapter.BannerAdapter
import com.onerainbow.module.recommend.databinding.FragmentRecommendBinding
import com.onerainbow.module.recommend.viewmodel.RecommendViewModel

/**
 * description ： 推荐页的Fragment
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 09:43
 */
class RecommendFragment : BaseFragment<FragmentRecommendBinding>(){
    override fun getViewBinding(): FragmentRecommendBinding = FragmentRecommendBinding.inflate(layoutInflater)
    private val viewModel by lazy {
        ViewModelProvider(this)[RecommendViewModel::class.java]
    }
    private val bannerAdapter by lazy {
        BannerAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun initEvent() {
        initView()
        initData()

    }

    //初始化一些View
    private fun initView() {
        binding.apply {
            vp2Banner.adapter = bannerAdapter
        }

        // 设置下拉刷新监听器
        binding.swipeRefresh.setOnRefreshListener {
            initData()
        }
    }

    private fun initData() {
        viewModel.getBanner()
    }

    override fun initViewModel() {
        viewModel.apply {
            banner.observe(this@RecommendFragment){
                bannerAdapter.submitList(it)
                binding.swipeRefresh.isRefreshing =false
                binding.vp2Banner.setCurrentItem(Int.MAX_VALUE/2 -(Int.MAX_VALUE/2 % bannerAdapter.currentList.size),false)

            }
            error.observe(this@RecommendFragment){
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
                binding.swipeRefresh.isRefreshing =false
            }
        }
    }


    private val autoScroll = object : Runnable {
        override fun run() {
            val listSize = bannerAdapter.currentList.size
            if (listSize > 0) {
                val current = binding.vp2Banner.currentItem
                val next = (current + 1) % Int.MAX_VALUE
                binding.vp2Banner.setCurrentItem(next, true)
                binding.vp2Banner.postDelayed(this, 5000) // 每5秒切换
            }
        }
    }

    private fun startAutoScroll() {
        binding.vp2Banner.removeCallbacks(autoScroll)
        binding.vp2Banner.postDelayed(autoScroll, 7000)
    }

    private fun stopAutoScroll() {
        binding.vp2Banner.removeCallbacks(autoScroll)
    }

    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScroll()
    }


}