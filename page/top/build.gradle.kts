import com.onerainbow.buildlogic.plugins.useNet
import com.onerainbow.buildlogic.plugins.useTheRouter

plugins {
	alias(libs.plugins.onerainbow.library)
}
useTheRouter()
useNet()
dependencies {
	implementation(projects.page.search)
	implementation(projects.components.base)
	implementation(libs.bundles.projectBasic)
}