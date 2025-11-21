package com.onerainbow.buildlogic.plugins

import com.onerainbow.buildlogic.plugins.config.getLib
import com.onerainbow.buildlogic.plugins.config.id
import com.onerainbow.buildlogic.plugins.config.implementation
import com.onerainbow.buildlogic.plugins.config.ksp
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * description ： 按需引入的插件
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/11/21 16:05
 */

// library使用TheRouter需要使用次依赖，application已经默认配置
fun Project.useTheRouter() {
	with(pluginManager) {
		id("com.google.devtools.ksp") //ksp
		id("kotlin-parcelize")
	}
	dependencies {
		ksp(getLib("therouter-apt"))
		implementation(getLib("therouter-router"))

		//如果自己不是lib_route说明是业务模块，则添加依赖
		if (name != "lib_route") {
			//使用 project(":lib_route") 通过字符串获取本地模块实例
			add("implementation", project(":lib_route"))
		}
	}
}

//使用数据库
fun Project.useRoom(){
	with(pluginManager){
		id("com.google.devtools.ksp")//ksp
	}
	dependencies{
		implementation(getLib("androidx-room-runtime"))
		implementation(getLib("converter-gson"))
		ksp(getLib("room-compiler"))
		implementation(getLib("room.ktx"))

		if (name != "lib_database") {
			//使用 project(":lib_database") 通过字符串获取本地模块实例
			add("implementation", project(":lib_database"))
		}
	}
}