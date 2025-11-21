import com.onerainbow.buildlogic.plugins.useMedia3
import com.onerainbow.buildlogic.plugins.useRoom
import com.onerainbow.buildlogic.plugins.useTheRouter

plugins {
    alias(libs.plugins.onerainbow.library)
}
useRoom()
useTheRouter()

// 诶谁把VideoActivity写到了Search模块里面？？？？
useMedia3()
dependencies {
    implementation(projects.libDatabase)
    implementation(projects.page.musicplayer)
    implementation(projects.page.share)
    implementation(projects.libNet)
    implementation(projects.libRoute)
    implementation(projects.libBase)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.flexbox)
    implementation(libs.bundles.projectBasic)
}