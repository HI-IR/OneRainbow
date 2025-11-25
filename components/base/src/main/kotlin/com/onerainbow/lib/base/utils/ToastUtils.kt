package com.onerainbow.lib.base.utils

import android.widget.Toast
import com.onerainbow.lib.base.BaseApplication

/**
 * description ： ToastUtils
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/17 15:20
 */
object ToastUtils {
    fun makeText(text:String){
        Toast.makeText(BaseApplication.context,text,Toast.LENGTH_SHORT).show()
    }
}