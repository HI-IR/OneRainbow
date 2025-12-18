package com.onerainbow.buildlogic.plugins

import com.onerainbow.buildlogic.plugins.dsl.id
import com.onerainbow.buildlogic.plugins.dsl.implementation
import com.onerainbow.buildlogic.plugins.dsl.ksp
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * description ： 按需引入的插件
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/11/21 16:05
 */

// library使用TheRouter需要使用次依赖，application已经默认配置，自动使用route
fun Project.useTheRouter() {
	with(pluginManager) {
		id("com.google.devtools.ksp") //ksp
		id("kotlin-parcelize")
	}
	dependencies {
		ksp("therouter-apt")
		implementation("therouter-router")

		//如果自己不是route说明是业务模块，则添加依赖
		if (name != "route") {
			//使用 project(":components:route") 通过字符串获取本地模块实例
			add("implementation", project(":components:route"))
		}
	}
}

//使用数据库
fun Project.useRoom() {
	with(pluginManager) {
		id("com.google.devtools.ksp")//ksp
	}
	dependencies {
		implementation("androidx-room-runtime")
		implementation("converter-gson")
		ksp("room-compiler")
		implementation("room.ktx")

		if (name != "database") {
			//使用 project(":components:database") 通过字符串获取本地模块实例
			add("implementation", project(":components:database"))
		}
	}
}

//使用媒体框架media3
fun Project.useMedia3() {
	dependencies {
		implementation("androidx-media3-session")
		implementation("androidx-media3-exoplayer")
		implementation("androidx-media3-ui")
	}
}


//使用网络服务，自动接入components:net，默认使用Rxjava
fun Project.useNet() {
	dependencies {
		implementation("adapter-rxjava3")
		implementation("rxandroid")
		implementation("rxjava")
		implementation("retrofit")
		implementation("converter-gson")


		if (name != "net") {
			//使用 project(":components:net") 通过字符串获取本地模块实例
			add("implementation", project(":components:net"))
		}
	}
}