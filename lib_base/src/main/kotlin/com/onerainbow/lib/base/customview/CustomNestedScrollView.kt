package com.onerainbow.lib.base.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.core.widget.NestedScrollView
import kotlin.math.abs

/**
 * description ： 解决滑动冲突版的NestedScrollView
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/22 10:53
 */
class CustomNestedScrollView @JvmOverloads constructor(context: Context,
    attrs: AttributeSet? =null,
    defStyleAttr : Int = 0
    ):NestedScrollView(context, attrs, defStyleAttr) {
    private var startX = 0f
    private var startY = 0f
    val touchSlop = ViewConfiguration.get(context).scaledTouchSlop //获取阈值


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when(ev.actionMasked){
            MotionEvent.ACTION_DOWN ->{
                startX = ev.x
                startY = ev.y
                return super.onInterceptTouchEvent(ev)
            }
            MotionEvent.ACTION_MOVE ->{
                val dx = ev.x - startX
                val dy = ev.y - startY
                if (abs(dx) > abs(dy) * 0.5 && abs(dx) >= touchSlop){
                    //水平滑动
                    Log.d("conflict", "dx -> ${dx},dy -> ${dy}")
                    return false
                }
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                startX = 0f
                startY = 0f
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}