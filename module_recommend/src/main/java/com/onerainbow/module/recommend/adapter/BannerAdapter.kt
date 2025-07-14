package com.onerainbow.module.recommend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onerainbow.module.recommend.R
import com.onerainbow.module.recommend.bean.Banner
import com.onerainbow.module.recommend.databinding.FragmentBannerBinding
import com.therouter.TheRouter

/**
 * description ： 推荐页的轮播图
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 13:22
 */
class BannerAdapter(private val context: Context): ListAdapter<Banner,BannerAdapter.ViewHolder>(object :DiffUtil.ItemCallback<Banner>(){
    override fun areItemsTheSame(oldItem: Banner, newItem: Banner): Boolean {
        return oldItem.bannerId == newItem.bannerId
    }

    override fun areContentsTheSame(oldItem: Banner, newItem: Banner): Boolean {
        return oldItem == newItem
    }
}){

    inner class ViewHolder(binding: FragmentBannerBinding): RecyclerView.ViewHolder(binding.root){
        private val imageBanner = binding.imgBanner
        private val itemBanner = binding.itemBanner
        private val textBanner = binding.tvBanner

        init {
            initClick()
        }

        private fun initClick() {
            itemBanner.setOnClickListener{
                val item = getItem(adapterPosition)
                if (!item.url.isNullOrBlank()) {
                    TheRouter.build("/recommend/web")
                        .withString("url", item.url)
                        .navigation()
                }
            }
        }

        fun bind(data: Banner){
            //加载网络图片
            val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.banner_example)
                .fallback(R.drawable.banner_example)
            Glide.with(context).load(data.pic).apply(requestOptions).into(imageBanner)

            textBanner.text = data.typeTitle
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentBannerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    //无限轮播
    override fun getItemCount(): Int = if (currentList.isEmpty()) 0 else Int.MAX_VALUE

    //取余currentList.size,获得最准确的Item，实现无限轮播图效果
    override fun getItem(position: Int): Banner {
        return super.getItem(position % currentList.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}