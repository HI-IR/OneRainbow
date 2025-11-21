plugins {
    alias(libs.plugins.onerainbow.application)
}
dependencies {
    implementation(projects.page.musicplayer)
    implementation(projects.page.account)
    implementation(projects.page.home)
    implementation(projects.libRoute)
    implementation(projects.libBase)
    implementation(libs.bundles.projectBasic)
}