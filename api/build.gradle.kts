/*
 * Copyright (c) 2019 http://happyprg.com all rights reserved. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring")
    java
    application
}
application {
    mainClassName = "com.happyprg.blockchain.monitor.MainApplicationKt"
    group = "com.happyprg.blockchain.monitor"
    version = "0.0.1-SNAPSHOT"

    applicationDefaultJvmArgs = listOf(
        "-server",
        "-Djava.awt.headless=true",
        "-Xms128m",
        "-Xmx256m",
        "-XX:+UseG1GC",
        "-XX:MaxGCPauseMillis=100"
    )
}
springBoot {
    mainClassName = application.mainClassName
}

dependencies {
    implementation(kotlin("stdlib"))
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("org.jetbrains.kotlin:kotlin-reflect")
    compile("commons-validator:commons-validator:1.6")

    compile("org.springframework.boot:spring-boot-configuration-processor:2.1.6.RELEASE")
    compile("org.springframework.boot:spring-boot-autoconfigure-processor:2.1.6.RELEASE")

    kapt("org.springframework.boot:spring-boot-configuration-processor")
    kapt("org.springframework.boot:spring-boot-autoconfigure-processor")

    implementation("com.github.kittinunf.fuel:fuel:2.0.1")
    implementation("com.github.kittinunf.fuel:fuel-reactor:2.0.1")
    implementation("com.beust:klaxon:5.0.1")

    implementation("org.apache.commons:commons-lang3:3.8.1")
    implementation("com.google.guava:guava:22.0")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mustache")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.apache.commons:commons-collections4:4.3")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.7")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.jayway.jsonpath:json-path:2.4.0")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("junit")
    }
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testCompile("io.projectreactor:reactor-test")
}


jib {
    container {
        ports = listOf("8080")
        mainClass = application.mainClassName

        // good default intended for Java 8 containers
        jvmFlags = listOf(
            "-server",
            "-Djava.awt.headless=true",
            "-XX:+UnlockExperimentalVMOptions",
            "-XX:+UseCGroupMemoryLimitForHeap",
            "-XX:InitialRAMFraction=2",
            "-XX:MinRAMFraction=2",
            "-XX:MaxRAMFraction=2",
            "-XX:+UseG1GC",
            "-XX:MaxGCPauseMillis=100",
            "-XX:+UseStringDeduplication"
        )
    }
}

