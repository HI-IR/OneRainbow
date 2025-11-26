package com.onerainbow.buildlogic.plugins

import com.android.build.api.dsl.ApplicationExtension
import com.onerainbow.buildlogic.plugins.config.debugImplementation
import com.onerainbow.buildlogic.plugins.config.getLib
import com.onerainbow.buildlogic.plugins.config.getVersion
import com.onerainbow.buildlogic.plugins.config.id
import com.onerainbow.buildlogic.plugins.config.implementation
import com.onerainbow.buildlogic.plugins.config.kotlinAndroid
import com.onerainbow.buildlogic.plugins.config.ksp
import com.onerainbow.buildlogic.plugins.config.versionLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * description ： OneRainbow 的 ApplicationPlugin
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/11/21 15:46
 */
class AndroidApplicationPlugin : Plugin<Project> {
	override fun apply(target: Project) {
		with(target) {
			with(pluginManager) {
				id("com.android.application") // 应用Android应用插件
				id("org.jetbrains.kotlin.android") // 应用Kotlin Android插件
				id("therouter") // therouter的插件
				id("com.google.devtools.ksp") //ksp
				id("org.jetbrains.kotlin.plugin.parcelize") //kotlin 序列化
			}

			extensions.configure<ApplicationExtension> {
				// 使用统一的 Kotlin Android 配置
				kotlinAndroid(this)
				//对于Application 的配置
				defaultConfig {
					applicationId = versionLibs.findVersion("namespace").get().toString()
					targetSdk = 35
					versionCode = getVersion("versionCode")
					versionName = versionLibs.findVersion("versionName").get().toString()
				}
			}

			dependencies{
				//内存泄露检测
				debugImplementation(getLib("leakcanary"))


				//application默认携带therouter能力
				ksp(getLib("therouter-apt"))
				implementation(getLib("therouter-router"))
			}
		}
	}
}