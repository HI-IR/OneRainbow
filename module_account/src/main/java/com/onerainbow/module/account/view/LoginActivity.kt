package com.onerainbow.module.account.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.lifecycle.ViewModelProvider
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.account.viewmodel.AccountViewModel
import com.onerainbow.module.module.account.databinding.ActivityLoginBinding
import com.therouter.TheRouter
import com.therouter.router.Route
import kotlin.math.max

@Route(path = RoutePath.LOGIN)
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    //是否在动画状态下
    private var isAnimator = false
    private var currentAnimatorSet: AnimatorSet? = null

    private val viewModel by lazy {
        ViewModelProvider(this)[AccountViewModel::class.java]
    }

    override fun getViewBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun observeData() {
        viewModel.apply {
            result.observe(this@LoginActivity) {
                TheRouter.build(RoutePath.HOME).navigation()
            }

            error.observe(this@LoginActivity) {
                ToastUtils.makeText(it)
            }
        }
    }

    override fun initEvent() {
        binding.apply {
            btnLoginLogin.setOnClickListener {
                if (isAnimator) return@setOnClickListener
                val username = etLoginUsername.text.toString()
                val password = etLoginPassword.text.toString()
                if (username.isBlank()) {
                    etLoginUsername.error = "用户名不能为空"
                    return@setOnClickListener
                }

                if (password.isBlank()) {
                    etLoginUsername.error = "密码不能为空"
                    return@setOnClickListener
                }
                if (btnLoginLogin.text.toString() == "登录"){
                    viewModel.doLogin(username, password)
                }else{
                    viewModel.doRegistered(username, password)
                }
            }

            tvLoginLogin.setOnClickListener {
                //显示注册界面
                pageChange("registered")

            }

            tvLoginRegistered.setOnClickListener {
                //显示登录界面
                pageChange("login")

            }
        }


    }

    /**
     * 登录页、注册页变化的动画
     * @param type 改变的类型
     * 可选："login" 前往login界面
     *      "registered" 前往registered界面
     */
    fun pageChange(type: String) {
        if (isAnimator) return
        isAnimator = true
        cancelCurrentAnimation()

        val btn = binding.btnLoginLogin //按钮
        val tvLogin = binding.tvLoginLogin//登录页显示的文字
        val tvRegister = binding.tvLoginRegistered//注册页现实的文字

        val targetWidth = btn.width - 50

        var anim1: Animator? = null
        var anim2: Animator? = null
        when (type) {
            "login" -> {
                tvRegister.visibility = View.GONE
                tvLogin.visibility = View.VISIBLE

                //隐藏第一个按钮
                anim1 = ValueAnimator.ofFloat(0f, 1f).apply {
                    duration = 350L
                    interpolator = DecelerateInterpolator()
                    doOnStart {
                        btn.text = ""
                    }

                    addUpdateListener {
                        val fraction = animatedFraction
                        btn.layoutParams.width = (targetWidth * (1 - fraction)).toInt() + 50
                        btn.requestLayout()
                    }

                }
                //显示第二个按钮
                anim2 = ValueAnimator.ofFloat(0f, 1f).apply {
                    duration = 350L
                    interpolator = DecelerateInterpolator()

                    addUpdateListener {
                        val fraction = animatedFraction
                        btn.layoutParams.width = (targetWidth * fraction).toInt() + 50
                        btn.requestLayout()
                    }
                    doOnEnd {
                        btn.text = "登录"
                    }
                }
            }

            "registered" -> {
                tvRegister.visibility = View.VISIBLE
                tvLogin.visibility = View.GONE
                //隐藏第一个按钮
                anim1 = ValueAnimator.ofFloat(0f, 1f).apply {
                    duration = 350L
                    interpolator = DecelerateInterpolator()
                    doOnStart {
                        btn.text = ""
                    }
                    addUpdateListener {
                        val fraction = animatedFraction
                        btn.layoutParams.width = (targetWidth * (1 - fraction)).toInt() + 50
                        btn.requestLayout()
                    }
                }
                //显示第二个按钮
                anim2 = ValueAnimator.ofFloat(0f, 1f).apply {
                    duration = 350L
                    interpolator = DecelerateInterpolator()
                    addUpdateListener {
                        val fraction = animatedFraction
                        btn.layoutParams.width = (targetWidth * fraction).toInt() + 50
                        btn.requestLayout()
                    }
                    doOnEnd {
                        btn.text = "注册"
                    }
                }

            }
        }
        if (anim1 == null || anim2 == null) {
            isAnimator = false
            return
        }
        currentAnimatorSet = AnimatorSet().apply {
            playSequentially(anim1, anim2)
            addListener(
                object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        currentAnimatorSet = null
                        isAnimator = false
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        currentAnimatorSet = null
                        isAnimator = false
                    }

                    override fun onAnimationRepeat(animation: Animator) {}
                }
            )
            start()
        }
    }

    // 取消当前动画
    fun cancelCurrentAnimation() {
        currentAnimatorSet?.let {
            if (it.isRunning) {
                it.cancel() // 取消动画
            }
            it.removeAllListeners() // 移除所有监听器
            currentAnimatorSet = null // 清空引用
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelCurrentAnimation()
    }
}
