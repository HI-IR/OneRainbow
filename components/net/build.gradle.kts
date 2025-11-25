import com.onerainbow.buildlogic.plugins.useNet
plugins {
    alias(libs.plugins.onerainbow.library)
}
useNet()
dependencies {
    implementation(projects.components.base)
}