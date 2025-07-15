package com.onerainbow.module.recommend.anim

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * description ： 网友精选页面的翻页动画
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/15 16:11
 */
class CommunityPicksAnim: ViewPager2.PageTransformer {
    private val mScale: Float = 0.85f
    private val mAlpha: Float = 0.8f
    override fun transformPage(page: View, position: Float) {
        val absPos = abs(position)
        // 缩放
        val scale = mScale + (1 - mScale) * (1-absPos)//目前的缩放比例
        page.scaleY = scale
        page.scaleX = scale

        // 透明度
        page.alpha = mAlpha + (1 - absPos) * (1-absPos)

        // 位置偏移
        page.translationX = -position * page.width * 0.125f
        page.translationY = -position * page.height * 0.08f
    }
}