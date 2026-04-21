plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
dependencies {

    // Paging
    implementation(libs.androidx.paging.common)

    // Mockk & Assertions
    testImplementation(libs.mockk)
    testImplementation(libs.kotest.assertions.core)

    implementation(libs.javax.inject)
    testImplementation(libs.junit)
}