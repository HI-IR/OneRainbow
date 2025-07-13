package com.onerainbow.module.account

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.TypedValue
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.module.account.R
import com.example.module.account.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputEditText
import com.onerainbow.lib.base.BaseActivity
import java.lang.reflect.Field

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override fun getViewBinding(): ActivityLoginBinding =ActivityLoginBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun initViewModel() {

    }

    override fun initEvent() {

    }
}