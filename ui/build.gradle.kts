plugins {
    application
    id("org.openjfx.javafxplugin") version "0.0.11"
}

application {
    mainClass.set("dev.taranys.MainKt")
}

dependencies {
    implementation(project(":kafka"))
    implementation("no.tornado:tornadofx:1.7.20")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.6.0")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.10")
}

javafx {
    version = "11.0.2"
    modules = listOf("javafx.controls", "javafx.graphics")
}