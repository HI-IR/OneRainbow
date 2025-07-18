package com.example.module.seek.Fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module.seek.adapter.SingleAdapter
import com.example.module.seek.adapter.SpecialDataAdapter
import com.example.module.seek.databinding.FragmentSpecialBinding
import com.example.module.seek.viewmodel.FinishSeekViewModel
import com.onerainbow.lib.base.BaseFragment

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 11:16
 */
class SpecialFragment:BaseFragment<FragmentSpecialBinding>() {
    private var keyword: String? = null
    private val finishSeekViewmodel: FinishSeekViewModel by lazy {
        ViewModelProvider(requireActivity())[FinishSeekViewModel::class.java]
    }
    private lateinit var specialDataAdapter: SpecialDataAdapter

    override fun getViewBinding(): FragmentSpecialBinding =FragmentSpecialBinding.inflate(layoutInflater)

    override fun initEvent() {
        specialDataAdapter =SpecialDataAdapter()
        binding.specialRecycleview.layoutManager=LinearLayoutManager(context)
        binding.specialRecycleview.adapter = specialDataAdapter
        keyword?.let {
            finishSeekViewmodel.getSpecial(it)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyword = arguments?.getString("keyword") ?: ""



    }
    override fun initViewModel() {
        finishSeekViewmodel.SpecialDataLiveData.observe(viewLifecycleOwner){
            result ->
            specialDataAdapter.submitList(result.result.albums)
        }

    }
    companion object {
        fun newInstance(keyword: String?): SpecialFragment {
            val fragment = SpecialFragment()
            fragment.arguments = Bundle().apply {
                putString("keyword", keyword)
            }
            return fragment
        }
    }
}