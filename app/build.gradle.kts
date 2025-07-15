plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("therouter")
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.onerainbow.onerainbow"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.onerainbow.onerainbow"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    debugImplementation ("com.squareup.leakcanary:leakcanary-android:2.8.1")
    implementation(project(":module_account"))
    implementation(project(":module_home"))
    implementation(project(":lib_route"))
    implementation(project(":lib_base"))
    ksp("cn.therouter:apt:1.2.4")
    implementation ("cn.therouter:router:1.2.4")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}