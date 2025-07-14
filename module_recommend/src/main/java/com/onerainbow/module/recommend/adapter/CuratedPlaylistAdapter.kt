package com.onerainbow.module.recommend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onerainbow.module.recommend.R
import com.onerainbow.module.recommend.bean.Curated
import com.onerainbow.module.recommend.databinding.ItemCuratedBinding

/**
 * description ： 甄选歌单的Adapter
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 19:38
 */
class CuratedPlaylistAdapter(val context: Context) : ListAdapter<Curated, CuratedPlaylistAdapter.ViewModel>(object :
    DiffUtil.ItemCallback<Curated>() {
    override fun areItemsTheSame(oldItem: Curated, newItem: Curated): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Curated, newItem: Curated): Boolean {
        return oldItem == newItem
    }

}) {
    inner class ViewModel(binding: ItemCuratedBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imgPic = binding.imgPic
        private val tvName = binding.tvName
        private val item = binding.root
        init {
            initClick()
        }
        fun bind(data: Curated){
            tvName.text = data.name

            //加载网络图片
            val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.loading)
                .fallback(R.drawable.loading)
            Glide.with(context).load(data.picUrl).apply(requestOptions).into(imgPic)
        }

        private fun initClick() {
            item.setOnClickListener {
                //TODO 歌曲列表
                Toast.makeText(context,"点击了歌单${tvName.text}，${getItem(adapterPosition).id}",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        return ViewModel(
            ItemCuratedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        holder.bind(getItem(position))
    }
}