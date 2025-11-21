import com.onerainbow.buildlogic.plugins.useTheRouter

plugins {
    alias(libs.plugins.onerainbow.library)
}
useTheRouter()

dependencies {
    implementation(project(":lib_net"))
    implementation(project(":lib_route"))
    implementation(project(":lib_base"))
    implementation(projects.page.search)
    implementation(libs.bundles.projectBasic)

}