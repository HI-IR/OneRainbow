package com.onerainbow.page.top.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

/**
 * description ： 热门页VP2的Adapter
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 12:28
 */
class TopVp2Adapter private constructor(
	fragmentManager: FragmentManager,
	lifecycle: Lifecycle
) : androidx.viewpager2.adapter.FragmentStateAdapter(
	fragmentManager, lifecycle
) {
	private val fragments = mutableListOf<() -> Fragment>()

	constructor(activity: FragmentActivity) : this(
		activity.supportFragmentManager,
		activity.lifecycle
	)

	constructor(fragment: Fragment) : this(
		fragment.childFragmentManager,
		fragment.lifecycle
	)

	fun add(fragmentList: List<Class<out Fragment>>): TopVp2Adapter {
		fragmentList.forEach { fragment ->
			fragments.add { fragment.newInstance() }
		}
		return this
	}


	override fun createFragment(position: Int): Fragment = fragments[position].invoke()

	override fun getItemCount(): Int = fragments.size
}