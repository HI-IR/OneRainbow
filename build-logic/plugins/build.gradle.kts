import org.jetbrains.kotlin.gradle.dsl.JvmTarget


// 应用Kotlin DSL插件，使构建脚本能够使用Kotlin语言编写
plugins {
	`kotlin-dsl`
}
// 定义构建逻辑模块的组名
group = "com.onerainbow.buildlogic.plugins"

// 配置构建逻辑插件以目标 JDK 17
// 这与用于构建项目的 JDK 匹配，与设备上运行的内容无关
java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
	compilerOptions {
		jvmTarget = JvmTarget.JVM_17
	}
}

// 声明构建逻辑模块的依赖
dependencies {
	// 添加Android Gradle插件依赖（仅编译时需要）
	compileOnly(libs.android.gradlePlugin)
	// 添加Kotlin Gradle插件依赖（仅编译时需要）
	compileOnly(libs.kotlin.gradlePlugin)
	// 添加KSP注解处理器插件依赖（仅编译时需要）
	compileOnly(libs.ksp.gradlePlugin)
}

// 配置Gradle插件
gradlePlugin {
	plugins {
		//注册使用application插件
		register("yanyunAndroidApplicationPlugin") {
			id = "com.onerainbow.android.application"
			implementationClass = "com.onerainbow.buildlogic.plugins.AndroidApplicationPlugin"
		}

		//注册使用library插件
		register("yanyunAndroidLibraryPlugin") {
			id = "com.onerainbow.android.library"
			implementationClass = "com.onerainbow.buildlogic.plugins.AndroidLibraryPlugin"
		}
	}
}

// 配置任务以处理重复资源
tasks.withType<ProcessResources> {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}