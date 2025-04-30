plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.keesp.dragonballapi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.keesp.dragonballapi"
        minSdk = 28
        targetSdk = 35
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    implementation(libs.volley)
    implementation (libs.glide)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


}