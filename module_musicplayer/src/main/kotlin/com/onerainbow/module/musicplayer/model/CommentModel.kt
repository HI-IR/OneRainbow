package com.onerainbow.module.musicplayer.model

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onerainbow.lib.net.RetrofitClient
import com.onerainbow.module.musicplayer.domain.Comment
import com.onerainbow.module.musicplayer.net.MusicApi
import kotlinx.coroutines.flow.Flow

/**
 * description ： TODO:类的作用
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/19 11:46
 */
object CommentModel {
    private val api by lazy {
        RetrofitClient.create(MusicApi::class.java)
    }

    private val PAGE_SIZE = 30

    fun getComment(musicId: Long): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { CommentPagingSource(api, musicId) }
        ).flow
    }
}