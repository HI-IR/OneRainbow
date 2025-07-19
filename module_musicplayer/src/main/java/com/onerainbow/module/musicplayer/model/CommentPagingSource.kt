package com.onerainbow.module.musicplayer.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onerainbow.module.musicplayer.bean.toComment
import com.onerainbow.module.musicplayer.repository.MusicApi

/**
 * description ： 评论的PagingSource
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/19 11:31
 */
class CommentPagingSource(
    private val apiService: MusicApi,
    private val musicId: Long
) : PagingSource<Int, Comment>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        return try {
            val page = params.key ?: 0
            val limit = params.loadSize
            val offset = page * limit

            val response = apiService.getMusicComments(musicId, limit, offset).blockingFirst()

            val commentList = response.comments.map { it.toComment() }

            LoadResult.Page(
                data = commentList,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.more) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? = null
}
