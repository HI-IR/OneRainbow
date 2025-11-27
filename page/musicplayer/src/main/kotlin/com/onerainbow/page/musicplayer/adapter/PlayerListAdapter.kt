package com.onerainbow.page.musicplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onerainbow.page.musicplayer.api.bean.Song
import com.onerainbow.page.musicplayer.databinding.ItemPlayerlistSongBinding
import com.onerainbow.page.musicplayer.service.musicManager

/**
 * description ： 歌曲播放列表的Adapter
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/17 15:04
 */
class PlayerListAdapter : ListAdapter<Song, PlayerListAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Song>() {
	override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
		return oldItem.id == newItem.id
	}

	override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
		return oldItem == newItem
	}

}) {
	companion object {
		private const val PAYLOAD_SELECTION = "PAYLOAD_SELECTION"
		private val HIGHLIGHT_COLOR = "#d81e06".toColorInt()
		private val DEFAULT_COLOR = "#17182C".toColorInt()
	}

	var listener: ((Int)-> Unit)? = null

	var selectionPosition = -1

	// 外部调用切换选中项（只刷新旧/新两项）
	fun updateSelection(newPos: Int) {
		if (newPos < 0 || newPos >= itemCount) return
		val old = selectionPosition
		if (old == newPos) return
		selectionPosition = newPos
		if (old != -1) {
			notifyItemChanged(old, PAYLOAD_SELECTION)
		}
		notifyItemChanged(newPos, PAYLOAD_SELECTION)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			ItemPlayerlistSongBinding.inflate(LayoutInflater.from(parent.context)
				, parent, false)
		)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any?>) {
		if (payloads.isEmpty()) {
			holder.bind(getItem(position), position)
		} else {
			// 仅处理选中态 payload
			if (payloads.any { it == PAYLOAD_SELECTION }) {
				holder.updateSelectionUI(position == selectionPosition)
			} else {
				holder.bind(getItem(position), position)
			}
		}

	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position), position)
	}


	//设置点击事件
	fun setOnItemClickListener(listener: (Int) -> Unit){
		this.listener = listener
	}


	inner class ViewHolder(val binding: ItemPlayerlistSongBinding) :
		RecyclerView.ViewHolder(binding.root) {
		init {
			initClick()
		}

		fun bind(data: Song, position: Int) {
			with(binding) {
				//绑定数据
				playerlistItemName.text = data.name
				playerlistItemCreator.text =
					data.artists.joinToString(separator = "/") { it.name } //作者用/连接

				//渲染颜色
				if (position == selectionPosition) {
					playerlistItemName.setTextColor(HIGHLIGHT_COLOR)
					playerlistItemCreator.setTextColor(HIGHLIGHT_COLOR)
				} else {
					playerlistItemName.setTextColor(DEFAULT_COLOR)
					playerlistItemCreator.setTextColor(DEFAULT_COLOR)
				}
			}
		}

		fun updateSelectionUI(isSelected: Boolean) = with(binding) {
			val color = if (isSelected) HIGHLIGHT_COLOR else DEFAULT_COLOR
			playerlistItemName.setTextColor(color)
			playerlistItemCreator.setTextColor(color)
		}

		private fun initClick() {
			with(binding) {
				root.setOnClickListener {
					listener?.invoke(bindingAdapterPosition)
				}
				playerlistDelete.setOnClickListener {
					musicManager.removeSongAt(bindingAdapterPosition)
				}
			}
		}
	}
}