plugins {
    alias(libs.plugins.rawgioclient.android.feature)
    alias(libs.plugins.rawgioclient.unit.test)
}

android {
    namespace = "mr.liks.feature.settings.impl"

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