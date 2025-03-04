plugins {
    application
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.example"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.12")
    implementation("com.sedmelluq:lavaplayer:1.3.77")
}


application {
    mainClass.set("MainKt")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
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
