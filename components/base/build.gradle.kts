import com.onerainbow.buildlogic.plugins.useTheRouter
plugins {
    alias(libs.plugins.onerainbow.library)
}
useTheRouter()
dependencies {
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.bundles.projectBasic)
}