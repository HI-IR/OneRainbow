package com.example.module.seek.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.module.seek.data.GetMvData
import com.example.module.seek.data.LyricData
import com.example.module.seek.data.PlaylistData
import com.example.module.seek.data.SingleData
import com.example.module.seek.data.UrlData
import com.example.module.seek.data.UserData
import com.example.module.seek.repository.FInishSeekRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 15:10
 */
class FinishSeekViewModel : ViewModel() {
    private val fInishSeekRepository: FInishSeekRepository = FInishSeekRepository()
    val singleDataLiveData = MutableLiveData<SingleData>()
    val playlistDataLiveData = MutableLiveData<PlaylistData>()
    val userDataLiveData = MutableLiveData<UserData>()
    val LyricDataLiveData = MutableLiveData<LyricData>()
    val getMvDataLiveData = MutableLiveData<GetMvData>()
    val getUrlDataLiveData =MutableLiveData<UrlData>()


    fun getSingle(keyWord: String) {
        fInishSeekRepository.getSingleData(keyWord).subscribe(object : Observer<SingleData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: SingleData) {
                Log.d("SingleData", t.toString())
                singleDataLiveData.postValue(t)
            }

        })


    }

    fun getPlaylist(keyWord: String) {
        fInishSeekRepository.getPlaylistData(keyWord).subscribe(object : Observer<PlaylistData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }


            override fun onComplete() {

            }

            override fun onNext(t: PlaylistData) {
                Log.d("PlaylistData", t.toString())
                playlistDataLiveData.postValue(t)
            }

        })
    }

    fun getUser(keyWord: String) {
        fInishSeekRepository.getUserData(keyWord).subscribe(object : Observer<UserData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: UserData) {
                Log.d("UserData", t.toString())
                userDataLiveData.postValue(t)

            }

        })
    }

    fun getLyric(keyWord: String) {
        fInishSeekRepository.getLyricData(keyWord).subscribe(object : Observer<LyricData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: LyricData) {
                Log.d("LyricData", t.toString())
                LyricDataLiveData.postValue(t)

            }

        })
    }

    fun getGetMv(keyWord: String) {
        fInishSeekRepository.getGetMvData(keyWord).subscribe(object : Observer<GetMvData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: GetMvData) {
                Log.d("GetMvlData", t.toString())
                getMvDataLiveData.postValue(t)

            }

        })
    }
    fun getUrlData(id:Long) {
        fInishSeekRepository.getUrlData(id).subscribe(object : Observer<UrlData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: UrlData) {
                Log.d("UrlData", t.toString())
                getUrlDataLiveData.postValue(t)

            }

        })
    }


}