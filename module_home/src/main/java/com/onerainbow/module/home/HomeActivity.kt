package com.onerainbow.module.home

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.home.databinding.ActivityHomeBinding
import com.therouter.router.Route

@Route(path = RoutePath.HOME)
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override fun getViewBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun initViewModel() {

    }

    override fun initEvent() {
        binding.apply {
            binding.toolbarHome.setOnClickListener{
                drawerlayoutHome.openDrawer(Gravity.START)
            }
        }
    }
}