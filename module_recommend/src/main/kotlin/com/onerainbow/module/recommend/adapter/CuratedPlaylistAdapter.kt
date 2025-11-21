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
import com.onerainbow.lib.base.anim.scaleAnim
import com.onerainbow.module.seek.data.Creator
import com.onerainbow.module.seek.data.Playlists
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.recommend.R
import com.onerainbow.module.recommend.bean.Curated
import com.onerainbow.module.recommend.databinding.ItemCuratedBinding
import com.therouter.TheRouter

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
        private var currentData : Curated? = null
        private val imgPic = binding.imgPic
        private val tvName = binding.tvName
        private val item = binding.root
        private val tvCount = binding.tvCount
        init {
            initClick()
        }
        fun bind(data: Curated){
            currentData =data
            tvName.text = data.name
            tvCount.text = data.playCount.toText()
            //加载网络图片
            val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.loading)
                .fallback(R.drawable.loading)
            Glide.with(context).load(data.picUrl).apply(requestOptions).into(imgPic)



        }
        fun Curated.toPlaylists(): Playlists {
            return Playlists(
                coverImgUrl = this.picUrl,
                creator = Creator(
                    avatarUrl = this.picUrl,
                    nickname = "帅哥",
                    userId = -1
                ),
                description = this.copywriter,
                id = this.id,
                name = this.name,
                userId = -1,
                trackCount = this.trackCount.toInt()
            )
        }

        private fun initClick() {
            item.setOnClickListener {
                val playlist =currentData?.toPlaylists()
                TheRouter.build(RoutePath.PLAYLIST)
                    .withParcelable("playlists",playlist)
                    .navigation()
            }
            item.scaleAnim()
        }


        //将数据转为从多少万
        fun Long.toText(): String {
            return when {
                this >= 100000000 -> "%.1f亿".format(this / 100000000.0).replace(".0", "")
                this >= 10000 -> "%.1f万".format(this / 10000.0).replace(".0", "")
                else -> toString()
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