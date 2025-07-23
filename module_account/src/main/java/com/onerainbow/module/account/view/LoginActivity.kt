package com.onerainbow.module.account.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.account.viewmodel.AccountViewModel
import com.onerainbow.module.module.account.databinding.ActivityLoginBinding
import com.therouter.TheRouter
import com.therouter.router.Route

@Route(path = RoutePath.LOGIN)
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
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
            result.observe(this@LoginActivity){
                TheRouter.build(RoutePath.HOME).navigation()
            }

            error.observe(this@LoginActivity){
                ToastUtils.makeText(it)
            }
        }
    }

    override fun initEvent() {
        binding.apply {
            btnLoginLogin.setOnClickListener {
                val username = etLoginUsername.text.toString()
                val password = etLoginPassword.text.toString()
                if (username.isBlank()){
                    etLoginUsername.error = "用户名不能为空"
                    return@setOnClickListener
                }

                if(password.isBlank()){
                    etLoginUsername.error = "密码不能为空"
                    return@setOnClickListener
                }
                viewModel.doLogin(username,password)
            }

            btnLoginRegistered.setOnClickListener {
                val username = etLoginUsername.text.toString()
                val password = etLoginPassword.text.toString()
                if (username.isBlank()){
                    etLoginUsername.error = "用户名不能为空"
                    return@setOnClickListener
                }

                if(password.isBlank()){
                    etLoginUsername.error = "密码不能为空"
                    return@setOnClickListener
                }
                viewModel.doRegistered(username,password)
            }

            tvLoginLogin.setOnClickListener{
                //显示注册界面
                btnLoginLogin.visibility = View.GONE
                tvLoginLogin.visibility = View.GONE
                tvLoginRegistered.visibility = View.VISIBLE
                btnLoginRegistered.visibility = View.VISIBLE

            }

            tvLoginRegistered.setOnClickListener{
                //显示登录界面
                btnLoginRegistered.visibility = View.GONE
                tvLoginRegistered.visibility = View. GONE

                btnLoginLogin.visibility = View.VISIBLE
                tvLoginLogin.visibility = View.VISIBLE

            }
        }




    }


}
