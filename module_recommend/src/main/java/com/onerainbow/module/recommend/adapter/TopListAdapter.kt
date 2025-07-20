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
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.recommend.R
import com.onerainbow.module.recommend.bean.Creator
import com.onerainbow.module.recommend.bean.Creator2
import com.onerainbow.module.recommend.bean.Playlist
import com.onerainbow.module.recommend.bean.Playlists
import com.onerainbow.module.recommend.databinding.ItemToplistBinding
import com.therouter.TheRouter

/**
 * description ： 推荐页的热榜适配器
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/15 20:03
 */
class TopListAdapter(private val context: Context) :
    ListAdapter<Playlist, TopListAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<Playlist>() {
            override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                return oldItem == newItem
            }

        }
    ) {
    val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.loading)
        .fallback(R.drawable.loading)

    inner class ViewHolder(binding: ItemToplistBinding) : RecyclerView.ViewHolder(binding.root) {
        private var currentData: Playlist? = null
        private val tvName = binding.tvName
        private val imgCover = binding.imgCover
        private val tvNo1Name = binding.tvNo1Name
        private val tvNo2Name = binding.tvNo2Name
        private val tvNo3Name = binding.tvNo3Name
        private val tvNo1Creator = binding.tvNo1Creator
        private val tvNo2Creator = binding.tvNo2Creator
        private val tvNo3Creator = binding.tvNo3Creator
        private val item = binding.root

        init {
            initClick()
        }

        private fun initClick() {
            item.setOnClickListener {
                currentData?.let { playlist ->
                    // 这里调用转换方法
                    val simplePlaylist = playlist.toSimple()
                    TheRouter.build(RoutePath.PLAYLIST)
                        .withParcelable("playlists", simplePlaylist)
                        .navigation()
                } ?: run {
                    Toast.makeText(context, "数据异常", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun bind(data: Playlist) {
            currentData = data

            val songs = data.tracks.take(3)
            //网路加载图片
            Glide.with(context).load(data.coverImgUrl).apply(requestOptions).into(imgCover)
            tvName.text = data.name
            songs.forEachIndexed { index, track ->
                when (index) {
                    0 -> {
                        tvNo1Name.text = track.name
                        tvNo1Creator.text = "- ${track.ar.firstOrNull()?.name ?: "未知歌手"}"

                    }

                    1 -> {
                        tvNo2Name.text = track.name
                        tvNo2Creator.text = "- ${track.ar.firstOrNull()?.name ?: "未知歌手"}"
                    }

                    2 -> {
                        tvNo3Name.text = track.name
                        tvNo3Creator.text = "- ${track.ar.firstOrNull()?.name ?: "未知歌手"}"
                    }

                }
            }

        }
        fun Playlist.toSimple(): com.onerainbow.module.seek.data.Playlists{
            return com.onerainbow.module.seek.data.Playlists(
                coverImgUrl = this.coverImgUrl,
                creator = this.creator.toSimple(),
                description = this.description,
                id = this.id,
                name = this.name,
                userId = this.userId,
                trackCount = this.trackCount.toInt()
            )
        }
        fun Creator2.toSimple(): com.onerainbow.module.seek.data.Creator {
            return com.onerainbow.module.seek.data.Creator(
                avatarUrl = this.avatarUrl,
                nickname = this.nickname,
                userId = this.userId
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemToplistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}