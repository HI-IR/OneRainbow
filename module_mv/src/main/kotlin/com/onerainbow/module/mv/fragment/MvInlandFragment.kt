package com.onerainbow.module.mv.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.module.mv.Artist
import com.onerainbow.module.mv.MvData
import com.onerainbow.module.mv.MvViewModel
import com.onerainbow.module.mv.MvsData
import com.onerainbow.module.mv.databinding.FragmentMvinlandBinding
import com.onerainbow.module.seek.adapter.GetMvDataAdapter
import com.onerainbow.module.seek.data.ArtistX
import com.onerainbow.module.seek.data.Mv
import com.onerainbow.module.seek.data.ResultGetMv

/**
 * description ： 内地视频
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/21 14:31
 */
class MvInlandFragment :BaseFragment<FragmentMvinlandBinding>() {
    private val mvViewModel : MvViewModel by lazy {  ViewModelProvider(requireActivity())[MvViewModel::class.java] }
    private val getMvDataAdapter :GetMvDataAdapter by lazy {
        GetMvDataAdapter()
    }
    override fun getViewBinding(): FragmentMvinlandBinding=FragmentMvinlandBinding.inflate(layoutInflater)

    override fun initEvent() {
        initView()
        initData()
    }

    private fun initData() {
        mvViewModel.getmvinland()

    }

    private fun initView() {
        binding.mvInlandRecycleview.apply {
            adapter = getMvDataAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun observeData() {
        mvViewModel.mvinlandLiveData.observe(viewLifecycleOwner){
                result ->
            getMvDataAdapter.submitList(result.toResultGetMv().mvs)
        }
    }
    fun MvsData.toResultGetMv(): ResultGetMv {
        return ResultGetMv(
            mvs = this.data.map { it.toMv() }
        )
    }

    fun MvData.toMv(): Mv {
        return Mv(
            artists = this.artists.map { it.toArtistX() },
            cover = this.cover,
            duration = this.duration.toLong(), // Int -> Long
            id = this.id,
            name = this.name,
            playCount = this.playCount
        )
    }

    fun Artist.toArtistX(): ArtistX {
        return ArtistX(
            briefDesc = "", // 没有对应字段，先留空
            img1v1Url = "", // 没有对应字段，先留空
            id = this.id,
            name = this.name
        )
    }


}