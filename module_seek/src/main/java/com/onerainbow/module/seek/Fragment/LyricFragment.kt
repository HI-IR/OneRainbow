package com.onerainbow.module.seek.Fragment

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.onerainbow.module.seek.adapter.LyricDataAdapter
import com.onerainbow.module.seek.adapter.SingleAdapter
import com.onerainbow.module.seek.databinding.FragmentLyricBinding
import com.onerainbow.module.seek.interfaces.GetImgUrl
import com.onerainbow.module.seek.viewmodel.FinishSeekViewModel
import com.onerainbow.lib.base.BaseFragment

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 11:18
 */
class LyricFragment :BaseFragment<FragmentLyricBinding>(),GetImgUrl {
    private var keyword: String? = null
    private lateinit var lyricAdapter: LyricDataAdapter
    private val finishSeekViewmodel: FinishSeekViewModel by lazy {
        ViewModelProvider(requireActivity())[FinishSeekViewModel::class.java]
    }
    override fun getViewBinding(): FragmentLyricBinding =FragmentLyricBinding.inflate(layoutInflater)
    override fun initEvent() {
        lyricAdapter = LyricDataAdapter(this)
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