plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.onerainbow.lib.base"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    ksp("cn.therouter:apt:1.2.4")
    implementation ("cn.therouter:router:1.2.4")

    // Jetpack Security
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    //datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //LifeCycle
    api ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.0")
    api ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0")

    api("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")



    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.viewbinding)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}