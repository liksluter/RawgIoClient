plugins {
    alias(libs.plugins.rawgioclient.android.application)
    alias(libs.plugins.rawgioclient.android.application.compose)
    alias(libs.plugins.rawgioclient.unit.test)
}

android {
    namespace = "mr.liks.rawgioclient"

    defaultConfig {
        applicationId = "mr.liks.rawgioclient"
    }
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.koin.android)
}