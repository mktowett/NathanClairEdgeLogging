plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.mk.jetpack.edgencg"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField( "String", "BASE_URL", "\"https://api.thescout.app/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField( "String", "BASE_URL", "\"https://api.thescout.app/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    packaging {
        resources {
            excludes.add("/META-INF/AL2.0")
            excludes.add("/META-INF/LGPL2.1")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.hilt.common)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    //timber
    implementation(libs.timber)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofitConverterGson)
    implementation(libs.okhttp.lib)

    //mockk testing
    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockkAndroid)

    //room
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)

    //coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    //work manager
    implementation(libs.work.runtime)

    // kotlinx serialization
    implementation(libs.kotlinx.serialization.json)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}