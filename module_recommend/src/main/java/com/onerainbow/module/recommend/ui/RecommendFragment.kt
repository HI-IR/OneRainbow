package com.onerainbow.module.recommend.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.module.recommend.adapter.BannerAdapter
import com.onerainbow.module.recommend.adapter.CommunityPicksAdapter
import com.onerainbow.module.recommend.adapter.CuratedPlaylistAdapter
import com.onerainbow.module.recommend.adapter.TopListAdapter
import com.onerainbow.module.recommend.anim.BannerAnim
import com.onerainbow.module.recommend.anim.CommunityPicksAnim
import com.onerainbow.module.recommend.databinding.FragmentRecommendBinding
import com.onerainbow.module.recommend.viewmodel.RecommendViewModel

/**
 * description ： 推荐页的Fragment
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 09:43
 */
class RecommendFragment : BaseFragment<FragmentRecommendBinding>() {
    override fun getViewBinding(): FragmentRecommendBinding =
        FragmentRecommendBinding.inflate(layoutInflater)

    private val viewModel by lazy {
        ViewModelProvider(this)[RecommendViewModel::class.java]
    }
    private val bannerAdapter by lazy {
        BannerAdapter(requireContext())
    }
    private val curatedAdapter by lazy {
        CuratedPlaylistAdapter(requireContext())
    }
    private val communityPicksAdapter by lazy {
        CommunityPicksAdapter(requireContext())
    }
    private val toplistAdapter by lazy {
        TopListAdapter(requireContext())
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
            //轮播图初始化
            vp2Banner.adapter = bannerAdapter
            vp2Banner.setPageTransformer(BannerAnim())

            //甄选好歌
            rvCuratedPlaylist.adapter = curatedAdapter
            rvCuratedPlaylist.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            //热歌推荐
            vp2CommunityPicks.adapter = communityPicksAdapter
            vp2CommunityPicks.setPageTransformer(CommunityPicksAnim())
            vp2CommunityPicks.offscreenPageLimit = 4

            //榜单
            rvToplist.adapter = toplistAdapter
            rvToplist.layoutManager = LinearLayoutManager(requireContext())
            rvToplist.addItemDecoration(object : RecyclerView.ItemDecoration(){
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.bottom = 55//给下面一点间距
                }
            })
        }


        // 设置下拉刷新监听器
        binding.swipeRefresh.setOnRefreshListener {
            initData()
        }
    }

    private fun initData() {
        viewModel.getBanner()
        viewModel.fetchTopList()
        viewModel.getCurateList()
        viewModel.getTopPlayList()
    }

    override fun initViewModel() {
        viewModel.apply {

            //关于轮播图的信息
            banner.observe(this@RecommendFragment) {
                bannerAdapter.submitList(it)
                binding.swipeRefresh.isRefreshing = false
                binding.vp2Banner.setCurrentItem(
                    Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % bannerAdapter.currentList.size),
                    false
                )

            }

            //关于甄选歌单信息
            curatedList.observe(this@RecommendFragment) {
                curatedAdapter.submitList(it)
            }


            //热歌推荐
            communityPicks.observe(this@RecommendFragment) {
                communityPicksAdapter.submitList(it.chunked(3))
            }

            //榜单推荐
            toplist.observe(this@RecommendFragment) {
                toplistAdapter.submitList(it)
            }


            error.observe(this@RecommendFragment) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                binding.swipeRefresh.isRefreshing = false
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