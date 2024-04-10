plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
}

android {
    namespace = "com.vl.cluster"
    compileSdk = libs.versions.targetSdk.get().toInt()

    defaultConfig {
        applicationId = "com.vl.cluster"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources.excludes.add("META-INF/INDEX.LIST")
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/io.netty.versions.properties")
    }
}

dependencies {
    /* Telegram Data Library */
    implementation(project(":libtd"))

    /* Coil */ // loading images async for url right in composable
    implementation(libs.coil)
    implementation(libs.coil.compose)

    /* Retrofit2 */
    implementation(libs.retrofit)
    implementation(libs.gson.converter)
    implementation (libs.okhttp)

    /* VK SDK */
    implementation(libs.vk)
    
    /* JUnit 5 */
    testImplementation(libs.junit.engine)
    testImplementation(libs.junit.params)

    /* Android */
    implementation(libs.lifecycle)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.compose)

    implementation(libs.datastore.preferences)

    runtimeOnly(libs.coroutines)
    implementation(libs.core)
    implementation(libs.appcompat)

    implementation(libs.compose.navigation)
    implementation(libs.compose.activity)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}