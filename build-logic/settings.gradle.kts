@file:Suppress("UnstableApiUsage")
// 开启模块的简化依赖方式
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// 插件管理配置
pluginManagement {
	repositories {
		gradlePluginPortal()
		google()
		mavenCentral()
		maven("https://maven.aliyun.com/repository/public")
		maven("https://maven.aliyun.com/repository/google")
		maven("https://jitpack.io")
		mavenLocal() // 本地仓库，位置在 用户名/.m2/ 下
	}
}

// 依赖解析管理配置
dependencyResolutionManagement {
	// 配置依赖仓库
	repositories {
		google() // Google的Maven仓库
		mavenCentral() // Maven中央仓库
	}

	// 开启 versionCatalogs 功能
	versionCatalogs {
		// 创建一个名为"libs"的版本目录
		create("libs") {
			// 从项目根目录的gradle/libs.versions.toml文件中加载版本信息
			from(files("../gradle/libs.versions.toml"))
		}
	}
}

rootProject.name = "build-logic"
//约定插件
include(":plugins")
