package com.example.module.seek

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.module.seek.adapter.PopmusicAdapter
import com.example.module.seek.adapter.PopmusicListAdapter
import com.example.module.seek.data.Playlist
import com.example.module.seek.databinding.ActivitySeekBinding
import com.example.module.seek.viewmodel.SeekViewModel
import com.google.android.flexbox.FlexboxLayout
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.route.RoutePath
import com.therouter.router.Route

@Route(path = RoutePath.SEEK)
class SeekActivity : BaseActivity<ActivitySeekBinding>() {
    private val seekViewModel: SeekViewModel by viewModels()
    private lateinit var viewpager2Adapter: PopmusicListAdapter

    override fun getViewBinding(): ActivitySeekBinding = ActivitySeekBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewPager2()
    }

    override fun initViewModel() {
        seekViewModel.PopmusicDataLiveData.observe(this) { result ->
            val playlists: List<Playlist> = result.map { it.playlist }
            viewpager2Adapter.submitList(playlists)
        }
    }

    override fun initEvent() {
        seekViewModel.getPopmusic()
        setHistory()
    }

    fun setHistory() {
        val tags = listOf("流行", "摇滚", "爵士", "古典", "电子", "说唱", "乡村", "程豪演的要不要")

        // 清空之前的内容（防止重复添加）
        binding.flexboxLayout.removeAllViews()

        for (tag in tags) {
            val textView = TextView(this).apply {
                text = tag
                setTextColor(Color.parseColor("#17182C")) // 深色文字
                textSize = 14f
                setPadding(40, 20, 40, 20)
                background = ContextCompat.getDrawable(this@SeekActivity, R.drawable.bg_tag)
                setOnClickListener {
                    Toast.makeText(this@SeekActivity, "点击了 $tag", Toast.LENGTH_SHORT).show()
                }
            }

            val params = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16, 16, 16, 16)
            }

            // 用 binding 引用布局里的 flexboxLayout
            binding.flexboxLayout.addView(textView, params)
        }

    }

    fun setViewPager2() {
        viewpager2Adapter = PopmusicListAdapter(this)
        binding.viewpager2Popmusic.adapter = viewpager2Adapter

        // 可选：设置初始页面
        binding.viewpager2Popmusic.setCurrentItem(0, false)
        binding.viewpager2Popmusic.apply {
            binding.viewpager2Popmusic.adapter = viewpager2Adapter

            // 让 ViewPager2 左右留白
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3

            // 页面缩放效果
            setPageTransformer { page, position ->
                val scale = 0.85f + (1 - kotlin.math.abs(position)) * 0.15f
                page.scaleY = scale
                page.scaleX = scale
                page.alpha = 0.5f + (1 - kotlin.math.abs(position)) * 0.5f
            }
        }
    }

}