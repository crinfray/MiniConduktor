plugins {
    kotlin("jvm") version "1.4.32"
    application
    id("org.openjfx.javafxplugin") version "0.0.11"
}
group = "dev.taranys"
version = "0.1.0"

repositories {
    mavenCentral()
}

application {
    mainClass.set("dev.taranys.MainKt")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("no.tornado:tornadofx:1.7.20")
    testImplementation(kotlin("test-junit"))
}

javafx {
    version = "11.0.2"
    modules = listOf("javafx.controls", "javafx.graphics")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}