import com.onerainbow.buildlogic.plugins.useRoom
import com.onerainbow.buildlogic.plugins.useTheRouter

plugins {
    alias(libs.plugins.onerainbow.library)
}
useTheRouter()
useRoom()
dependencies {
    implementation(libs.bundles.projectBasic)
    implementation(projects.libRoute)
    implementation(projects.libBase)
    implementation(projects.libNet)
    implementation(projects.libDatabase)
    implementation(projects.page.recommend)
    implementation(projects.page.top)
    implementation(projects.page.search)
    implementation(projects.page.musicplayer)
    implementation(projects.page.mv)
}