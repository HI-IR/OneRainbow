plugins {
    alias(libs.plugins.onerainbow.application)
}
dependencies {
    implementation(projects.moduleMusicplayer)
    implementation(projects.moduleAccount)
    implementation(projects.moduleHome)
    implementation(projects.libRoute)
    implementation(projects.libBase)
    implementation(libs.bundles.projectBasic)
}