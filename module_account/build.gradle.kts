plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    //alias(libs.plugins.android.application)
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.onerainbow.module.module.account"
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
//    sourceSets {
//        sourceSets.getByName("main"){
//            manifest.srcFile("src/main/debug/AndroidManifest.xml")
//        }
//    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation(project(":lib_database"))
    implementation(project(":lib_route"))
    implementation(project(":lib_net"))
    implementation(project(":lib_base"))
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.ui.desktop)
    ksp("cn.therouter:apt:1.2.4")
    implementation ("cn.therouter:router:1.2.4")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}