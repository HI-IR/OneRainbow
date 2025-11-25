import com.onerainbow.buildlogic.plugins.useMedia3
import com.onerainbow.buildlogic.plugins.useNet
import com.onerainbow.buildlogic.plugins.useRoom
import com.onerainbow.buildlogic.plugins.useTheRouter

plugins {
    alias(libs.plugins.onerainbow.library)
}
useRoom()
useTheRouter()

// 诶谁把VideoActivity写到了Search模块里面？？？？
useMedia3()
useNet()
dependencies {
    implementation(projects.page.musicplayer)
    implementation(projects.page.share)
    implementation(projects.components.base)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.flexbox)
    implementation(libs.bundles.projectBasic)
}