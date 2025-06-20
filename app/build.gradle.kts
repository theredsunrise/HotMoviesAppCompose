import com.android.build.api.dsl.VariantDimension
import com.example.shared.encrypt
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serializable)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.hotmovies"
    compileSdk = 35

    signingConfigs {
        create("release") {
            val keystoreProperties = Properties().apply {
                load(FileInputStream(rootProject.file("keystore.properties")))
            }
            storeFile = file(keystoreProperties["storeFile"] as String)
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storePassword = keystoreProperties["storePassword"] as String
        }
    }
    defaultConfig {
        applicationId = "com.example.hotmovies"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val tmdbProperties = Properties().apply {
            load(FileInputStream(rootProject.file("tmdb.properties")))
        }
        buildConfigField("TMDB_API_KEY", (tmdbProperties["apiKey"] as String).encrypt(12))
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = false
            isJniDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "retrofit2.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    testOptions {
        this.unitTests.all {
            it.testLogging {
                events(
                    TestLogEvent.FAILED,
                    TestLogEvent.PASSED,
                    TestLogEvent.SKIPPED,
                    TestLogEvent.STANDARD_ERROR,
                    TestLogEvent.STANDARD_OUT
                )
                exceptionFormat = TestExceptionFormat.FULL
                showCauses = true
                showExceptions = true
                showStackTraces = true
            }
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

tasks.withType(JavaCompile::class.java) {
    options.compilerArgs.add("-Xlint:deprecation")
}

tasks.withType<Test> {
    jvmArgs = listOf("-XX:+EnableDynamicAgentLoading")
    systemProperty("isTest", true)
}

inline fun <reified ValueT> VariantDimension.buildConfigField(name: String, value: ValueT) {
    val resolvedValue = when (value) {
        is String -> "\"$value\""
        is Int -> "$value"
        is Boolean -> "$value"
        else -> value.toString()
    }
    buildConfigField(ValueT::class.java.simpleName, name, resolvedValue)
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraint.layout.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.lottie.compose)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.viewmodel)
    implementation(libs.androidx.viewmodel.savedstate)
    implementation(libs.androidx.downloadable.fonts.compose)
    implementation(libs.androidx.constraintlayout.core)
    implementation(libs.androidx.compose.adaptive)

    implementation(libs.androidx.datastore)
    implementation(libs.androidx.paging)
    implementation(libs.androidx.paging.compose)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.retrofit.logging.inteceptor)
    implementation(libs.retrofit.logging.inteceptor)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlin.json)
    implementation(libs.coil3.compose)
    implementation(libs.coil3.okhttp.compose)

    testImplementation(libs.mockk)
    testImplementation(libs.coroutine.tests)
    testImplementation(libs.junit)
    debugImplementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    releaseCompileOnly(libs.androidx.ui.tooling.preview)
    releaseCompileOnly(libs.androidx.ui.tooling)
    implementation(libs.hilt)
    implementation(libs.hilt.compose)

    ksp(libs.ksp)
}