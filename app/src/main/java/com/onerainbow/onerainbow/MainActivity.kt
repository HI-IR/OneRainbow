package com.onerainbow.onerainbow

import android.os.Bundle
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.onerainbow.databinding.ActivityMainBinding
import com.therouter.TheRouter
import com.therouter.router.Route

@Route(path = RoutePath.MAIN)
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun observeData() {
    }

    override fun initEvent() {
        TheRouter.build(RoutePath.HOME).navigation(this)
    }
}