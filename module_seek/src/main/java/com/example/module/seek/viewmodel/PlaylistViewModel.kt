package com.example.module.seek.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.module.seek.data.GetPlaylistData
import com.example.module.seek.data.SongersData
import com.example.module.seek.data.SongsData
import com.example.module.seek.repository.PlaylistRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/18 21:09
 */
class PlaylistViewModel : ViewModel() {
    private val playlistRepository: PlaylistRepository = PlaylistRepository()
    val playListLiveData = MutableLiveData<GetPlaylistData>()
    val songerDataLiveData = MutableLiveData<SongersData>()
    val songsDataLiveData =MutableLiveData<SongsData>()

    fun getPlaylistData(id: Long) {
        playlistRepository.getPlaylistData(id).subscribe(object : Observer<GetPlaylistData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: GetPlaylistData) {
                Log.d("GetPlaylistData", t.toString())
                playListLiveData.postValue(t)
            }


        })
    }
    fun getSongerData(id: Long){
        playlistRepository.getSongerData(id).subscribe(object :Observer<SongersData>{
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
               e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: SongersData) {
               Log.d("SongerData",t.toString())
                songerDataLiveData.postValue(t)
            }

        })
    }
    fun getSongsData(id: Long){
        playlistRepository.getSongsData(id).subscribe(object :Observer<SongsData>{
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
              e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: SongsData) {
                Log.d("SongsData",t.toString())
                songsDataLiveData.postValue(t)

            }

        })
    }
}