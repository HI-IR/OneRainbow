package com.onerainbow.module.account

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.module.account.R
import com.example.module.account.databinding.ActivityLoginBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.account.adapter.FragmentAdapter
import com.onerainbow.module.account.viewmodel.LoginViewModel
import com.therouter.router.Route

@Route(path = RoutePath.LOGIN)
class LoginActivity : BaseActivity<ActivityLoginBinding>() {


    private lateinit var fragmentAdapter: FragmentAdapter

    override fun getViewBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViewPager()

    }

    private fun setupViewPager() {
        fragmentAdapter = FragmentAdapter(this)
        binding.viewPager.adapter = fragmentAdapter
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.offscreenPageLimit = fragmentAdapter.itemCount
        binding.bottomNavLogin.itemIconTintList = null
        binding.bottomNavLogin.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_captcha -> binding.viewPager.currentItem = 0
                R.id.navigtion_username -> binding.viewPager.currentItem = 1
            }
            true
        }
        // 监听软键盘弹出与收起
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = binding.root.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            // 判断键盘是否弹出，阈值可根据实际调整
            val isKeyboardVisible = keypadHeight > screenHeight * 0.15

            if (isKeyboardVisible) {
                binding.bottomNavLogin.visibility = View.GONE
            } else {
                binding.bottomNavLogin.visibility = View.VISIBLE
            }
        }

    }


    override fun initViewModel() {
        // 只 observe 一次，防止重复调用
    }

    override fun initEvent() {

    }
}
