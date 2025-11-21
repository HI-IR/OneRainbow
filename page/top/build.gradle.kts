import com.onerainbow.buildlogic.plugins.useTheRouter

plugins {
	alias(libs.plugins.onerainbow.library)
}
useTheRouter()
dependencies {
	implementation(projects.libNet)
	implementation(projects.page.search)
	implementation(projects.libBase)
	implementation(libs.bundles.projectBasic)
}