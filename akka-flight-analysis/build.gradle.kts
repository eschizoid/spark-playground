import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.AppendingTransformer

val awsJavaSdkVersion = "1.7.4"
val akkaVersion = "2.5.25"
val hadoopAwsVersion = "2.7.3"
val logbackVersion = "1.2.3"
val scalaMinorVersion = "2.11.12"
val scalaVersion = "2.11"
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

    //Akka
    compile("com.typesafe.akka:akka-stream_${scalaVersion}:${akkaVersion}")
    compile("com.typesafe.akka:akka-actor_${scalaVersion}:${akkaVersion}")

    testImplementation("org.scalatest:scalatest_${scalaVersion}:3.0.6")
    testImplementation("org.scalamock:scalamock_${scalaVersion}:4.1.0")
    testImplementation("org.mockito:mockito-core:2.18.3")
    testImplementation("log4j:log4j:1.2.17")
    testImplementation("org.pegdown:pegdown:1.6.0")
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
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

scalafmt {
    configFilePath = ".scalafmt.conf"
}
