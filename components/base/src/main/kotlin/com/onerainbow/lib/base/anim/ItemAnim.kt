package com.onerainbow.lib.base.anim

import android.annotation.SuppressLint
import android.view.MotionEvent
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
@SuppressLint("ClickableViewAccessibility")
fun View.scaleAnim(){

    setOnTouchListener { v, event ->
        when(event.actionMasked){
            MotionEvent.ACTION_DOWN ->{
                animate().scaleX(1.05f).scaleY(1.05f).setDuration(100).start()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }
        }
        false
    }

}