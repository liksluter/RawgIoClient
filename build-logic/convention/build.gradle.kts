plugins {
    `kotlin-dsl`
}

group = "mr.liks.rawgioclient.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    implementation(libs.room.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    compileOnly("de.mannodermaus.gradle.plugins:android-junit5:${libs.versions.junit5Plugin.get()}")
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.rawgioclient.android.application.asProvider().get().pluginId
            implementationClass = "mr.liks.rawgioclient.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.rawgioclient.android.library.asProvider().get().pluginId
            implementationClass = "mr.liks.rawgioclient.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = libs.plugins.rawgioclient.android.feature.get().pluginId
            implementationClass = "mr.liks.rawgioclient.buildlogic.AndroidFeatureConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = libs.plugins.rawgioclient.android.application.compose.get().pluginId
            implementationClass = "mr.liks.rawgioclient.buildlogic.AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.rawgioclient.android.library.compose.get().pluginId
            implementationClass = "mr.liks.rawgioclient.buildlogic.AndroidLibraryComposeConventionPlugin"
        }
        register("androidRoom") {
            id = libs.plugins.rawgioclient.android.room.get().pluginId
            implementationClass = "mr.liks.rawgioclient.buildlogic.AndroidRoomConventionPlugin"
        }
        register("kotlinSerialization") {
            id = libs.plugins.rawgioclient.kotlin.serialization.get().pluginId
            implementationClass = "mr.liks.rawgioclient.buildlogic.KotlinSerializationConventionPlugin"
        }
        register("unitTest") {
            id = libs.plugins.rawgioclient.unit.test.get().pluginId
            implementationClass = "mr.liks.rawgioclient.buildlogic.UnitTestConventionPlugin"
        }
    }
}
