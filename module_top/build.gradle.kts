import com.onerainbow.buildlogic.plugins.useTheRouter

plugins {
	alias(libs.plugins.onerainbow.library)
}
useTheRouter()
dependencies {
	implementation(libs.glide)
	implementation(projects.libNet)
	implementation(projects.moduleSeek)
	implementation(projects.libBase)
	implementation(libs.bundles.projectBasic)
}