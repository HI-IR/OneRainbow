package com.onerainbow.module.seek.adapter

/**
 * description ： 榜单适配器
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/16 12:04
 */
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onerainbow.module.seek.data.Tracks
import com.onerainbow.module.seek.databinding.ItemPopmusicBinding

class PopmusicAdapter(
    private val onItemClick: ((Tracks) -> Unit)? = null // 外部传点击回调
) : ListAdapter<Tracks, PopmusicAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(val binding: ItemPopmusicBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Tracks, position: Int) {
            // 设置内容
            binding.tvHotTitleOne.text = (position + 1).toString()
            binding.tvHotmusicName.text = item.name

            // 点击事件
            binding.root.setOnClickListener {
                onItemClick?.invoke(item) // 调用外部传入的点击逻辑
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPopmusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Tracks>() {
            override fun areItemsTheSame(oldItem: Tracks, newItem: Tracks): Boolean {
                // 这里判断唯一标识，例如 searchWord 唯一
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Tracks, newItem: Tracks): Boolean {
                // 判断内容是否相同
                return oldItem == newItem
            }
        }
    }
}
