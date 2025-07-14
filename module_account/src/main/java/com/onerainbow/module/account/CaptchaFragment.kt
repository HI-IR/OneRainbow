package com.onerainbow.module.account

import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener

import androidx.lifecycle.ViewModelProvider
import com.example.module.account.databinding.FragmentCaptchaBinding
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.account.data.CaptchaDataResult
import com.onerainbow.module.account.viewmodel.LoginViewModel
import com.therouter.TheRouter
import com.therouter.getApplicationContext
import com.therouter.router.Route

/**
 * description ： 验证码登录
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/14 10:48
 */
@Route(path = RoutePath.LOGIN_PHONE)
class CaptchaFragment : BaseFragment<FragmentCaptchaBinding>() {
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun getViewBinding(): FragmentCaptchaBinding = FragmentCaptchaBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun initEvent() {
        binding.inPhone.addTextChangedListener {
            if (it?.length == 15) {
                Toast.makeText(requireContext(), "最多输入15位", Toast.LENGTH_SHORT).show()
            }
        }
        binding.button.setOnClickListener {

            val phone = binding.inPhone.text.toString()

            if (!binding.checkbox.isChecked) {
                Toast.makeText(requireContext(), "请勾选协议", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPhone(phone)) {
                Toast.makeText(requireContext(), "请输入有效的手机号", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 两个条件都通过，执行登录请求
            viewModel.getCaptchaResult(phone)
        }

    }




    override fun initViewModel() {
        viewModel.result.observe(viewLifecycleOwner) { result ->
            when (result) {
                is CaptchaDataResult.Success -> {
                    TheRouter.build(RoutePath.LOGIN_CAPTCHA)
                        .withString("phone",binding.inPhone.text.toString())
                        .navigation()

                }
                is CaptchaDataResult.Error -> {
                    Toast.makeText(requireContext(), "发送验证码失败", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
    fun isValidPhone(phone: String): Boolean {
        val regex = Regex("^1[3-9]\\d{9}$")
        return regex.matches(phone)
    }



}