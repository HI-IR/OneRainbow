package com.onerainbow.page.musicplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.onerainbow.page.musicplayer.domain.Comment
import com.onerainbow.page.musicplayer.model.CommentModel
import kotlinx.coroutines.flow.Flow

/**
 * description ： 评论的ViewModel
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/19 11:50
 */
class CommentViewModel : ViewModel() {
	private val model by lazy {
		CommentModel
	}

	fun getComments(musicId: Long): Flow<PagingData<Comment>> {
		return model.getComment(musicId).cachedIn(viewModelScope)
	}


}