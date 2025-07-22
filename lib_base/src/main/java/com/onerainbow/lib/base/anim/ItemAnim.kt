package com.onerainbow.lib.base.anim

import android.view.View

/**
 * description ： Item被点击稍微放大的动画
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/22 15:24
 */
/**
 * Item点击后缩放的动画
 */
fun View.scale(){
    this.animate()
        .scaleX(1.05f)
        .scaleY(1.05f)
        .setDuration(100)
        .withEndAction{
            this.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(100)
                .start()
        }.start()
}