package com.onerainbow.module.seek.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onerainbow.lib.net.RetrofitClient
import com.onerainbow.module.musicplayer.model.CommentPagingSource
import com.onerainbow.module.seek.data.Comment

import com.onerainbow.module.seek.interfaces.MvService
import com.onerainbow.module.seek.repository.MvCommentPagingSource
import kotlinx.coroutines.flow.Flow

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/21 00:26
 */
object CommentModel {
    private val api by lazy {
        RetrofitClient.create(MvService::class.java)
    }

    private val PAGE_SIZE = 30

    fun getComment(musicId: Long): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { MvCommentPagingSource(api, musicId) }
        ).flow
    }
}