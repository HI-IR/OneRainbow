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
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.recommend.R
import com.onerainbow.module.recommend.bean.Creator
import com.onerainbow.module.recommend.bean.Playlists
import com.onerainbow.module.recommend.databinding.ItemCommunityPicksBinding
import com.therouter.TheRouter

/**
 * description ： 网友推荐的Adapter
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/15 12:23
 */

class CommunityPicksAdapter(private val context: Context) :ListAdapter<List<Playlists>,CommunityPicksAdapter.ViewHolder>(object : DiffUtil.ItemCallback<List<Playlists>>(){
    override fun areItemsTheSame(oldItem: List<Playlists>, newItem: List<Playlists>): Boolean {
        return oldItem.first().id == newItem.first().id
    }

    override fun areContentsTheSame(oldItem: List<Playlists>, newItem: List<Playlists>): Boolean {
        if (oldItem.size != newItem.size) return false
        for (i in oldItem.indices) {
            if (oldItem[i] != newItem[i]) return false
        }
        return true
    }
}){

    inner class ViewHolder(binding:ItemCommunityPicksBinding):RecyclerView.ViewHolder(binding.root){
        private var currentData : List<Playlists>? = null
        //暂时用列表存储一下View，方便后续的绑定操作
        //该列表用于存储标题：第一名的标题，第二名的标题，第三名的标题，
        val titles = listOf(
            binding.tvTitle1,
            binding.tvTitle2,
            binding.tvTitle3,
        )

        //该列表用于存储描述：第一名的描述，第二名的描述，第三名的描述，
        val descs = listOf(
            binding.tvDesc1,
            binding.tvDesc2,
            binding.tvDesc3,
        )

        //该列表用于存储封面：第一名的封面，第二名的封面，第三名的封面，
        val covers = listOf(
            binding.imgCover1,
            binding.imgCover2,
            binding.imgCover3,
        )

        //该列表用于存储创作者信息的View：第一名的创作者信息，第二名的创作者信息，第三名的创作者信息，
        val creators = listOf(
            binding.tvCreator1,
            binding.tvCreator2,
            binding.tvCreator3,
        )

        //用于存储播放的按钮
        val plays = listOf(
            binding.btnPlay1,
            binding.btnPlay2,
            binding.btnPlay3,
        )

        //用于存储那个item
        val items = listOf(
            binding.item1,
            binding.item2,
            binding.item3,
        )
        init {
            initClick()
        }

        private fun initClick() {
            items.forEachIndexed { index, item ->
                item.setOnClickListener {
                    val playlist = currentData?.getOrNull(index)
                    if (playlist != null) {
                        // 转换成简版 Playlists
                        val simplePlaylist = playlist.toSimple()
                        TheRouter.build(RoutePath.PLAYLIST)
                            .withParcelable("playlists",simplePlaylist)
                            .navigation()

                    }
                }
                item.scaleAnim()
                plays.forEachIndexed { index, play ->
                    play.setOnClickListener {
                        val playlist = currentData?.getOrNull(index)
                        if (playlist != null) {
                            // 转换成简版 Playlists
                            val simplePlaylist = playlist.toSimple()
                            TheRouter.build(RoutePath.PLAYLIST)
                                .withParcelable("playlists",simplePlaylist)
                                .navigation()

                        }
                    }

                }
            }
        }

        fun bind(data : List<Playlists>){
            currentData = data
            val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.loading)
                .fallback(R.drawable.loading)
            //按照Index分别绑定视图数据
            data.forEachIndexed { index, playlists ->
                titles[index].text = playlists.name
                descs[index].text = playlists.description
                //加载网络图片
                Glide.with(context).load(playlists.coverImgUrl).apply(requestOptions).into(covers[index])

                creators[index].text = "-${playlists.creator.nickname}"
            }
        }
        fun Playlists.toSimple(): com.onerainbow.module.seek.data.Playlists{
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
        fun Creator.toSimple(): com.onerainbow.module.seek.data.Creator {
            return com.onerainbow.module.seek.data.Creator(
                avatarUrl = this.avatarUrl,
                nickname = this.nickname,
                userId = this.userId
            )
        }




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCommunityPicksBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}