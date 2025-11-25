package com.onerainbow.page.search.repository

import com.onerainbow.lib.net.RetrofitClient
import com.onerainbow.page.search.data.GetMvData
import com.onerainbow.page.search.data.LyricData
import com.onerainbow.page.search.data.PlaylistData
import com.onerainbow.page.search.data.SingleData
import com.onerainbow.page.search.data.UrlData
import com.onerainbow.page.search.data.UserData
import com.onerainbow.page.search.interfaces.FinishSeekDataService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 15:17
 */
class FinishSeekRepository {
	private val finishSeekDataService = RetrofitClient.create(FinishSeekDataService::class.java)
	private var urlDataDisposable: Disposable? = null


	fun getSingleData(keyWord: String): Observable<SingleData> {
		return finishSeekDataService.getSinglekData(keyWord, 1)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
	}

	fun getPlaylistData(keyWord: String): Observable<PlaylistData> {
		return finishSeekDataService.getPlaylistData(keyWord, 1000)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
	}

	fun getUserData(keyWord: String): Observable<UserData> {
		return finishSeekDataService.getUserData(keyWord, 100)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
	}

	fun getLyricData(keyWord: String): Observable<LyricData> {
		return finishSeekDataService.getLyricData(keyWord, 1006)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
	}

	fun getGetMvData(keyWord: String): Observable<GetMvData> {
		return finishSeekDataService.getGetMvData(keyWord, 1004)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
	}

	fun getUrlData(id: Long, onResult: (UrlData) -> Unit, onError: (Throwable) -> Unit) {
		// 取消之前的请求
		urlDataDisposable?.dispose()

		// 重新订阅
		urlDataDisposable = finishSeekDataService.getImgUrl(id)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
				{ result ->
					onResult(result)
				},
				{ error ->
					onError(error)
				}
			)
	}


	fun cancelUrlRequest() {
		urlDataDisposable?.dispose()
		urlDataDisposable = null
	}


}