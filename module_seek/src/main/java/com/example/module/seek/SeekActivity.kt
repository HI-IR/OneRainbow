package com.example.module.seek

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.therouter.TheRouter
import com.therouter.router.Autowired
import com.therouter.router.Route

@Route(path = RoutePath.SEEK)
class SeekActivity : BaseActivity<ActivitySeekBinding>() {
    @Autowired(name = "keywords")
    var keywords: String? = null
    private val seekViewModel: SeekViewModel by viewModels()
    private lateinit var viewpager2Adapter: PopmusicListAdapter

    override fun getViewBinding(): ActivitySeekBinding = ActivitySeekBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewPager2()
    }

    fun addTag(tag: String) {
        val prefs = getSharedPreferences("seek_prefs", MODE_PRIVATE)
        val tags = prefs.getStringSet("tags_key", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        // 检查是否已经存在
        if (tags.contains(tag)) {
            return  // 已存在，不重复添加
        }

        tags.add(tag) // 加入新标签
        prefs.edit().putStringSet("tags_key", tags).apply()
    }



    fun getAllTags(): List<String> {
        val prefs = getSharedPreferences("seek_prefs", MODE_PRIVATE)
        return prefs.getStringSet("tags_key", emptySet())?.toList() ?: emptyList()
    }

    fun clearAllTags() {
        val prefs = getSharedPreferences("seek_prefs", MODE_PRIVATE)
        prefs.edit().remove("tags_key").apply()
        setHistory()
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
        binding.tvDelete.setOnClickListener{
            clearAllTags()
        }
        binding.tvSeek.setOnClickListener {

            Log.d("keywordone", binding.etSeek.text.toString())
            TheRouter.build(RoutePath.FINISHSEEK)
                .withString("keyword", binding.etSeek.text?.toString()?.trim() ?: "冬眠")
                .navigation()
            addTag(binding.etSeek.text.toString())
            setHistory()

        }
        LiveDataBus.keywordResult.observe(this) { result ->
            binding.etSeek.setText(result)
        }

    }

    fun setHistory() {
        val tags = getAllTags().ifEmpty {
            listOf("流行", "摇滚", "爵士", "古典", "电子", "说唱", "乡村", "要不要")
        }

        binding.flexboxLayout.removeAllViews()

        for (tag in tags) {
            val textView = TextView(this).apply {
                text = tag
                setTextColor(Color.parseColor("#17182C"))
                textSize = 14f
                setPadding(40, 20, 40, 20)
                background = ContextCompat.getDrawable(this@SeekActivity, R.drawable.bg_tag)
                setOnClickListener {
                    TheRouter.build(RoutePath.FINISHSEEK)
                        .withString("keyword", tag)
                        .navigation()

                }
            }

            val params = FlexboxLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(16, 16, 16, 16)
            }
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