import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.9"
}

application{
    mainClass.set("xyz.sushiland.gardenia.mo.MyApp")
}
group = "me.incorrectm"
version = "1.0"

javafx {
    version = "11.0.2"
    modules = listOf("javafx.controls", "javafx.graphics")
}

repositories {
    mavenCentral()
    jcenter()
//    mavenLocal()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.31")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.31")
    implementation("no.tornado:tornadofx:1.7.20")
    implementation("com.beust:klaxon:5.4")

}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}