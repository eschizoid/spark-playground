plugins {
    id("idea")
    id("cz.alenkacz.gradle.scalafmt") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "5.0.0"
    id("com.github.maiflai.scalatest") version "0.25"
    kotlin("jvm") version "1.3.20"
}

allprojects {
    repositories {
        jcenter()
        maven(url = "http://dl.bintray.com/spark-packages/maven")
        maven(url = "http://repo.artima.com/releases")
    }
}

subprojects {
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "cz.alenkacz.gradle.scalafmt")
    apply(plugin = "com.github.maiflai.scalatest")
    apply(plugin = "java")
    apply(plugin = "scala")
}
