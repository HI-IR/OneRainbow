package com.onerainbow.module.seek.Fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.module.seek.adapter.SongerAdapter
import com.onerainbow.module.seek.databinding.FragmentSingersingleBinding
import com.onerainbow.module.seek.viewmodel.PlaylistViewModel

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/22 20:33
 */
class SingerSingleFragment: BaseFragment<FragmentSingersingleBinding>()  {
    private val playlistViewModel: PlaylistViewModel by lazy { ViewModelProvider(requireActivity())[PlaylistViewModel::class.java] }
    private val songerAdapter: SongerAdapter by lazy { SongerAdapter() }
    private var id:Long? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = arguments?.getLong("id") ?:0
    }

    override fun getViewBinding(): FragmentSingersingleBinding =FragmentSingersingleBinding.inflate(layoutInflater)

    override fun initEvent() {
        binding.singerSingleRecycleview.adapter = songerAdapter
        binding.singerSingleRecycleview.layoutManager =LinearLayoutManager(requireContext())
        binding.singerSingleRecycleview.isNestedScrollingEnabled = true
        playlistViewModel.getSongsData(id!!)

    }

    override fun observeData() {
        playlistViewModel.songsDataLiveData.observe(viewLifecycleOwner){
            result ->
            songerAdapter.submitList(result.songs)
        }

    }
    companion object{
        fun newInstance(id:Long):SingerSingleFragment{
            val fragment = SingerSingleFragment()
            fragment.arguments= Bundle().apply {
                putLong("id",id)
            }
            return fragment
        }
    }

}
