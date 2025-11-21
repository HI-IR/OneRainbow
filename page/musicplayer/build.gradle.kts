import com.onerainbow.buildlogic.plugins.useMedia3
import com.onerainbow.buildlogic.plugins.useTheRouter
import com.onerainbow.buildlogic.plugins.useRoom
plugins {
    alias(libs.plugins.onerainbow.library)
}
useTheRouter()
useRoom()
useMedia3()
dependencies {
    implementation(projects.page.share)
    implementation(projects.libDatabase)
    implementation(projects.libRoute)
    implementation(projects.libNet)
    implementation(projects.libBase)
    implementation(libs.bundles.projectBasic)
    implementation(libs.androidx.paging.runtime)
}