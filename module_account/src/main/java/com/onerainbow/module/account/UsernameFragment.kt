package com.onerainbow.module.account

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.module.account.databinding.FragmentUsernameBinding
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.lib.database.dao.LoginDao
import com.onerainbow.lib.database.entity.LoginUser
import com.onerainbow.lib.database.LoginUserDatabaseClient
import kotlinx.coroutines.launch

/**
 * description ： 密码登录
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/14 10:57
 */

class UsernameFragment :BaseFragment<FragmentUsernameBinding>(){
    private lateinit var userDao: LoginDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDao= LoginUserDatabaseClient.getDatabase(requireContext()).LoginDao()
    }
    override fun getViewBinding(): FragmentUsernameBinding =FragmentUsernameBinding.inflate(layoutInflater)

    override fun initEvent() {
        binding.usernameButton.setOnClickListener {
            lifecycleScope.launch {
                val userId = userDao.insertUser(LoginUser(name =binding.inputUsername.text.toString(), id = 1, password = binding.inputPassword.text.toString()))
                Log.d("RoomDemo", "Inserted user ID: $userId")
            }

        }
    }

    override fun initViewModel() {

    }

}