package com.onerainbow.module.seek.repository

import com.onerainbow.module.seek.data.GetPlaylistData
import com.onerainbow.module.seek.data.PlaylistData
import com.onerainbow.module.seek.data.SongersData
import com.onerainbow.module.seek.data.SongsData
import com.onerainbow.module.seek.interfaces.PlaylistAllService
import com.onerainbow.lib.net.RetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/18 21:06
 */
class PlaylistRepository {
    private val playlistRepository = RetrofitClient.create(PlaylistAllService::class.java)

    fun getPlaylistData(id: Long): Observable<GetPlaylistData> {
        return playlistRepository.getPlaylistAll(id, 50)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getSongerData(id: Long): Observable<SongersData> {
        return playlistRepository.getSongerData(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun getSongsData(id: Long):Observable<SongsData>{
        return playlistRepository.getSongsData(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}