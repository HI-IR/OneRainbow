package com.onerainbow.lib.base.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.app.NotificationCompat.MessagingStyle.Message

/**
 * description ： 太棒了是CV工具包我们有救了
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 16:14
 */
object CopyUtils {

    fun copy(context: Context,message:String){
        //获取ClipboardManager实例，剪切板
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建 ClipData 对象
        val clip = ClipData.newPlainText("link", message)
        // 将 ClipData 设置到剪贴板
        clipboard.setPrimaryClip(clip)
    }
}