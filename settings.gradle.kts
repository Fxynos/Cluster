pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            version("minSdk", "27")
            version("targetSdk", "34")
            version("versionName", "1.0")
            version("versionCode", "1")

            version("androidPlugin", "8.3.1")

            version("coil", "2.5.0")
            version("retrofit", "2.9.0")
            version("junit", "5.9.2")
            version("lifecycle", "2.7.0")

            plugin("android-application", "com.android.application").versionRef("androidPlugin")
            plugin("android-library", "com.android.library").versionRef("androidPlugin")
            plugin("android.kotlin", "org.jetbrains.kotlin.android").version("1.9.10")
            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").version("1.8.20")

            library("support-annotations", "com.android.support:support-annotations:28.0.0")

            library("coil", "io.coil-kt", "coil").versionRef("coil")
            library("coil-compose", "io.coil-kt", "coil-compose").versionRef("coil")

            library("retrofit", "com.squareup.retrofit2", "retrofit").versionRef("retrofit")
            library("gson-converter", "com.squareup.retrofit2", "converter-gson").versionRef("retrofit")
            library("okhttp", "com.squareup.okhttp3:okhttp:4.12.0")

            library("vk", "com.vk.api:sdk:1.0.14")

            library("junit-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("junit-params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")

            library("lifecycle", "androidx.lifecycle", "lifecycle-runtime-ktx").versionRef("lifecycle")
            library("lifecycle-viewmodel", "androidx.lifecycle", "lifecycle-viewmodel-ktx").versionRef("lifecycle")
            library("lifecycle-viewmodel-compose", "androidx.lifecycle", "lifecycle-viewmodel-compose").versionRef("lifecycle")

            library("coroutines", "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
            library("core", "androidx.core:core-ktx:1.12.0")
            library("appcompat", "androidx.appcompat:appcompat:1.6.1")

            library("compose-navigation", "androidx.navigation:navigation-compose:2.7.7")
            library("compose-activity", "androidx.activity:activity-compose:1.8.2")
            library("compose-bom", "androidx.compose:compose-bom:2024.03.00")
            library("compose-ui", "androidx.compose.ui", "ui").withoutVersion()
            library("compose-ui-graphics", "androidx.compose.ui", "ui-graphics").withoutVersion()
            library("compose-ui-tooling", "androidx.compose.ui", "ui-tooling").withoutVersion()
            library("compose-ui-tooling-preview", "androidx.compose.ui", "ui-tooling-preview").withoutVersion()
            library("compose-ui-test-junit4", "androidx.compose.ui", "ui-test-junit4").withoutVersion()
            library("compose-ui-test-manifest", "androidx.compose.ui", "ui-test-manifest").withoutVersion()
            library("compose-material3", "androidx.compose.material3", "material3").withoutVersion()
            library("compose-foundation", "androidx.compose.foundation:foundation:1.6.4")
        }
    }
}
rootProject.name = "Cluster"
include(":app")
include(":libtd")
