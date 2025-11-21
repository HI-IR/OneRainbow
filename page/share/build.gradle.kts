plugins {
    alias(libs.plugins.onerainbow.library)
}
dependencies {
    implementation(project(":lib_base"))
    implementation(libs.bundles.projectBasic)
}