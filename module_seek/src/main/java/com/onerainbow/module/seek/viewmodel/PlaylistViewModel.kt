package com.onerainbow.module.seek.viewmodel

import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onerainbow.lib.base.utils.UsernameUtils
import com.onerainbow.lib.database.OneRainbowDatabase
import com.onerainbow.lib.database.entity.CollectEntity
import com.onerainbow.lib.database.entity.RecentPlayedEntity
import com.onerainbow.module.musicplayer.domain.Song
import com.onerainbow.module.musicplayer.helper.RecentPlayHelper
import com.onerainbow.module.musicplayer.service.MusicManager
import com.onerainbow.module.seek.data.GetPlaylistData
import com.onerainbow.module.seek.data.SongersData
import com.onerainbow.module.seek.data.SongsData
import com.onerainbow.module.seek.repository.PlaylistRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/18 21:09
 */
class PlaylistViewModel : ViewModel() {
    private val recentDao by lazy {
        OneRainbowDatabase.getDatabase().recentPlayDao()
    }
    private val collectDao by lazy {
        OneRainbowDatabase.getDatabase().collectDao()
    }
    private val userDao by lazy {
        OneRainbowDatabase.getDatabase().userDao()
    }


    private val playlistRepository: PlaylistRepository = PlaylistRepository()

    private val _playListLiveData = MutableLiveData<GetPlaylistData>()
    val playListLiveData: LiveData<GetPlaylistData> = _playListLiveData

    private val _songerDataLiveData = MutableLiveData<SongersData>()
    val songerDataLiveData: LiveData<SongersData> = _songerDataLiveData

    private val _songsDataLiveData = MutableLiveData<SongsData>()
    val songsDataLiveData: LiveData<SongsData> = _songsDataLiveData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _recentDataLiveData = MutableLiveData<List<RecentPlayedEntity>> ()
    val recentDataLiveData :LiveData<List<RecentPlayedEntity>> = _recentDataLiveData

    private val _collectDataLiveData = MutableLiveData<List<CollectEntity>>()
    val collectDataLiveData : LiveData<List<CollectEntity>> = _collectDataLiveData

    private val _avatarData = MutableLiveData<ByteArray?>()
    val avatarData: LiveData<ByteArray?>  = _avatarData

    fun getUserImg(){
        val username = UsernameUtils.getUsername()
        if(username.isNullOrBlank()) return

        viewModelScope.launch(Dispatchers.IO) {
            launch {
                try {
                    val base64String = userDao.getAvatarResByUsername(username)
                    if (base64String.isNullOrBlank()) return@launch

                    val pureBase64 = base64String.substringAfter("base64,", base64String)
                    val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
                    _avatarData.postValue(decodedBytes)
                } catch (e:Exception){
                    _error.postValue(e.message?:"头像加载失败")
                }
            }
        }
    }
    fun getRecentData(){
        val username = UsernameUtils.getUsername()
        if(username.isNullOrBlank()) return
        viewModelScope.launch {
            launch {
                try {
                    val recentData = recentDao.getRecentPlayList(username)

                    recentData?.let { _recentDataLiveData.postValue(it) }
                }catch (e:Exception){
                    _error.postValue(e.message?:"最近歌单加载失败")
                }
            }
        }
    }
    fun getCollectData(){
        val username = UsernameUtils.getUsername()
        if(username.isNullOrBlank())return
        viewModelScope.launch {
            launch {
                try {
                    val collectData = collectDao.getCollectionList(username)

                    collectData?.let { _collectDataLiveData.postValue(it) }
                }catch (e:Exception){
                    _error.postValue(e.message?:"我的收藏歌单加载失败")
                }
            }
        }
    }


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
                _playListLiveData.postValue(t)
            }


        })
    }

    fun getSongerData(id: Long) {
        playlistRepository.getSongerData(id).subscribe(object : Observer<SongersData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: SongersData) {
                Log.d("SongerData", t.toString())
                _songerDataLiveData.postValue(t)
            }

        })
    }

    fun getSongsData(id: Long) {
        playlistRepository.getSongsData(id).subscribe(object : Observer<SongsData> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }

            override fun onNext(t: SongsData) {
                Log.d("SongsData", t.toString())
                _songsDataLiveData.postValue(t)

            }

        })
    }





}