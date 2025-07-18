package com.example.module.seek.Fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module.seek.adapter.PlaylistDataAdapter
import com.example.module.seek.databinding.FragmentPalylistBinding
import com.example.module.seek.viewmodel.FinishSeekViewModel
import com.onerainbow.lib.base.BaseFragment

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 11:15
 */
class PlaylistFragment :BaseFragment<FragmentPalylistBinding>() {
    private var keyword: String? = null
    private val finishSeekViewmodel: FinishSeekViewModel by lazy {
        ViewModelProvider(requireActivity())[FinishSeekViewModel::class.java]
    }
    private lateinit var playlistAdapter:PlaylistDataAdapter
    override fun getViewBinding(): FragmentPalylistBinding =FragmentPalylistBinding.inflate(layoutInflater)

    override fun initEvent() {
        playlistAdapter =PlaylistDataAdapter()
        binding.playlistRecycleview.layoutManager=LinearLayoutManager(context)
        binding.playlistRecycleview.adapter=playlistAdapter
        keyword?.let {
            finishSeekViewmodel.getPlaylist(it)
        }


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyword = arguments?.getString("keyword") ?: ""


    }
    override fun initViewModel() {
        finishSeekViewmodel.playlistDataLiveData.observe(viewLifecycleOwner){
            result ->
            playlistAdapter.submitList(result.result.playlists)
        }

    }
    companion object {
        fun newInstance(keyword: String?): PlaylistFragment {
            val fragment = PlaylistFragment()
            fragment.arguments = Bundle().apply {
                putString("keyword", keyword)
            }
            return fragment
        }
    }

}