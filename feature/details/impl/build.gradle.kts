plugins {
    alias(libs.plugins.rawgioclient.android.feature)
}

android {
    namespace = "mr.liks.feature.details.impl"

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