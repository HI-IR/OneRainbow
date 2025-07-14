package com.onerainbow.module.account

import android.os.Bundle
import android.widget.Toast
import com.example.module.account.databinding.ActivityCaptchBinding
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.database.dao.LoginDao
import com.onerainbow.lib.database.LoginUserDatabaseClient
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.account.data.LoginCaptchaDataResult
import com.onerainbow.module.account.viewmodel.LoginViewModel
import com.therouter.router.Autowired
import com.therouter.router.Route

@Route(path = RoutePath.LOGIN_CAPTCHA)
class CaptchActivity : BaseActivity<ActivityCaptchBinding>() {
    private val viewModel: LoginViewModel by lazy {
        LoginViewModel(application)
    }
    private lateinit var userDao: LoginDao

    override fun getViewBinding(): ActivityCaptchBinding =
        ActivityCaptchBinding.inflate(layoutInflater)

    @Autowired(name = "phone") // key 要和 withString 的一致
    lateinit var phone: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDao=LoginUserDatabaseClient.getDatabase(this).LoginDao()
    }

    override fun initViewModel() {
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginCaptchaDataResult.Success -> {

                }

                is LoginCaptchaDataResult.Error -> {
                    Toast.makeText(this, "验证码错误或失效", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    override fun initEvent() {
        binding.buttonLogin.setOnClickListener {

            val captchaText = binding.inCaptcha.text.toString().trim()
            val captcha = captchaText.toIntOrNull()
            if (captcha == null) {
                Toast.makeText(this, "请输入正确格式的验证码", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 输入是数字，可以调用接口
            viewModel.getLoginCaptchaResult(phone, captcha)
        }


    }
}