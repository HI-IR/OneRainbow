package com.onerainbow.module.seek

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.seek.adapter.PopmusicListAdapter
import com.onerainbow.module.seek.data.Playlist
import com.onerainbow.module.seek.databinding.ActivitySeekBinding
import com.onerainbow.module.seek.viewmodel.SeekViewModel
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

    override fun observeData() {
        seekViewModel.PopmusicDataLiveData.observe(this) { result ->
            val playlists: List<Playlist> = result.map { it.playlist }
            viewpager2Adapter.submitList(playlists)
        }

    }

    override fun initEvent() {
        seekViewModel.getPopmusic()
        setHistory()
        binding.tvDelete.setOnClickListener {
            clearAllTags()
        }
        binding.tvSeek.clickWithScale {
            val keyword = binding.etSeek.text?.toString()?.trim()?.ifBlank { "冬眠" }
            TheRouter.build(RoutePath.FINISHSEEK)
                .withString("keyword", keyword)
                .navigation()
            addTag(keyword!!)
            setHistory()
        }

        LiveDataBus.keywordResult.observe(this) { result ->
            binding.etSeek.setText(result)
        }

    }

    fun setViewPager2() {
        viewpager2Adapter = PopmusicListAdapter(this)
        binding.viewpager2Popmusic.adapter = viewpager2Adapter
        binding.btnOpenDrawer.setOnClickListener {
            finish()
        }

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

    //获取列表中所有内容
    fun getAllTags(): List<String> {
        val prefs = getSharedPreferences("seek_prefs", MODE_PRIVATE)
        return prefs.getStringSet("tags_key", emptySet())?.toList() ?: emptyList()
    }

    //清理历史记录
    fun clearAllTags() {
        val prefs = getSharedPreferences("seek_prefs", MODE_PRIVATE)
        prefs.edit().remove("tags_key").apply()
        setHistory()
    }

    //显示历史搜索，如果没有搜索就直接启用默认结果 并且再每次一次搜索后调用，及时更新显示数据
    fun setHistory() {
        val tags = getAllTags().ifEmpty {
            listOf("流行", "摇滚", "爵士", "古典", "电子", "说唱", "乡村", "要不要")
        }
        //删除上一次的内容
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
            textView.alpha = 0f
            binding.flexboxLayout.addView(textView, params)
            textView.animate()
                .alpha(1f)
                .setDuration(300)
                .start()

        }
    }
    fun View.clickWithScale(block: () -> Unit) {
        this.setOnClickListener {
            this.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction {
                this.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                block()
            }.start()
        }
    }



}