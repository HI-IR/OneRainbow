package com.onerainbow.module.recommend.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.math.abs

/**
 * description ： 解决滑动冲突
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 16:37
 */
class MySwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
): SwipeRefreshLayout(context, attrs) {
    private var startX = 0f
    private var startY = 0f
    val touchSlop = ViewConfiguration.get(context).scaledTouchSlop //获取阈值


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when(ev!!.actionMasked){
            MotionEvent.ACTION_DOWN ->{
                startX = ev.x
                startY = ev.y
            }
            MotionEvent.ACTION_MOVE ->{
                val dx = ev.x - startX
                val dy = ev.y - startY
                if (abs(dx) > abs(dy) * 1.25 && abs(dx) >= touchSlop){
                    //水平滑动
                    return false
                }
            }

            MotionEvent.ACTION_CANCEL,MotionEvent.ACTION_UP -> {
                  startX = 0f
                  startY = 0f
            }

        }

        return super.onInterceptTouchEvent(ev)
    }

}