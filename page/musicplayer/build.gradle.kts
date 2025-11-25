import com.onerainbow.buildlogic.plugins.useMedia3
import com.onerainbow.buildlogic.plugins.useNet
import com.onerainbow.buildlogic.plugins.useTheRouter
import com.onerainbow.buildlogic.plugins.useRoom
plugins {
    alias(libs.plugins.onerainbow.library)
}
useTheRouter()
useRoom()
useNet()
useMedia3()
dependencies {
    implementation(projects.components.base)
    implementation(libs.bundles.projectBasic)
    implementation(libs.androidx.paging.runtime)
    implementation(projects.page.share)
}