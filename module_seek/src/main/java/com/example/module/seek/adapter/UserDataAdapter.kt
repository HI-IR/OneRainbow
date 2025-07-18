package com.example.module.seek.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.module.seek.R
import com.example.module.seek.data.ArtistUser
import com.example.module.seek.data.UserData
import com.example.module.seek.databinding.ItemPlaylistBinding
import com.example.module.seek.databinding.ItemUserBinding

/**
 * description ： 歌手列表设配器
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 21:17
 */
class UserDataAdapter:ListAdapter<ArtistUser,UserDataAdapter.ViewHodler>(DiffCallback) {
    inner class ViewHodler(val binding: ItemUserBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: ArtistUser){
            binding.userName.text=item.name
            Glide.with(binding.userImg.context)
                .load(item.picUrl)
                .placeholder(R.drawable.ic_avatar_placeholder) // 加载中显示
                .error(R.drawable.ic_avatar_placeholder)       // 加载失败显示
                .fallback(R.drawable.ic_avatar_placeholder)    // url 为空时显示
                .circleCrop() // 裁剪成圆形
                .into(binding.userImg)

            binding.root.setOnClickListener{

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodler {
        val binding =ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHodler(binding)
    }

    override fun onBindViewHolder(holder: ViewHodler, position: Int) {
        holder.bind(getItem(position))

    }
    companion object{
        private val DiffCallback =object :DiffUtil.ItemCallback<ArtistUser>(){
            override fun areItemsTheSame(oldItem: ArtistUser, newItem: ArtistUser): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArtistUser, newItem: ArtistUser): Boolean {
              return oldItem ==newItem
            }

        }

    }
}