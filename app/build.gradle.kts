plugins {
    alias(libs.plugins.onerainbow.application)
}
dependencies {
    implementation(projects.page.musicplayer)
    implementation(projects.page.account)
    implementation(projects.page.home)
    implementation(projects.components.route)
    implementation(projects.components.base)
    implementation(libs.bundles.projectBasic)
}