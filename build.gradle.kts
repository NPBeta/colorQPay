import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    java
}

group = "com.npbeta"
version = "2.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.31")
    implementation("com.google.zxing:core:3.4.1")
    implementation("cn.springboot:best-pay-sdk:1.3.4-BETA")
    implementation("com.alibaba:fastjson:1.2.78")
    implementation("org.apache.logging.log4j:log4j-api:2.16.0")
    implementation("org.apache.logging.log4j:log4j-core:2.16.0")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.0.1")
    implementation("mysql:mysql-connector-java:8.0.25")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.register<Jar>("uberJar") {
    archiveClassifier.set("uber")

    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

    manifest {
        attributes["Main-Class"] = "com.npbeta.colorQPay.Main"
    }

}
