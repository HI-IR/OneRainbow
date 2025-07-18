package com.example.module.seek.repository

import com.example.module.seek.data.GetMvData
import com.example.module.seek.data.LyricData
import com.example.module.seek.data.PlaylistData
import com.example.module.seek.data.SingleData
import com.example.module.seek.data.SpecialData
import com.example.module.seek.data.UserData
import com.example.module.seek.interfaces.FinishSeekDataService
import com.onerainbow.lib.net.RetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 15:17
 */
class FInishSeekRepository {
    private val FinishSeekDataService =RetrofitClient.create(FinishSeekDataService::class.java)

    fun getSingleData(keyWord:String): Observable<SingleData> {
        return FinishSeekDataService.getSinglekData(keyWord,1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun getPlaylistData(keyWord: String):Observable<PlaylistData> {
        return FinishSeekDataService.getPlaylistData(keyWord ,1000)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun getUserData(keyWord: String):Observable<UserData>{
        return  FinishSeekDataService.getUserData(keyWord,100)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun getLyricData(keyWord: String):Observable<LyricData>{
        return  FinishSeekDataService.getLyricData(keyWord,1006)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun getSpecialData(keyWord: String):Observable<SpecialData>{
        return  FinishSeekDataService.getSpecialData(keyWord,10)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun getGetMvData(keyWord: String):Observable<GetMvData>{
        return  FinishSeekDataService.getGetMvData(keyWord,1004)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


}