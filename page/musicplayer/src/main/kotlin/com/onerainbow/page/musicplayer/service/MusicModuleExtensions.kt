package com.onerainbow.page.musicplayer.service

import com.onerainbow.page.musicplayer.api.IMusicplayerService
import com.therouter.TheRouter

/**
 * description ： 用于在MusicModule中获取MusicManager
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/11/26 19:07
 */
internal val musicManager: MusicManager
	get() {
		// 获取接口实例
		val service = TheRouter.get(IMusicplayerService::class.java)
			?: throw IllegalStateException("MusicManager not found! Make sure @ServiceProvider is working.")

		return service as MusicManager
	}