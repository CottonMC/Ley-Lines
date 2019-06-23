import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.31"
}

group = "io.github.cottonmc.leylines"
version = "1.0.0"

repositories {
    mavenCentral()
    //mavenLocal()
    maven { setUrl("http://server.bbkr.space:8081/artifactory/libs-release") }
    maven { setUrl("http://server.bbkr.space:8081/artifactory/libs-snapshot") }


}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(group="io.github.cottonmc", name="json-factory", version="0.5.0-beta.2-SNAPSHOT")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}