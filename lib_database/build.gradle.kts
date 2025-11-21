import com.onerainbow.buildlogic.plugins.useRoom

plugins {
    alias(libs.plugins.onerainbow.library)
}
useRoom()
dependencies {
    implementation(projects.libBase)
}