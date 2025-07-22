package com.onerainbow.module.top.view

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.module.top.adapter.ArtistsRankAdapter
import com.onerainbow.module.top.databinding.FragmentArtistsBinding
import com.onerainbow.module.top.viewmodel.ArtistsViewModel

/**
 * description ： 歌手排行榜页
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 12:08
 */
class ArtistsFragment : BaseFragment<FragmentArtistsBinding>(){
    override fun getViewBinding(): FragmentArtistsBinding = FragmentArtistsBinding.inflate(layoutInflater)
    private val rvAdapter by lazy {
        ArtistsRankAdapter(requireContext())
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[ArtistsViewModel::class.java]
    }

    override fun initEvent() {
        initView()
        initData()
    }

    private fun initData() {
        viewModel.getArtistsRank()
    }

    private fun initView() {
        //关于RV的配置
        binding.artistsRv.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    override fun observeData() {
        viewModel.apply {
            isLoading.observe(this@ArtistsFragment){
                if (it) binding.artistsLoading.visibility = View.VISIBLE
                else binding.artistsLoading.visibility = View.INVISIBLE
            }

            artistsData.observe(this@ArtistsFragment){
                rvAdapter.submitList(it)
            }

            error.observe(this@ArtistsFragment){
                ToastUtils.makeText("发生错误：${it}")
            }
        }
    }
}