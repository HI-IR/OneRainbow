package com.onerainbow.module.share

import android.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View.OnClickListener
import android.view.WindowManager
import com.onerainbow.module.share.databinding.CustomShareBinding


/**
 * description ： 自定义的分享弹窗
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 15:34
 */
class CustomShare(context: Context) : Dialog(context) {
    private val binding : CustomShareBinding by lazy{
        CustomShareBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 设置点击外部区域关闭 Dialog
        setCanceledOnTouchOutside(true)


        // 设置 Dialog 的位置和大小
        val window = window
        if (window != null) {
            val params = window.attributes
            params.gravity = Gravity.BOTTOM
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = (context.resources.displayMetrics.heightPixels / 4)//显示屏幕的1/4
            window.attributes = params
            window.setBackgroundDrawableResource(R.color.transparent)
        }


    }
    /**
     * 点击事件QQ
     */
    fun setOnQqClickListener(listener: OnClickListener) {
        binding.shareQq.setOnClickListener(listener)
    }

    /**
     * 点击事件Wx
     */
    fun setOnWxClickListener(listener: OnClickListener) {
        binding.shareWx.setOnClickListener(listener)
    }

    /**
     * 点击事件复制链接
     */
    fun setOnLinkClickListener(listener: OnClickListener) {
        binding.shareLink.setOnClickListener(listener)
    }

    /**
     * 点击事件 浏览器
     */
    fun setOnBrowseClickListener(listener: OnClickListener) {
        binding.shareBrowse.setOnClickListener(listener)
    }

}