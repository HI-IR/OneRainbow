package com.onerainbow.module.seek.Fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.module.seek.adapter.GetMvDataAdapter
import com.onerainbow.module.seek.databinding.FragmentVideoBinding
import com.onerainbow.module.seek.viewmodel.FinishSeekViewModel

/**
 * description ： 视频搜索
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 11:17
 */
class VideoFragment : BaseFragment<FragmentVideoBinding>() {
    private var keyword: String? = null
    private val finishSeekViewmodel: FinishSeekViewModel by lazy {
        ViewModelProvider(requireActivity())[FinishSeekViewModel::class.java]
    }
    private val getMvDataAdapter: GetMvDataAdapter by lazy {
        GetMvDataAdapter()
    }
    override fun getViewBinding(): FragmentVideoBinding =
        FragmentVideoBinding.inflate(layoutInflater)

    override fun initEvent() {
        initView()

        keyword?.let {
            finishSeekViewmodel.getGetMv(it)
        }
    }

    private fun initView() {
        binding.mvRecycleview.apply {
            adapter = getMvDataAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyword = arguments?.getString("keyword") ?: ""


    }

    override fun observeData() {

        finishSeekViewmodel.getMvDataLiveData.observe(viewLifecycleOwner) { result ->
            getMvDataAdapter.submitList(result.result.mvs)
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