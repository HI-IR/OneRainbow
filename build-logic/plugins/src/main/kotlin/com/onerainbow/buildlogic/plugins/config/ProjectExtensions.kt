package com.onerainbow.buildlogic.plugins.config

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType

/**
 * description ： libs
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/11/17 22:16
 */

//获取项目的libs
val Project.versionLibs
	get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")


//简化获取依赖
fun Project.getLib(alias: String) = versionLibs.findLibrary(alias).get()

//简化获取版本
fun Project.getVersion(alias: String) = versionLibs.findVersion(alias).get().toString().toInt()

