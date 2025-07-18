package com.example.module.seek.Fragment

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module.seek.adapter.SingleAdapter
import com.example.module.seek.databinding.FragmentSingleBinding
import com.example.module.seek.viewmodel.FinishSeekViewModel
import com.onerainbow.lib.base.BaseFragment

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 11:15
 */
class SingleFragment :BaseFragment<FragmentSingleBinding>() {
    private val finishSeekViewmodel: FinishSeekViewModel by lazy {
        ViewModelProvider(requireActivity())[FinishSeekViewModel::class.java]
    }

    private lateinit var singlerAdapter :SingleAdapter
    private var keyword: String? = null


    override fun getViewBinding(): FragmentSingleBinding =FragmentSingleBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         keyword = arguments?.getString("keyword") ?: ""



    }
    companion object {
        fun newInstance(keyword: String?): SingleFragment {
            val fragment = SingleFragment()
            fragment.arguments = Bundle().apply {
                putString("keyword", keyword)
            }
            return fragment
        }
    }



    override fun initEvent() {
        singlerAdapter = SingleAdapter()
        binding.singleRecycleview.layoutManager=LinearLayoutManager(context)
        binding.singleRecycleview.adapter=singlerAdapter
        Log.d("keywordone",keyword.toString())
       keyword?.let {
          finishSeekViewmodel.getSingle(it)
       }
    }

    override fun initViewModel() {
        finishSeekViewmodel.singleDataLiveData.observe(viewLifecycleOwner){
                result ->

            singlerAdapter.submitList(result.result.songs)

        }


    }
}