plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.bicycles"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bicycles"
        minSdk = 26
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

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation("com.airbnb.android:lottie:6.0.1")
    implementation ("com.auth0:java-jwt:3.18.2")
    implementation(libs.constraintlayout)
    implementation(libs.converter.gson)
    implementation(libs.retrofit)
    implementation(libs.contentpager)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.github.Foysalofficial:NafisBottomNav:5.0")

}