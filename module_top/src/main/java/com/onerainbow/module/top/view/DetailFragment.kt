package com.onerainbow.module.top.view

import android.graphics.Rect
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.module.top.adapter.DetailFrontAdapter
import com.onerainbow.module.top.adapter.DetailRankAdapter
import com.onerainbow.module.top.databinding.FragmentDetailBinding
import com.onerainbow.module.top.viewmodel.DetailViewModel

/**
 * description ： 排行榜概览页
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 12:34
 */
class DetailFragment: BaseFragment<FragmentDetailBinding>() {
    override fun getViewBinding(): FragmentDetailBinding = FragmentDetailBinding.inflate(layoutInflater)
    private val viewModel by lazy {
        ViewModelProvider(this)[DetailViewModel::class.java]
    }
    private val frontAdapter by lazy {
        DetailFrontAdapter(requireContext())
    }
    private val rankAdapter by lazy {
        DetailRankAdapter(requireContext())
    }


    override fun initEvent() {
        initView()
        initData()
    }

    private fun initView() {
        //配置官方榜单
        binding.detailRvFront.apply {
            adapter = frontAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false

            addItemDecoration(object : RecyclerView.ItemDecoration(){
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.bottom =30 //给下面一点间距
                }
            })
        }

        //配置精选榜
        binding.detailRvRank.apply {
            adapter = rankAdapter
            layoutManager = GridLayoutManager(context, 3)
            isNestedScrollingEnabled = false
        }

        // 设置下拉刷新监听器
        binding.swipeRefresh.setOnRefreshListener {
            initData()
        }
    }

    private fun initData() {
        viewModel.getDetailData()
    }

    override fun initViewModel() {
        viewModel.apply {
            frontData.observe(this@DetailFragment){
                frontAdapter.submitList(it)
                binding.swipeRefresh.isRefreshing = false
            }
            rankData.observe(this@DetailFragment){
                rankAdapter.submitList(it)
            }
        }
    }
}