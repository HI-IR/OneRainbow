package com.onerainbow.module.seek.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onerainbow.module.musicplayer.R
import com.onerainbow.module.musicplayer.databinding.ActivityCommentBinding
import com.onerainbow.module.musicplayer.databinding.ItemCommentBinding
import com.onerainbow.module.seek.data.Comment
import com.onerainbow.module.seek.databinding.ItemCommentsBinding


/**
 * description ： 评论的Adapter
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/19 11:58
 */
class CommentAdapter_mv(private val context: Context) : PagingDataAdapter<Comment, CommentAdapter_mv.ViewHolder>(
    object : DiffUtil.ItemCallback<Comment>(){
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }

    }
) {
    //加载图片
    val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.empty)
        .fallback(R.drawable.empty)
    inner class ViewHolder(val binding: ItemCommentsBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(data: Comment){

            binding.apply {
                Glide.with(context).load(data.user.avatarUrl).apply(requestOptions).into(imgAvatar)
                data.user.vipIconUrl?.let {
                    Glide.with(context).load(data.user.vipIconUrl).apply(requestOptions).into(imgViprank)
                }
                tvTimestr.text = data.timeStr
                tvContent.text = data.content
                tvLikeCount.text = data.likedCount.toString()
                tvNickname.text = data.user.nickname
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCommentsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


}