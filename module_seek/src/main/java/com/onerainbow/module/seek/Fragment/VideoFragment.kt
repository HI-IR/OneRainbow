package com.onerainbow.module.seek.Fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.module.seek.adapter.GetMvDataAdapter
import com.onerainbow.module.seek.databinding.FragmentVideoBinding
import com.onerainbow.module.seek.viewmodel.FinishSeekViewModel

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 11:17
 */
class VideoFragment : BaseFragment<FragmentVideoBinding>() {
    private var keyword: String? = null
    private val finishSeekViewmodel: FinishSeekViewModel by lazy {
        ViewModelProvider(requireActivity())[FinishSeekViewModel::class.java]
    }
    private lateinit var getMvDataAdapter: GetMvDataAdapter
    override fun getViewBinding(): FragmentVideoBinding =
        FragmentVideoBinding.inflate(layoutInflater)

    override fun initEvent() {
        finishSeekViewmodel.getMvDataLiveData.observe(viewLifecycleOwner) { result ->
            getMvDataAdapter.submitList(result.result.mvs)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyword = arguments?.getString("keyword") ?: ""


    }

    override fun initViewModel() {
        getMvDataAdapter = GetMvDataAdapter()
        binding.mvRecycleview.adapter = getMvDataAdapter
        binding.mvRecycleview.layoutManager = LinearLayoutManager(context)
        keyword?.let {
            finishSeekViewmodel.getGetMv(it)
        }

    }

    companion object {
        fun newInstance(keyword: String?): VideoFragment {
            val fragment = VideoFragment()
            fragment.arguments = Bundle().apply {
                putString("keyword", keyword)
            }
            return fragment
        }
    }

}