import com.onerainbow.buildlogic.plugins.useTheRouter

plugins {
    alias(libs.plugins.onerainbow.application)
}
useTheRouter()
dependencies {
    implementation(projects.page.account)
    implementation(projects.page.home)
    implementation(projects.components.base)
    implementation(libs.bundles.projectBasic)
    implementation(projects.page.musicplayer.api)


    //让musicplayer进入APK打包
    runtimeOnly(projects.page.musicplayer)
}