package com.onerainbow.page.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onerainbow.page.search.data.GetMvData
import com.onerainbow.page.search.data.LyricData
import com.onerainbow.page.search.data.PlaylistData
import com.onerainbow.page.search.data.SingleData
import com.onerainbow.page.search.data.UrlData
import com.onerainbow.page.search.data.UserData
import com.onerainbow.page.search.repository.FinishSeekRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 15:10
 */
class FinishSeekViewModel : ViewModel() {
    private val finishSeekRepository = FinishSeekRepository()

    val _singleDataLiveData = MutableLiveData<SingleData>()
    val singleDataLiveData: LiveData<SingleData> = _singleDataLiveData

    val _playlistDataLiveData = MutableLiveData<PlaylistData>()
    val playlistDataLiveData : LiveData<PlaylistData> = _playlistDataLiveData

    val _userDataLiveData = MutableLiveData<UserData>()
    val userDataLiveData : LiveData<UserData> = _userDataLiveData

    val _LyricDataLiveData = MutableLiveData<LyricData>()
    val LyricDataLiveData : LiveData<LyricData> = _LyricDataLiveData

    val _getMvDataLiveData = MutableLiveData<GetMvData>()
    val getMvDataLiveData : LiveData<GetMvData> = _getMvDataLiveData

    val _getUrlDataLiveData = MutableLiveData<UrlData>()
    val getUrlDataLiveData : LiveData<UrlData> = _getUrlDataLiveData


    fun getSingle(keyWord: String) {
        finishSeekRepository.getSingleData(keyWord).subscribe(object : Observer<SingleData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: SingleData) {
                Log.d("SingleData", t.toString())
                _singleDataLiveData.postValue(t)
            }

        })


    }

    fun getPlaylist(keyWord: String) {
        finishSeekRepository.getPlaylistData(keyWord).subscribe(object : Observer<PlaylistData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }


            override fun onComplete() {

            }

            override fun onNext(t: PlaylistData) {
                Log.d("PlaylistData", t.toString())
                _playlistDataLiveData.postValue(t)
            }

        })
    }

    fun getUser(keyWord: String) {
        finishSeekRepository.getUserData(keyWord).subscribe(object : Observer<UserData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: UserData) {
                Log.d("UserData", t.toString())
                _userDataLiveData.postValue(t)

            }

        })
    }

    fun getLyric(keyWord: String) {
        finishSeekRepository.getLyricData(keyWord).subscribe(object : Observer<LyricData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: LyricData) {
                Log.d("LyricData", t.toString())
                _LyricDataLiveData.postValue(t)

            }

        })
    }

    fun getGetMv(keyWord: String) {
        finishSeekRepository.getGetMvData(keyWord).subscribe(object : Observer<GetMvData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: GetMvData) {
                Log.d("GetMvlData", t.toString())
                _getMvDataLiveData.postValue(t)

            }

        })
    }

    fun getUrlData(id: Long, onResult: (UrlData) -> Unit, onError: (Throwable) -> Unit) {
        // 每次新请求前取消旧请求
        finishSeekRepository.cancelUrlRequest()

        finishSeekRepository.getUrlData(
            id,
            { result ->
                _getUrlDataLiveData.postValue(result)
                onResult(result)
            },
            { error ->
                onError(error)
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        finishSeekRepository.cancelUrlRequest() // 清理 URL 请求
    }

    fun cancelUrlRequest() {
        finishSeekRepository.cancelUrlRequest()
    }


}