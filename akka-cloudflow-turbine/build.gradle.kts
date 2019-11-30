import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.AppendingTransformer

val akkaVersion = "2.5.25"
val akkaHttpVersion = "10.1.10"
val akkaStreamsVersion = "1.1.2"
val akkaCloudflowVersion = "1.3.0-M1"
val avroVersion = "1.9.1"
val logbackVersion = "1.2.3"
val scalaMinorVersion = "2.12.10"
val scalaVersion = "2.12"
val sparkDariaVersion = "0.31.0-s_${scalaVersion}"
val sparkVersion = "2.4.3"

group = "com.sparkplayground"
version = "1.0-SNAPSHOT"

dependencies {
    //Scala
    compileOnly("org.scala-lang:scala-library:$scalaMinorVersion")
    compileOnly("org.scala-lang:scala-reflect:$scalaMinorVersion")
    compileOnly("org.scala-lang:scala-compiler:$scalaMinorVersion")

    //Spark
    compileOnly("org.apache.spark:spark-core_${scalaVersion}:$sparkVersion")
    compileOnly("org.apache.spark:spark-mllib_${scalaVersion}:$sparkVersion")
    compileOnly("org.apache.spark:spark-sql_${scalaVersion}:$sparkVersion")
    compileOnly("mrpowers:spark-daria:$sparkDariaVersion")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Avro
    implementation("org.apache.avro:avro:$avroVersion")
    implementation("org.oedura:scavro_2.12:1.0.3")

    //Akka
    implementation("com.lightbend.akka:akka-stream-alpakka-file_${scalaVersion}:${akkaStreamsVersion}")
    implementation("com.lightbend.cloudflow:cloudflow-akka_${scalaVersion}:${akkaCloudflowVersion}")
    implementation("com.lightbend.cloudflow:cloudflow-akka-util_${scalaVersion}:${akkaCloudflowVersion}")
    implementation("com.lightbend.cloudflow:cloudflow-streamlets_${scalaVersion}:${akkaCloudflowVersion}")
    implementation("com.typesafe.akka:akka-http-spray-json_${scalaVersion}:${akkaHttpVersion}")
    implementation("com.typesafe.akka:akka-actor_${scalaVersion}:${akkaVersion}")

    testImplementation("org.scalatest:scalatest_${scalaVersion}:3.0.6")
    testImplementation("org.scalamock:scalamock_${scalaVersion}:4.1.0")
    testImplementation("org.mockito:mockito-core:2.18.3")
    testImplementation("log4j:log4j:1.2.17")
    testImplementation("org.pegdown:pegdown:1.6.0")
    testImplementation("com.typesafe.akka:akka-http-testkit_${scalaVersion}:${akkaHttpVersion}")
}

tasks {
    named<ShadowJar>("shadowJar") {
        manifest {
            attributes(mapOf("Main-Class" to "FlightDelayAnalysis.Main"))
        }
        dependencies {
            exclude("*csv*")
        }
        transform(AppendingTransformer::class.java) {
            resource = "reference.conf"
        }
    }
    register<Exec>("avroScalaGenerateSpecific") {
        commandLine = listOf("sbt", "avroScalaGenerateSpecific")
    }
    register<Copy>("avroCaseClass") {
        from("target/scala-2.12/src_managed/main/compiled_avro/sensordata")
        into("src/main/scala/sensordata")
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

scalafmt {
    configFilePath = ".scalafmt.conf"
}
