package com.example.module.seek

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.module.seek.databinding.ActivityViedoBinding
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.route.RoutePath
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.therouter.router.Route

@Route(path = RoutePath.MV)
class ViedoActivity : BaseActivity<ActivityViedoBinding>() {
    private var orientationUtils: OrientationUtils? = null
    override fun getViewBinding(): ActivityViedoBinding =
        ActivityViedoBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置播放资源
        val videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4" // 网络视频链接
        binding.videoPlayer.setUp(videoUrl, true, "示例视频")
        // 开始播放
        binding.videoPlayer.startPlayLogic()
        orientationUtils = OrientationUtils(this, binding.videoPlayer)
        orientationUtils?.isEnable = true
    }

    override fun initViewModel() {

    }

    override fun initEvent() {
        binding.videoPlayer.setRotateViewAuto(true)
        binding.videoPlayer.setLockLand(true)
        binding.videoPlayer.setShowFullAnimation(true)
        binding.videoPlayer.fullscreenButton.setOnClickListener {
            // 1️⃣ 先强制横屏
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            // 2️⃣ 然后进入全屏窗口
            binding.videoPlayer.startWindowFullscreen(
                this, // context
                true, // 是否需要动画
                true  // 是否支持锁屏
            )
        }


    }

    override fun onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            // 退出全屏时恢复竖屏
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            return
        }
        super.onBackPressed()
    }


    override fun onPause() {
        super.onPause()
        // 页面暂停时暂停视频
        binding.videoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        // 页面回到前台时恢复播放
        binding.videoPlayer.onVideoResume()
    }


    override fun onDestroy() {
        super.onDestroy()
        orientationUtils?.releaseListener()
        orientationUtils = null

        // 释放全局播放器，防止内存泄漏
        GSYVideoManager.releaseAllVideos()

        // 释放当前播放器
        binding.videoPlayer.release()

    }
}