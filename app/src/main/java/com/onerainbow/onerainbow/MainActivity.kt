package com.onerainbow.onerainbow

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.lifecycleScope
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.onerainbow.databinding.ActivityMainBinding
import com.therouter.TheRouter
import com.therouter.router.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Route(path = RoutePath.MAIN)
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun observeData() {
    }

    override fun initEvent() {
        playSplashAnimation()
    }

    private fun playSplashAnimation() {
        lifecycleScope.launch {
            delay(2000)
            TheRouter.build(RoutePath.HOME).navigation()
            // 设置启动淡入淡出动画
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()

        }

    }

}
