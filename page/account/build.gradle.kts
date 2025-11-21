import com.onerainbow.buildlogic.plugins.useRoom
import com.onerainbow.buildlogic.plugins.useTheRouter

plugins {
    alias(libs.plugins.onerainbow.library)
}
useRoom()
useTheRouter()
dependencies {
    implementation(projects.libRoute)
    implementation(projects.libNet)
    implementation(projects.libBase)
    implementation(libs.bundles.projectBasic)
}