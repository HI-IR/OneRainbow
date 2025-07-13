package com.onerainbow.onerainbow

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.onerainbow.databinding.ActivityMainBinding
import com.therouter.TheRouter

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initViewModel() {
    }

    override fun initEvent() {
    }
}