package com.onerainbow.module.musicplayer.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.onerainbow.module.musicplayer.adapter.PlayerListAdapter
import com.onerainbow.module.musicplayer.databinding.DialogPlayerlListBinding
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.service.MusicManager

/**
 * description ： 播放列表的Dialog
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/17 14:18
 */
class PlayerList(
    context: Context,
    private val onSongSelected: (Int) -> Unit, // 歌曲选择回调
) : BottomSheetDialog(context) {

    private val binding by lazy {
        DialogPlayerlListBinding.inflate(layoutInflater)
    }

    private val playerListAdapter by lazy {
        PlayerListAdapter {
            onSongSelected(it)//将点击事件传入
            dismiss()//关闭BottomSheetDialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 设置点击外部区域关闭 Dialog
        setCanceledOnTouchOutside(true)

        initView()
        initClick()
    }

    private fun initClick() {
        binding.apply {
            playerlistClean.setOnClickListener {
                MusicManager.cleanList()
                dismiss()
            }
        }
    }

    private fun initView() {
        // 配置 RecyclerView
        binding.playerlistRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playerListAdapter
            isNestedScrollingEnabled = true
        }
        // 配置 BottomSheet 行为
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            //行为控制器
            BottomSheetBehavior.from(it).apply {
                //折叠高度
                peekHeight = 1500
                //初始状态
                state = BottomSheetBehavior.STATE_COLLAPSED
                isDraggable = true
            }
        }
    }


    // 设置歌单数据
    fun setSongs(songs: List<Song>) {
        playerListAdapter.submitList(songs)
    }


}