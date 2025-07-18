plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.qrcodescanner"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.qrcodescanner"
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

   buildFeatures {
       dataBinding = true
       viewBinding = true
   }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //libary for sdp
    implementation ("com.intuit.sdp:sdp-android:1.0.6")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")

    //Google material design library
    implementation("com.google.android.material:material:1.12.0")

    //QR Scanner Library
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")

    //RoomDatabase library
    implementation("androidx.room:room-runtime:2.7.2")
    implementation("androidx.room:room-ktx:2.7.2")
    kapt("androidx.room:room-compiler:2.7.2")
    testImplementation("androidx.room:room-testing:2.7.2")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")



}