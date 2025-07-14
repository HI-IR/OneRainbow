plugins {
    //alias(libs.plugins.android.library)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}
val isBuildModule = true

android {
    namespace = "com.onerainbow.module.home"
    compileSdk = 35

    defaultConfig {
        applicationId  = "com.example.module_login"
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //consumerProguardFiles("consumer-rules.pro")
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
    sourceSets["main"].manifest.srcFile(
        if (isBuildModule)
            "src/main/debug/AndroidManifest.xml"
        else
            "src/main/AndroidManifest.xml"
    )
}

dependencies {
    implementation(project(":lib_base"))
    implementation(project(":lib_net"))
    implementation(project(":module_recommend"))
    implementation(project(":module_top"))
    implementation(project(":module_mv"))
    implementation(project(":module_user"))

    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    ksp("cn.therouter:apt:1.2.4")
    implementation ("cn.therouter:router:1.2.4")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}