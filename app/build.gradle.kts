plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("android")
    kotlin("kapt")
}

kapt {
    correctErrorTypes = true
}

android {
    compileSdk = Versions.compile_sdk_version
    buildToolsVersion = Versions.build_tools_version

    defaultConfig {
        applicationId = "co.iskv.crypto"
        minSdk = Versions.min_sdk_version
        targetSdk = Versions.target_sdk_version
        versionCode = Versions.app_version_code
        versionName = Versions.app_version_name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    flavorDimensions.add("client")
    productFlavors {
        create("clientTest") {
            dimension = "client"
            applicationIdSuffix = ".test"
            versionNameSuffix = "-test"
            buildConfigField("String", "END_POINT", "\"https://rest-sandbox.coinapi.io\"")
            buildConfigField("String", "COINAPI_TOKEN", "\"DE71760B-A201-4F59-80E0-49F39B661652\"")
        }
        create("clientProduction") {
            dimension = "client"
            buildConfigField("String", "END_POINT", "\"https://rest.coinapi.io\"")
            buildConfigField("String", "COINAPI_TOKEN", "\"DE71760B-A201-4F59-80E0-49F39B661652\"")
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures.dataBinding = true
    buildFeatures.viewBinding = true

}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7")
    implementation(AndroidX.appcompat)
    implementation(AndroidX.core_ktx)

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation(AndroidX.test_runner)
    androidTestImplementation(AndroidX.espresso_core)

    implementation(Moxy.moxy)
    kapt(Moxy.compiler)
    implementation(Moxy.android_x)
    implementation(Moxy.ktx)

    implementation(Toothpick.ktp)
    kapt(Toothpick.compiler)

    implementation(Cicerone.cicerone)

    implementation(Constraint.constraint)
    implementation(Google.material)

    implementation(Epoxy.epoxy)
    kapt(Epoxy.processor)

    implementation(Rx.rx_java)
    implementation(Rx.rx_android)

    implementation(Retrofit.retrofit)
    implementation(Retrofit.gson)
    implementation(Retrofit.adapter)

    implementation(Lottie.lottie_animation)
    implementation(Picasso.picasso)

    implementation(OkHttp.okhttp_server)
    implementation(OkHttp.okhttp_logging)

    implementation(JodaTime.joda_time)

}