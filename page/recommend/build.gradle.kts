import com.onerainbow.buildlogic.plugins.useTheRouter
plugins {
    alias(libs.plugins.onerainbow.library)
}
useTheRouter()
dependencies {
    implementation(projects.page.search)
    implementation(projects.page.share)
    implementation(project(":lib_base"))
    implementation(project(":lib_net"))
    implementation(project(":lib_route"))
    implementation(libs.bundles.projectBasic)
}