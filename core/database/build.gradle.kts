plugins {
    alias(libs.plugins.rawgioclient.android.library)
    alias(libs.plugins.rawgioclient.android.room)
    alias(libs.plugins.rawgioclient.unit.test)
    alias(libs.plugins.tech.apter.robolectric)
}

android {
    namespace = "mr.liks.core.database"

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

// todo отрефакторить на этапе рабочего MVP
tasks.withType<Test> {
    exclude("**/DatabaseTest.class")
}

dependencies {
    implementation(libs.koin.android)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.robolectric)
}