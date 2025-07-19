package com.onerainbow.module.musicplayer.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.musicplayer.R
import com.onerainbow.module.musicplayer.adapter.CommentAdapter
import com.onerainbow.module.musicplayer.databinding.ActivityCommentBinding
import com.onerainbow.module.musicplayer.viewmodel.CommentViewModel
import com.therouter.router.Autowired
import com.therouter.router.Route
import kotlinx.coroutines.launch

@Route(path = RoutePath.COMMENTS)
class CommentActivity : BaseActivity<ActivityCommentBinding>() {
    @Autowired(name = "musicId")
    var musicId: Long? = null


    override fun getViewBinding(): ActivityCommentBinding = ActivityCommentBinding.inflate(layoutInflater)
    private val commentAdapter by lazy { CommentAdapter(this) }
    private val viewModel by lazy {
        ViewModelProvider(this)[CommentViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initViewModel() {

    }

    override fun initEvent() {
        initClick()
        initView()
        initData()
    }

    private fun initView() {
        binding.rvComment.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(this@CommentActivity)
        }
        commentAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    binding.progressBar.visibility = View.INVISIBLE
                }
                is LoadState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, "Load Error: ${state.error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initData() {
        lifecycleScope.launch {
            viewModel.getComments(musicId!!).collect { pagingData ->
                commentAdapter.submitData(pagingData)
            }
        }

    }

    private fun initClick() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}