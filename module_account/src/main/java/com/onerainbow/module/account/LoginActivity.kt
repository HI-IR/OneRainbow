package com.onerainbow.module.account

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.onerainbow.module.module.account.databinding.ActivityLoginBinding
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.base.utils.CookieUtils
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.account.viewmodel.LoginViewModel
import com.therouter.TheRouter
import com.therouter.router.Route
import java.util.Base64

@Route(path = RoutePath.LOGIN)
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private val loginviewmodle: LoginViewModel by lazy {
        LoginViewModel()
    }


    override fun getViewBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun observeData() {
        loginviewmodle.qrDataLiveData.observe(this) { QR ->
            Log.d("ErrorQR", QR.toString())
            val bitmap = base64ToBitmap(QR.data.qrimg)
            binding.loginImageView.setImageBitmap(bitmap)
            loginviewmodle.getState()

            loginviewmodle.stateDataLiveData.observe(this) { stateData ->
                Log.d("stateData", stateData.toString())
                when (stateData.code) {
                    803 -> {
                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                        Log.d("cookiestring", stateData.cookie)
                        finish()
                    }

                    800 -> {
                        binding.tvQRState.text = "二维码已过期"
                    }

                    802 -> {
                        binding.tvQRState.text = "授权中"
                    }

                    else -> {
                        binding.tvQRState.text = "等待扫码"
                    }
                }


            }
        }
    }

    override fun initEvent() {
        binding.loginButton.setOnClickListener {
            loginviewmodle.getQR()
            binding.tvQRState.setText("生成二维码中")
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun base64ToBitmap(base64Str: String): Bitmap {
        val pureBase64 = base64Str.substringAfter("base64,", base64Str)
        val decodedBytes = Base64.getDecoder().decode(pureBase64)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }


}
