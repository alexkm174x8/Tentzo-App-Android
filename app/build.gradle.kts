plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.apptentzo_android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.apptentzo_android"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }


        buildConfigField("String", "METEOMATICS_USERNAME", "\"${project.findProperty("METEOMATICS_USERNAME") ?: ""}\"")
        buildConfigField("String", "METEOMATICS_PASSWORD", "\"${project.findProperty("METEOMATICS_PASSWORD") ?: ""}\"")

        // Using manifest placeholders for the API key
        manifestPlaceholders["com.google.android.geo.API_KEY"] = findProperty("MAPS_API_KEY") as String

    }

    buildTypes {
        debug {
            buildConfigField("String", "MAPS_API_KEY", "\"${findProperty("MAPS_API_KEY")}\"")
        }
        release {
            buildConfigField("String", "MAPS_API_KEY", "\"${findProperty("MAPS_API_KEY")}\"")
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-storage-ktx:20.2.0")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation ("com.google.maps.android:maps-ktx:3.4.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation ("androidx.activity:activity-compose:1.7.2")
    // Retrofit para realizar solicitudes HTTP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Converter para JSON usando Gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Coroutines para manejar tareas asíncronas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    // Coil para cargar imágenes
    implementation("io.coil-kt:coil-compose:2.1.0")
    // OkHttp para manejo de autenticación básica
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    // Interceptor de logging para OkHttp
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.navigation.compose)

    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.maps.android:maps-compose:2.12.0")
}
