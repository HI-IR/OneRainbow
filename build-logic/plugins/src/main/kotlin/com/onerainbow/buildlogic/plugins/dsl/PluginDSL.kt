package com.onerainbow.buildlogic.plugins.dsl

import org.gradle.api.plugins.PluginManager

/**
 * description ： 插件管理的DSL
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/12/18 23:19
 */
fun PluginManager.id(alias: String) {
	apply(alias)
}