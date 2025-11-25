import com.onerainbow.buildlogic.plugins.useNet
import com.onerainbow.buildlogic.plugins.useRoom
import com.onerainbow.buildlogic.plugins.useTheRouter

plugins {
    alias(libs.plugins.onerainbow.library)
}
useRoom()
useTheRouter()
useNet()
dependencies {
    implementation(projects.components.base)
    implementation(libs.bundles.projectBasic)
}