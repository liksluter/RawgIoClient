plugins {
    alias(libs.plugins.rawgioclient.android.feature)
    alias(libs.plugins.rawgioclient.unit.test4)
    alias(libs.plugins.roborazzi.plugin)
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
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":feature:settings:api"))

    implementation(project(":core:model"))
    implementation(project(":core:datastore"))
    implementation(project(":core:ui"))

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.timber)

    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.roborazzi.junit.rule)
}