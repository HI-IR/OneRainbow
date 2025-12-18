package com.onerainbow.buildlogic.plugins.config

import com.android.build.api.dsl.CommonExtension
import com.onerainbow.buildlogic.plugins.dsl.androidTestImplementation
import com.onerainbow.buildlogic.plugins.dsl.coreLibraryDesugaring
import com.onerainbow.buildlogic.plugins.dsl.testImplementation
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * description ： 安卓的基础通用配置
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/11/17 22:59
 */
internal fun Project.kotlinAndroid(
	commonExtension: CommonExtension<*, *, *, *, *, *>
) {
	commonExtension.apply {
		val pathName = project.path.drop(1).replace(":", ".").replace("-", "_")
		namespace = "com.onerainbow.${pathName}"
		compileSdk = getVersion("compileSdk")

		defaultConfig {
			minSdk = versionLibs.findVersion("minSdk").get().toString().toInt()
			testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		}
		packaging {
			resources {
				excludes += listOf(
					"META-INF/gradle/incremental.annotation.processors",
					"META-INF/*.kotlin_module",
					"META-INF/AL2.0",
					"META-INF/LGPL2.1"
				)
			}
		}
		buildFeatures {
			viewBinding = true
		}

		compileOptions {
			sourceCompatibility = JavaVersion.VERSION_17
			targetCompatibility = JavaVersion.VERSION_17
			isCoreLibraryDesugaringEnabled = true
		}

		buildTypes {
			getByName("release") {
				isMinifyEnabled = false
				proguardFiles(
					getDefaultProguardFile("proguard-android-optimize.txt"),
					"proguard-rules.pro"
				)
			}
		}
		tasks.withType(KotlinCompile::class.java).configureEach {
			kotlinOptions {
				jvmTarget = "17"
			}
		}
		dependencies {
			// 单元测试
			testImplementation("junit")
			androidTestImplementation("junit")
			androidTestImplementation("junit")

			//兼容低版本的依赖
			coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
		}
	}
}