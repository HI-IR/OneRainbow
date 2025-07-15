package com.onerainbow.onerainbow

import android.os.Bundle
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.onerainbow.databinding.ActivityMainBinding

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