package com.onerainbow.page.musicplayer.impl

import android.content.Context
import com.onerainbow.page.musicplayer.api.IMusicUIService
import com.onerainbow.page.musicplayer.api.bean.Song
import com.onerainbow.page.musicplayer.ui.PlayerListDialog
import com.therouter.inject.ServiceProvider
import java.lang.ref.WeakReference

/**
 * description ： IMusicUIService的实现类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/11/26 19:35
 */

@ServiceProvider(IMusicUIService::class)
class IMusicUIServiceImpl : IMusicUIService {

	//因为IMusicUIServiceImpl会比PlayerListDialog生命周期长，防止内存泄漏这里使用弱引用
	private var dialogRef: WeakReference<PlayerListDialog>? = null


	override fun openPlayerListDialog(context: Context, onSongSelected: (Int) -> Unit) {
		// 检查是否存在旧的 Dialog 且正在显示
		val oldDialog = dialogRef?.get()
		if (oldDialog != null && oldDialog.isShowing) {
			// 如果已经在显示了，就不要重复创建了，直接返回
			return
		}
		// 创建新的 Dialog
		val newDialog = PlayerListDialog(context, onSongSelected)
		// 监听 Dismiss 事件，清理引用
		newDialog.setOnDismissListener {
			dialogRef = null
		}

		newDialog.show()
		dialogRef = WeakReference(newDialog)
	}

	override fun setSelectedPosition(position: Int) {
		val dialog = dialogRef?.get()

		//如果为空或者没显示则直接返回
		if (dialog == null || !dialog.isShowing){
			return
		}
		dialog.setSelectedPosition(position)
	}

	override fun setSongs(list: List<Song>){
		val dialog = dialogRef?.get()

		//如果为空或者没显示则直接返回
		if (dialog == null || !dialog.isShowing){
			return
		}
		dialog.setSongs(list)
	}


}

