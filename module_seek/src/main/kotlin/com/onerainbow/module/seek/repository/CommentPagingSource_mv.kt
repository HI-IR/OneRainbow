package com.onerainbow.module.seek.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onerainbow.module.musicplayer.bean.toComment
import com.onerainbow.module.seek.data.Comment

import com.onerainbow.module.seek.interfaces.MvService

/**
 * description ： 评论的PagingSource
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/19 11:31
 */
class MvCommentPagingSource(
    private val mvService: MvService,
    private val mvId: Long
) : PagingSource<Int, Comment>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        return try {
            val page = params.key ?: 0
            val limit = params.loadSize
            val offset = page * limit

            val response = mvService.getMvComments(mvId).blockingFirst()

            val commentList = response.comments.map { it }

            LoadResult.Page(
                data = commentList,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.more) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
