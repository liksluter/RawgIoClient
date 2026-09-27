plugins {
    alias(libs.plugins.rawgioclient.android.library)
    alias(libs.plugins.rawgioclient.kotlin.serialization)
}

android {
    namespace = "mr.liks.feature.detais.api"

    defaultConfig {
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
}

dependencies {
    api(project(":core:navigation"))
}