import com.google.firebase.appdistribution.gradle.firebaseAppDistribution

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.firebaseDistribution)
}

android {
    namespace = "com.example.test"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.test"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            firebaseAppDistribution {
                val serviceCredentialsPath: String =
                    System.getenv("SERVICE_CREDENTIALS_PATH")
                    ?: "app/serviceCredentialsFile.json"
                val releaseNotesPath: String =
                    System.getenv("RELEASE_NOTES_PATH")
                        ?: "app/src/releaseNotes.txt"
                serviceCredentialsFile = serviceCredentialsPath
                artifactType = "APK"
                releaseNotesFile = releaseNotesPath
                testers = ""
            }
        }

        getByName("debug") {
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

firebaseAppDistribution {
    val serviceCredentialsPath: String =
        System.getenv("SERVICE_CREDENTIALS_PATH")
            ?: "app/serviceCredentialsFile.json"
    val releaseNotesPath: String =
        System.getenv("RELEASE_NOTES_PATH")
            ?: "app/src/releaseNotes.txt"
    serviceCredentialsFile = serviceCredentialsPath
    artifactType = "APK"
    releaseNotesFile = releaseNotesPath
    testers = ""
}

task("appDistributionToFb") {
    dependsOn("bundleDebug")
    dependsOn("appDistributionUploadDebug")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-analytics")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}