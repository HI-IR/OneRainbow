package com.onerainbow.module.recommend.ui

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
                    outRect.bottom =30 //给下面一点间距
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
        viewModel.getCommunityPicks()
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
    private val handler = Handler(Looper.getMainLooper())
    private val autoScroll = object : Runnable {
        override fun run() {
            if (!isAdded || binding == null)
                return

            val listSize = bannerAdapter.currentList.size
            if (listSize > 0){
                val current = binding.vp2Banner.currentItem
                val nextPage = (current + 1) % listSize
                binding.vp2Banner.setCurrentItem(nextPage,true)
                handler.postDelayed(this, 5000)
            }
        }
    }


    private fun startAutoScroll() {
        handler.removeCallbacks(autoScroll)
        handler.postDelayed(autoScroll, 5000)

    }

    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(autoScroll)
    }

    override fun onDestroyView() {
        handler.removeCallbacks(autoScroll)
        super.onDestroyView()
    }


}