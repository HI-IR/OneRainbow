package com.onerainbow.module.seek.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.onerainbow.module.seek.data.Comment
import com.onerainbow.module.seek.data.CommentNumber
import com.onerainbow.module.seek.data.MvUrl
import com.onerainbow.module.seek.repository.VideoRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.flow.Flow
import kotlin.math.E

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/20 21:35
 */
class VideoViewModel : ViewModel() {
    val videoRepository: VideoRepository = VideoRepository()

    val _getUrlLiveData = MutableLiveData<MvUrl>()
    val Url: LiveData<MvUrl> = _getUrlLiveData

    val _getCommentNumberLiveData = MutableLiveData<CommentNumber>()
    val getCommentNumberLiveData: LiveData<CommentNumber> = _getCommentNumberLiveData

    private val model by lazy {
        CommentModel
    }


    fun getUrl(id: Long) {
        videoRepository.getUrl(id).subscribe(object : Observer<MvUrl> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: MvUrl) {
                Log.d("MvUrl", t.toString())
                _getUrlLiveData.postValue(t)
            }

        })
    }


    fun getCommentNumber(id: Long) {
        videoRepository.getCommentNumber(id).subscribe(object : Observer<CommentNumber> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: CommentNumber) {
                Log.d("CommentNumber", t.toString())
                _getCommentNumberLiveData.postValue(t)
            }

        })
    }

    fun getComments(musicId: Long): Flow<PagingData<Comment>> {
        return model.getComment(musicId).cachedIn(viewModelScope)
    }
}