plugins {
    //alias(libs.plugins.android.application)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id ("kotlin-parcelize")

}

android {
    namespace = "com.onerainbow.module.musicplayer"
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
    buildFeatures{
        viewBinding = true
    }

}

dependencies {
    //数据持久化
    implementation ("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    //数据库
    implementation(project(":lib_database"))

    //媒体框架
    implementation ("androidx.media3:media3-exoplayer:1.7.1")
    implementation ("androidx.media3:media3-ui:1.7.1")

    implementation ("com.github.bumptech.glide:glide:4.12.0")

    implementation(libs.androidx.paging.runtime)

    ksp("cn.therouter:apt:1.2.4")
    implementation ("cn.therouter:router:1.2.4")
    implementation(project(":module_share"))
    implementation(project(":lib_route"))
    implementation(project(":lib_net"))
    implementation(project(":lib_base"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.media3.session)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}