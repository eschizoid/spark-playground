import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val awsJavaSdk = "1.7.4"
val geosparkSparkCompatibleVersion = "2.3"
val geosparkVersion = "1.2.0"
val hadoopAws = "2.7.3"
val scalaVersion = "2.11.8"
val sparkDariaVersion = "0.31.0-s_2.11"
val sparkVersion = "2.4.3"

group = "com.sparkplayground"
version = "1.0-SNAPSHOT"

dependencies {
    compileOnly("org.scala-lang:scala-library:$scalaVersion")
    compileOnly("org.scala-lang:scala-reflect:$scalaVersion")
    compileOnly("org.scala-lang:scala-compiler:$scalaVersion")
    compileOnly("org.apache.spark:spark-core_2.11:$sparkVersion")
    compileOnly("org.apache.spark:spark-mllib_2.11:$sparkVersion")
    compileOnly("org.apache.spark:spark-sql_2.11:$sparkVersion")
    compileOnly("org.datasyslab:geospark:$geosparkVersion")
    compileOnly("org.datasyslab:geospark-sql_$geosparkSparkCompatibleVersion:$geosparkVersion")
    compileOnly("org.datasyslab:geospark-viz_$geosparkSparkCompatibleVersion:$geosparkVersion")
    compileOnly("org.datasyslab:sernetcdf:0.1.0")
    compileOnly("org.apache.hadoop:hadoop-aws:$hadoopAws")
    compileOnly("com.amazonaws:aws-java-sdk:$awsJavaSdk")
    compileOnly("mrpowers:spark-daria:$sparkDariaVersion")

    testImplementation("org.scalatest:scalatest_2.11:3.0.6")
    testImplementation("org.scalamock:scalamock_2.11:4.1.0")
    testImplementation("org.mockito:mockito-core:2.18.3")
    testImplementation("log4j:log4j:1.2.17")
    testImplementation("org.pegdown:pegdown:1.6.0")
}

tasks {
    named<ShadowJar>("shadowJar") {
        manifest {
            attributes(mapOf("Main-Class" to "ChicagoCrime.Main"))
        }
        dependencies {
            exclude("*.csv")
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
