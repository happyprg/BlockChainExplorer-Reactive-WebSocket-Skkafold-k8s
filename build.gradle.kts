/*
 * Copyright (c) 2019 http://happyprg.com all rights reserved. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springBootVersion: String by project
val kotlinVersion: String by project
extra.set(
    "pkgVersions", mapOf(
        "springBootVersion" to springBootVersion,
        "kotlinVersion" to kotlinVersion
    )
)

val junitVersion: String by project
val reactorVersion: String by project
val reactorAddOnVersion: String by project
val reactorKafkaVersion: String by project
val mockitoVersion: String by project

plugins {
    kotlin("jvm") version "1.3.41"
    kotlin("plugin.allopen") version "1.3.41"
    kotlin("plugin.noarg") version "1.3.41"
    kotlin("plugin.spring") version "1.3.41"
    jacoco
    idea
    checkstyle
    id("com.github.johnrengelman.shadow") version "5.1.0" apply false
    id("org.springframework.boot") version "2.1.6.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.8.RELEASE" apply false
    id("com.google.cloud.tools.jib") version "1.3.0" apply false
}


allprojects {
    group = "com.happyprg.blockchain.monitor"
    version = "0.0.1-SNAPSHOT"

    repositories {
        jcenter()
        mavenCentral()
    }
    /**
     * Enable java incremental compilation.
     */
    tasks.withType<JavaCompile> {
        options.isIncremental = true
    }
}

subprojects {
    println("Enabling Kotlin Spring plugin in project ${project.name}...")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "kotlin-kapt")

    apply<JavaLibraryPlugin>()
    println("Enabling Spring Boot Dependency Management in project ${project.name}...")
    apply(plugin = "io.spring.dependency-management")

    apply(plugin = "com.google.cloud.tools.jib")

    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "checkstyle")
    apply(plugin = "idea")
    apply(plugin = "org.springframework.boot")


    tasks.withType<JacocoReport> {
        reports {
            xml.isEnabled = true
            html.isEnabled = false
            csv.isEnabled = false
        }
        val jacocoTestReport by tasks
        jacocoTestReport.dependsOn("test")
    }

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    checkstyle {
        configFile = file("$rootDir/config/checkstyle/checkstyle.xml")
        toolVersion = "7.8.2"
        isIgnoreFailures = false
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile>().configureEach {
        println("Configuring $name in project ${project.name}...")
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs = listOf("-Xdoclint:none", "-Xlint:none", "-nowarn")
    }

    configurations.all {
        exclude(module = "servlet-api")
        resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
    }
    dependencies {
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        "testImplementation"("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
        "testImplementation"("org.junit.jupiter:junit-jupiter-params:$junitVersion")
        "testImplementation"("org.junit.vintage:junit-vintage-engine:4.12.2")
        "testImplementation"("io.projectreactor:reactor-test:$reactorVersion")
        subprojects.forEach {
            archives(it)
        }
    }
}