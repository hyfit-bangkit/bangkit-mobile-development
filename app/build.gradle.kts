plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.scannerapp"
    compileSdk = 35 // Mempertahankan SDK versi 35 sesuai permintaan Anda

    defaultConfig {
        applicationId = "com.example.scannerapp"
        minSdk = 22
        targetSdk = 35 // Mempertahankan target SDK versi 35 sesuai permintaan Anda
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    // Menghapus buildFeatures untuk Compose
}

dependencies {
    // Material Design Library
    implementation("com.google.android.material:material:1.8.0")

    // AndroidX Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Camera Libraries
    implementation("androidx.camera:camera-core:1.5.0-alpha03")
    implementation("androidx.camera:camera-lifecycle:1.5.0-alpha03")
    implementation("androidx.camera:camera-view:1.5.0-alpha03")
    implementation("androidx.camera:camera-extensions:1.5.0-alpha03")
    implementation("androidx.camera:camera-camera2:1.5.0-alpha03")

    // TensorFlow Lite
    implementation("org.tensorflow:tensorflow-lite:2.13.0")

    // Glide for Image loading
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Networking Libraries (Retrofit + OkHttp)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
