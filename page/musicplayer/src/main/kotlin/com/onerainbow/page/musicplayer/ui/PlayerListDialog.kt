package com.onerainbow.page.musicplayer.ui
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.onerainbow.page.musicplayer.R
import com.onerainbow.page.musicplayer.databinding.DialogPlayerlListBinding
import com.onerainbow.page.musicplayer.adapter.PlayerListAdapter
import com.onerainbow.page.musicplayer.api.bean.Song

import com.onerainbow.page.musicplayer.service.musicManager

/**
 * description ： 播放列表的Dialog
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/17 14:18
 */
class PlayerListDialog(
    context: Context,
    private val onSongSelected: (Int) -> Unit, // 歌曲选择回调
) : BottomSheetDialog(context, R.style.BottomSheetDialogTheme) {

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
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        // 设置点击外部区域关闭 Dialog
        setCanceledOnTouchOutside(true)

        initView()
        initClick()
    }

    private fun initClick() {
        binding.apply {
            playerlistClean.setOnClickListener {
                musicManager.cleanList()
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

        //直接获取一遍播放列表
        val currentPlaylist = musicManager.getPlaylist()
        if (currentPlaylist.isNotEmpty()) {
            playerListAdapter.submitList(currentPlaylist)
        }

        //  获取当前播放索引，高亮显示正在播放的歌曲
        val currentIndex = musicManager.getCurrentIndex()
        if (currentIndex != -1) {
            playerListAdapter.selectionPosition = currentIndex
        }

        // 配置 BottomSheet 行为
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let { sheet ->
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenHeight = displayMetrics.heightPixels
            val screenWidth = displayMetrics.widthPixels
            val height = screenHeight / 1.5
            sheet.minimumHeight = height.toInt()
            sheet.minimumWidth = screenWidth
            sheet.setBackgroundResource(R.drawable.background_playerlist)
            BottomSheetBehavior.from(sheet).apply {
                peekHeight = height.toInt()
                state = BottomSheetBehavior.STATE_COLLAPSED
                isDraggable = true
            }
        }
    }


    // 设置歌单数据
    fun setSongs(songs: List<Song>) {
        playerListAdapter.submitList(songs)
    }


		fun setSelectedPosition(position: Int) {
        playerListAdapter.selectionPosition = position
        playerListAdapter.notifyDataSetChanged()
    }

}