package com.example.module.seek.Fragment

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module.seek.adapter.LyricDataAdapter
import com.example.module.seek.adapter.SingleAdapter
import com.example.module.seek.databinding.FragmentLyricBinding
import com.example.module.seek.viewmodel.FinishSeekViewModel
import com.onerainbow.lib.base.BaseFragment

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 11:18
 */
class LyricFragment :BaseFragment<FragmentLyricBinding>() {
    private var keyword: String? = null
    private lateinit var lyricAdapter: LyricDataAdapter
    private val finishSeekViewmodel: FinishSeekViewModel by lazy {
        ViewModelProvider(requireActivity())[FinishSeekViewModel::class.java]
    }
    override fun getViewBinding(): FragmentLyricBinding =FragmentLyricBinding.inflate(layoutInflater)
    override fun initEvent() {
        lyricAdapter = LyricDataAdapter()
        binding.lyricRecycleview.layoutManager= LinearLayoutManager(context)
        binding.lyricRecycleview.adapter=lyricAdapter
        Log.d("keywordone",keyword.toString())
        keyword?.let {
            finishSeekViewmodel.getLyric(it)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyword = arguments?.getString("keyword") ?: ""



    }

    override fun initViewModel() {
        finishSeekViewmodel.LyricDataLiveData.observe(viewLifecycleOwner){
            result ->
            lyricAdapter.submitList(result.result.songs)

        }


    }
    companion object {
        fun newInstance(keyword: String?): LyricFragment {
            val fragment = LyricFragment()
            fragment.arguments = Bundle().apply {
                putString("keyword", keyword)
            }
            return fragment
        }
    }
}