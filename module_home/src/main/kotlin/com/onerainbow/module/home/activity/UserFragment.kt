package com.onerainbow.module.home.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onerainbow.lib.base.BaseFragment
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.home.R
import com.onerainbow.module.home.databinding.FragmentUserBinding
import com.onerainbow.module.home.viewmodel.HomeViewModel
import com.onerainbow.module.seek.PlaylistActivity
import com.therouter.TheRouter

/**
 * description ： 用户页的Fragment
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 00:45
 */
class UserFragment : BaseFragment<FragmentUserBinding>() {
    override fun getViewBinding(): FragmentUserBinding = FragmentUserBinding.inflate(layoutInflater)
    private val viewModel by lazy {
        //使用Home页的ViewModel
        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    override fun initEvent() {
        initClick()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 仅 Android 13 及以上版本申请 READ_MEDIA_IMAGES 权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.READ_MEDIA_IMAGES
            if (requireContext().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(permission)
            }
        }
    }
    // 申请单个权限回调Launcher
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            ToastUtils.makeText("请授予访问图片的权限，否则无法更换头像")
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            viewModel.saveAvatar(it)
        }
    }

    //初始化点击事件
    private fun initClick() {
        binding.apply {
            tvUserLogin.setOnClickListener {
                TheRouter.build(RoutePath.LOGIN).navigation()
            }


            itemUserCollect.setOnClickListener {
                //未登录的状态
                if (viewModel.usernameData.value.isNullOrBlank()) {
                    ToastUtils.makeText("你还没登录哟")
                    return@setOnClickListener
                }
                PlaylistActivity.startPlaylistActivity("collect")
            }

            itemUserRecentplayed.setOnClickListener {
                //未登录的状态
                if (viewModel.usernameData.value.isNullOrBlank()) {
                    ToastUtils.makeText("你还没登录哟")
                    return@setOnClickListener
                }
                PlaylistActivity.startPlaylistActivity("recent")
            }

            imgUserAvatar.setOnClickListener {
                // 登录状态下才允许更换头像
                if (viewModel.usernameData.value.isNullOrBlank()) {
                    ToastUtils.makeText("请先登录再更换头像")
                } else {
                    pickImageLauncher.launch("image/*")
                }
            }

        }
    }

    //图片加载配置
    val requestOptions: RequestOptions =
        RequestOptions().placeholder(com.onerainbow.module.musicplayer.R.drawable.loading)
            .fallback(com.onerainbow.module.musicplayer.R.drawable.loading)

    override fun observeData() {
        viewModel.apply {
            usernameData.observe(viewLifecycleOwner) {
                if (it.isNotBlank()) {
                    //显示用户名，登出按钮，隐藏登录按钮
                    binding.apply {
                        tvUserUsername.text = "你好！ ${it}"
                        tvUserUsername.visibility = View.VISIBLE
                        tvUserLogin.visibility = View.GONE
                    }
                } else {
                    //返回“”，表示登出
                    //隐藏用户名和登出,显示登录
                    binding.apply {
                        tvUserUsername.visibility = View.GONE
                        tvUserLogin.visibility = View.VISIBLE
                    }
                }
            }

            //头像的加载
            avatarData.observe(viewLifecycleOwner) {
                it?.let {
                    Glide.with(requireActivity()).load(it).apply(requestOptions)
                        .into(binding.imgUserAvatar)
                }?:binding.imgUserAvatar.setImageResource(R.drawable.avatar)
            }

            errorAvatar.observe(viewLifecycleOwner) {
                ToastUtils.makeText(it)
            }

            //最近播放的一首歌封面
            lastPlay.observe(this@UserFragment) {
                it?.let {
                    Glide.with(requireActivity()).load(it.coverUrl).apply(requestOptions)
                    .into(binding.imgUserRecentplayed)
                }?:binding.imgUserRecentplayed.setImageResource(com.onerainbow.module.musicplayer.R.drawable.loading)
            }

                collect.observe(this@UserFragment) {
                    it?.let {
                        Glide.with(requireActivity()).load(it.coverUrl).apply(requestOptions)
                            .into(binding.imgUserCollect)
                    }?:binding.imgUserCollect.setImageResource(com.onerainbow.module.musicplayer.R.drawable.loading)
                }

            collectCount.observe(this@UserFragment) {
                binding.tvUserLikeCount.text = "${it} 首"
            }
        }
    }
}