plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android") version "2.51"
    id ("kotlin-parcelize")
}

android {
    namespace = "com.example.pokemonapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pokemonapp"
        minSdk = 27
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

    hilt {
        enableAggregatingTask = false
    }
    buildFeatures {
        viewBinding = true
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

//Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation("androidx.palette:palette-ktx:1.0.0")

    //ProgressView
    implementation ("com.github.skydoves:progressview:1.1.3")

    //Retrofit, OkHttp
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")


    // Coroutines
    implementation ( "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ViewModel, LiveData, Lifecycle

    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation  ("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // android
    implementation ("androidx.fragment:fragment-ktx:1.7.0")
    implementation ("androidx.activity:activity-ktx:1.9.0")

    //hilt
    implementation ("com.google.dagger:hilt-android:2.51")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    kapt ("com.google.dagger:hilt-compiler:2.51")

    implementation("androidx.palette:palette-ktx:1.0.0")

    //android
    implementation ("androidx.fragment:fragment-ktx:1.7.0")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}