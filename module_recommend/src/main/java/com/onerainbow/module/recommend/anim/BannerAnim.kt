package com.onerainbow.module.recommend.anim

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * description ： Banner的翻页动画
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/15 10:29
 */
class BannerAnim : ViewPager2.PageTransformer{
    override fun transformPage(page: View, position: Float) {
        // 1. 当页面离开中央超过一个页面宽度，直接隐藏
        if (abs(position) > 1f) {
            page.alpha = 0f
            return
        }

        //控制透明度
        page.alpha = 1f - 0.2f * abs(position)

        //控制缩放
        //中间为1，两边为0.8
        val scale = 1f - 0.2f * abs(position)
        page.scaleX = scale
        page.scaleY = scale

        // 5. 缩放基点设置为中心
        page.pivotX = page.width / 2f
        page.pivotY = page.height / 2f
    }
}