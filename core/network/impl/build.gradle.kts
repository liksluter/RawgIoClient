import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.rawgioclient.android.library)
    alias(libs.plugins.rawgioclient.kotlin.serialization)
    alias(libs.plugins.rawgioclient.unit.test)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(FileInputStream(localPropertiesFile))
    }
}

android {
    namespace = "mr.liks.core.network.impl"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
        val rawgApiKey = localProperties.getProperty("rawg.io.api.key") ?: "default_key"
        buildConfigField("String", "RAWG_API_KEY", "\"$rawgApiKey\"")
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://api.rawg.io/api\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"https://api.rawg.io/api\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:network:api"))

    implementation(libs.koin.android)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.mock)
    implementation(libs.timber)
}