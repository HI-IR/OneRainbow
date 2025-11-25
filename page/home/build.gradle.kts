import com.onerainbow.buildlogic.plugins.useNet
import com.onerainbow.buildlogic.plugins.useRoom
import com.onerainbow.buildlogic.plugins.useTheRouter

plugins {
    alias(libs.plugins.onerainbow.library)
}
useTheRouter()
useRoom()
useNet()
dependencies {
    implementation(libs.bundles.projectBasic)
    implementation(projects.components.base)

    implementation(projects.page.recommend)
    implementation(projects.page.top)
    implementation(projects.page.search)
    implementation(projects.page.musicplayer)
    implementation(projects.page.mv)
}