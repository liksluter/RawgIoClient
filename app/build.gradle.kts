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
    implementation(project(":feature:feed:api"))
    implementation(project(":feature:feed:impl"))
    implementation(project(":feature:details:api"))
    implementation(project(":feature:details:impl"))
    implementation(project(":feature:search:api"))
    implementation(project(":feature:search:impl"))
    implementation(project(":feature:settings:api"))
    implementation(project(":feature:settings:impl"))

    implementation(project(":core:common"))
    implementation(project(":core:design-system"))
    implementation(project(":core:navigation"))
    implementation(project(":core:network:impl"))
    implementation(project(":core:database"))
    implementation(project(":core:media"))
    implementation(project(":core:ui"))

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.timber)

    implementation(libs.haze)
    implementation(libs.haze.materials)
}