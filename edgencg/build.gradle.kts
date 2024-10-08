plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.dokka)
    id("maven-publish")
    kotlin("kapt")
}

android {
    namespace = "com.mk.jetpack.edgencg"
    compileSdk = 34

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

    packaging {
        resources {
            excludes.add("/META-INF/AL2.0")
            excludes.add("/META-INF/LGPL2.1")
        }
    }

    subprojects {
        apply(plugin = "org.jetbrains.dokka")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.hilt.common)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
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
    implementation(libs.logging.interceptor.lib)

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

    //dokka
    dokkaPlugin(libs.dokka.android)

    //datastore
    implementation(libs.datastore.preferences)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

afterEvaluate {
    publishing {
        publications {
            create("release", MavenPublication::class) {
                from(components["release"])

                groupId = "com.github.mkowett"
                artifactId = "edgencg"
                version = "1.0.0"
            }
        }
    }
}

//com.mk.jetpack.nathanclairedgelogging