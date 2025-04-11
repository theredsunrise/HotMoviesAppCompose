import com.android.build.api.dsl.VariantDimension
import com.example.shared.encrypt
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serializable)
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
        buildConfigField("TMDB_BEARER", (tmdbProperties["bearer"] as String).encrypt(45))
        buildConfigField("TMDB_ACCOUNT_ID", (tmdbProperties["accountId"] as String).encrypt(32))
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
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

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    releaseCompileOnly(libs.androidx.ui.tooling.preview)
    releaseCompileOnly(libs.androidx.ui.tooling)
}