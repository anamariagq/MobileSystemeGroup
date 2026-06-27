plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.dokka)
}

android {
    namespace = "com.example.todoapp"
    compileSdk {
        version = release(34)
    }

    defaultConfig {
        applicationId = "com.example.todoapp"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("com.kizitonwose.calendar:view:2.0.0")

    //UI libs for sdk 34
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.preference:preference:1.2.1")

    //AndroidX core libs
    implementation("androidx.core:core:1.13.1")
    implementation("androidx.core:core-ktx:1.13.1")

    //new libs replacements
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // <-- ¡AQUÍ ESTÁ!
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")       // <-- ¡AQUÍ ESTÁ!
    implementation("androidx.preference:preference:1.2.1")

    //Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)

    //Room
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
}

