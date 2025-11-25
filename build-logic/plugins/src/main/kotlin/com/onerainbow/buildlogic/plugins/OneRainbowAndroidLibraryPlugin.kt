package com.onerainbow.buildlogic.plugins

import com.android.build.gradle.LibraryExtension
import com.onerainbow.buildlogic.plugins.config.id
import com.onerainbow.buildlogic.plugins.config.kotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * description ： OneRainbow 的 LibraryPlugin
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/11/21 16:00
 */
class AndroidLibraryPlugin: Plugin<Project>  {
	override fun apply(target: Project) {
		with(target) {
			with(pluginManager) {
				id("org.jetbrains.kotlin.android") // 应用Kotlin Android插件
				id("com.android.library") // 应用kotlin library
				id("com.google.devtools.ksp") // ksp
			}
			extensions.configure<LibraryExtension> {
				kotlinAndroid(this)
			}
		}
	}
}