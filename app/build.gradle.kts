plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services) // Plugin cho Google Services (Firebase)
}

android {
    namespace = "com.sinhvien.nhom11_app_dat_tiec"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sinhvien.nhom11_app_dat_tiec"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Core libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Firebase dependencies
    implementation(libs.firebase.database) // Firebase Realtime Database
    implementation(libs.firebase.auth)     // Firebase Authentication

    // Thêm các thư viện cần thiết khác
    implementation("androidx.cardview:cardview:1.0.0") // CardView cho layout trước đó
    implementation("com.google.android.material:material:1.12.0") // BottomNavigationView

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}