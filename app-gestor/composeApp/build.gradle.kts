import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import java.net.URI

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

android {
    namespace = "com.appGestor.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.appGestor.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

// Configuración obligatoria para Room Gradle Plugin
room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    jvmToolchain(21)

    @Suppress("DEPRECATION")
    androidTarget()
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.ktor.client.okhttp)

            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.config)
            implementation(libs.firebase.database)
            implementation(libs.firebase.messaging)
            implementation(libs.firebase.analytics)
            implementation(libs.kotlinx.coroutines.play.services)
            
            // WorkManager
            implementation(libs.androidx.work.runtime.ktx)
        }
        commonMain.dependencies {
            implementation(project(":designsystem"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            
            implementation(libs.compose.materialIconsCore)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            
            // Koin Core y Compose
            implementation(libs.koin.core)
            implementation("io.insert-koin:koin-compose:4.0.1")
            implementation("io.insert-koin:koin-compose-viewmodel:4.0.1")

            // Room
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
    // Procesador de Room con KSP
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

// Tarea para descargar traducciones de Loco compatible con Gradle 8+
abstract class DownloadLocoTask : DefaultTask() {
    @get:Input
    abstract val key: Property<String>

    @get:Input
    abstract val locales: MapProperty<String, String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun download() {
        val apiKey = key.get().trim()
        if (apiKey.isEmpty()) {
            throw GradleException("locoKey not found in local.properties")
        }

        locales.get().forEach { (locoLocale, folderName) ->
            println("Downloading translations for $locoLocale...")
            val url = "https://localise.biz/api/export/locale/$locoLocale.xml?key=$apiKey"
            
            val destFolder = outputDir.get().dir(folderName).asFile
            if (!destFolder.exists()) destFolder.mkdirs()
            
            val destinationFile = File(destFolder, "strings.xml")
            
            try {
                val content = URI(url).toURL().readText()
                destinationFile.writeText(content)
                println("✅ Saved to ${destinationFile.absolutePath}")
            } catch (e: Exception) {
                println("❌ Error downloading $locoLocale: ${e.message}")
            }
        }
    }
}

val locoProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

tasks.register<DownloadLocoTask>("downloadLocoTranslations") {
    group = "localization"
    description = "Downloads translations from Loco (localise.biz)"
    key.set(locoProperties.getProperty("locoKey") ?: "")
    locales.set(mapOf(
        "es-BO" to "values-es",
        "en-US" to "values",
        "it-IT" to "values-it"
    ))
    outputDir.set(project.file("src/commonMain/composeResources"))
}
