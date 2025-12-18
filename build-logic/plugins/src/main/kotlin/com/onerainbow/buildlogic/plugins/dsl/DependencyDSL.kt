package com.onerainbow.buildlogic.plugins.dsl

import com.onerainbow.buildlogic.plugins.config.getLib
import org.gradle.api.Project

/**
 * description ： 关于依赖的DSL
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/12/18 23:18
 */

fun Project.implementation(lib: String) {
	dependencies.add("implementation", getLib(lib))
}

fun Project.ksp(lib: String) {
	dependencies.add("ksp", getLib(lib))

}

fun Project.coreLibraryDesugaring(alias: String) {
	dependencies.add("coreLibraryDesugaring", alias)

}

fun Project.testImplementation(lib: String) {
	dependencies.add("testImplementation", getLib(lib))

}


fun Project.androidTestImplementation(lib: String) {
	dependencies.add("androidTestImplementation", getLib(lib))

}


fun Project.debugImplementation(lib: String) {
	dependencies.add("debugImplementation", getLib(lib))
}

