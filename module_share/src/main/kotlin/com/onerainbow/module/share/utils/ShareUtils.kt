package com.onerainbow.module.share.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * description ： 关于qq分享的工具
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 13:44
 */
object ShareUtils {

    /**
     * 分享到QQ
     * @param message 要分享的消息
     */
    fun shareToQQ(context: Context, message: String){
        if (message !== ""){
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.setPackage("com.tencent.mobileqq")

            try {
                Toast.makeText(context, "QQ正在启动中！", Toast.LENGTH_SHORT).show()
                context.startActivity(intent)
            } catch (e: Exception) {
                // 处理QQ未安装或其他异常情况
                Toast.makeText(context ,e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 分享到微信
     * @param message 分享的消息
     */
    fun shareToWX(context: Context, message: String){
        if (message !== ""){
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.setPackage("com.tencent.mm")

            try {
                Toast.makeText(context, "微信正在启动中！", Toast.LENGTH_SHORT).show()
                context.startActivity(intent)
            } catch (e: Exception) {
                // 处理微信未安装或其他异常情况
                Toast.makeText(context ,e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 分享到浏览器
     * @param message 分享的消息
     */
    fun shareToBrowser(context: Context, message: String){
        try {
            val uri = Uri.parse(message)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }catch (e:Exception){
            // 处理浏览器未安装或其他异常情况
            Toast.makeText(context ,e.message, Toast.LENGTH_SHORT).show()
        }
    }
}