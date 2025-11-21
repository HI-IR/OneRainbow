package com.onerainbow.module.seek.Fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.module.seek.adapter.SongPageAdapter
import com.onerainbow.module.seek.adapter.SongerAdapter
import com.onerainbow.module.seek.databinding.FragmentSingerhomepageBinding
import com.onerainbow.module.seek.viewmodel.PlaylistViewModel

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/22 19:32
 */
class SingerHomePageFragment : BaseFragment<FragmentSingerhomepageBinding>() {
    private val playlistViewModel: PlaylistViewModel by lazy { ViewModelProvider(requireActivity())[PlaylistViewModel::class.java] }
    private val songerAdapter: SongPageAdapter by lazy { SongPageAdapter() }

    private var id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = arguments?.getLong("id") ?: 0
    }

    override fun getViewBinding(): FragmentSingerhomepageBinding =
        FragmentSingerhomepageBinding.inflate(layoutInflater)

    override fun initEvent() {
        binding.singleHotRecycleview.adapter = songerAdapter
        binding.singleHotRecycleview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.singleHotRecycleview.isNestedScrollingEnabled = true
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.singleHotRecycleview)
        playlistViewModel.getSongsData(id!!)
    }

    override fun observeData() {
        playlistViewModel.songerDataLiveData.observe(viewLifecycleOwner) { result ->

            binding.singerIntroduceContent.text = result.artist.briefDesc
            var isExpanded = false

            binding.singerIntroduceContent.maxLines = 3 // 初始显示3行
            binding.tvExpand.setOnClickListener {
                if (isExpanded) {
                    binding.singerIntroduceContent.maxLines = 3
                    binding.tvExpand.text = "展开 v" // 切换按钮文字
                } else {
                    binding.singerIntroduceContent.maxLines = Int.MAX_VALUE
                    binding.tvExpand.text = "收起 ^"
                }
                isExpanded = !isExpanded
            }

        }
        playlistViewModel.songsDataLiveData.observe(viewLifecycleOwner) { result ->
            songerAdapter.submitList(result.songs)
            playlistViewModel.getSongerData(id!!)
        }

    }

    companion object {
        fun newInstance(id: Long): SingerHomePageFragment {
            val fragment = SingerHomePageFragment()
            fragment.arguments = Bundle().apply {
                putLong("id", id)
            }
            return fragment
        }
    }

}