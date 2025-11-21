package com.onerainbow.module.seek.Fragment

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.onerainbow.module.seek.adapter.SingleAdapter
import com.onerainbow.module.seek.databinding.FragmentSingleBinding
import com.onerainbow.module.seek.interfaces.GetImgUrl
import com.onerainbow.module.seek.viewmodel.FinishSeekViewModel
import com.onerainbow.lib.base.BaseFragment

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 11:15
 */
class SingleFragment :BaseFragment<FragmentSingleBinding>(),GetImgUrl {
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
        singlerAdapter = SingleAdapter(this)
        binding.singleRecycleview.layoutManager=LinearLayoutManager(context)
        binding.singleRecycleview.adapter=singlerAdapter
        Log.d("keywordone",keyword.toString())
       keyword?.let {
          finishSeekViewmodel.getSingle(it)
       }
    }

    override fun observeData() {
        finishSeekViewmodel.singleDataLiveData.observe(viewLifecycleOwner){
                result ->

            singlerAdapter.submitList(result.result.songs)

        }


    }

    override fun getGetImgUrl(id: Long, callback: (String) -> Unit) {
        // 每次点击先取消之前的请求（防止连点）
        finishSeekViewmodel.cancelUrlRequest()

        // 调用 ViewModel 获取数据
        finishSeekViewmodel.getUrlData(
            id,
            onResult = { result ->
                val url = result.songs[0].al.picUrl
                Log.d("SingleFragment", "图片 URL: $url")
                callback(url)
            },
            onError = { error ->
                Log.e("SingleFragment", "获取图片 URL 失败", error)
                callback("") // 失败时回调空字符串或默认图
            }
        )
    }

}