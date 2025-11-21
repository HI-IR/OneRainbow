package com.onerainbow.module.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * description ： 首页的Vp2的Adapter
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/14 09:53
 */
class HomeVpAdapter private constructor(
	fragmentManager: FragmentManager,
	lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

	constructor(activity: FragmentActivity) : this(
		activity.supportFragmentManager,
		activity.lifecycle
	)

	constructor(fragment: Fragment) : this(fragment.childFragmentManager, fragment.lifecycle)

	private val mFragments = arrayListOf<() -> Fragment>()
	//注意这个设计很好：
	//只有当 ViewPager2 滚动到对应位置时，才会通过 createFragment() 调用 lambda 创建 Fragment 实例，避免提前创建所有 Fragment 导致的内存浪费

	fun add(fragment: () -> Fragment): HomeVpAdapter {
		mFragments.add(fragment)
		return this
	}

	fun add(fragment: Class<out Fragment>): HomeVpAdapter {
		// 官⽅源码中在恢复 Fragment 时就是调⽤的这个反射⽅法，该⽅法应该不是很耗性能
		mFragments.add { fragment.newInstance() }
		return this
	}

	fun add(fragmentList: List<Class<out Fragment>>): HomeVpAdapter{
		fragmentList.forEach {fragment->
			mFragments.add { fragment.newInstance()}
		}
		return this
	}

	override fun getItemCount(): Int = mFragments.size

	//调用mFragments中存储的创建Fragment的函数，来创建Fragment
	override fun createFragment(position: Int): Fragment =
		mFragments[position].invoke()
}