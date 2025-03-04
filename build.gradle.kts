plugins {
    application
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "2.0.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

application.mainClass = "dev.kuchen.MainKt"
group = "com.example"
version = "1.0"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

kotlin {
    jvmToolchain(22)
}

val kotlinxSerializationVersion = "1.7.3"
val kamlVersion = "0.65.0"

val jdaVersion = "5.2.1"
val jdaKtxVersion = "0.12.0"
val logbackVersion = "1.5.6"
val lavaPlayerVersion = "1.3.77"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("com.charleskorn.kaml:kaml:$kamlVersion")

    implementation("net.dv8tion:JDA:$jdaVersion") {
        exclude(module = "opus-java")
    }
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("club.minnced:jda-ktx:$jdaKtxVersion")
    implementation("com.sedmelluq:lavaplayer:$lavaPlayerVersion")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks {
    shadowJar {
        archiveBaseName.set("discord-bot")
        archiveVersion.set("")
        archiveClassifier.set("")
    }
}
