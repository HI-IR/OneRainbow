package com.onerainbow.module.musicplayer.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.base.BaseApplication
import com.onerainbow.module.musicplayer.R
import com.onerainbow.module.musicplayer.databinding.ActivityMusicPlayerBinding
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.service.MusicService
import com.onerainbow.module.musicplayer.viewmodel.MusicPlayerViewModel

class MusicPlayerActivity : BaseActivity<ActivityMusicPlayerBinding>() {
    override fun getViewBinding(): ActivityMusicPlayerBinding = ActivityMusicPlayerBinding.inflate(layoutInflater)

    private val viewModel by lazy {
        ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(BaseApplication.context))[MusicPlayerViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initViewModel() {
        //TODO("Not yet implemented")
    }

    override fun initEvent() {
        initClick()
    }

    /**
     * 绑定点击事件
     */
    private fun initClick() {
        binding.apply {
            btnPlay.setOnClickListener {
                viewModel.play(Song(2723771732,"测试歌曲","测试作者","http://m801.music.126.net/20250716194047/30a849aa49031c8b6ebd5196dd2eacb4/jdymusic/obj/wo3DlMOGwrbDjj7DisKw/61177650827/1c59/e251/4d20/fe5b57d0e30b9d16a328940c14e16e1a.mp3?vuutv=QOvA7BAhasY9ti+9su8l6WO2ThjTPwpN07sXbAUek3B316F6/uUMRfvJqqiD1Lhb29OhMF2Lq3R2IA0hzt0JjKv4gRahpFweozjRNoO2E2M=","https://p1.music.126.net/iAwVf8ag_45csIUuh1wSZg==/109951168912558470.jpg"))
            }
        }
    }

    /**
     * 初始化音乐服务
     */
    private fun initMusicService() {
        viewModel.bindService()
    }

    /**
     * 解绑
     */
    protected fun unbindMusicService(){
        viewModel.unbindService()
    }

    override fun onStart() {
        super.onStart()
        initMusicService()
    }

    override fun onStop() {
        super.onStop()
        unbindMusicService()
    }


}