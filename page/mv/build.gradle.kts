import com.onerainbow.buildlogic.plugins.useNet
import com.onerainbow.buildlogic.plugins.useTheRouter

plugins {
    alias(libs.plugins.onerainbow.library)
}
useTheRouter()
useNet()
dependencies {
    implementation(projects.components.base)
    implementation(projects.page.search)
    implementation(libs.bundles.projectBasic)
}