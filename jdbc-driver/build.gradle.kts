val awsJavaSdk = "1.7.4"
val hadoopAws = "2.7.3"
val scalaVersion = "2.11.8"
val sparkDariaVersion = "0.31.0-s_2.11"
val sparkVersion = "2.4.3"

group = "com.sparkplayground"
version = "1.0-SNAPSHOT"

dependencies {
    compile("org.scala-lang:scala-library:$scalaVersion")
    compile("org.scala-lang:scala-reflect:$scalaVersion")
    compile("org.scala-lang:scala-compiler:$scalaVersion")
    compile("org.apache.spark:spark-core_2.11:$sparkVersion")
    compile("org.apache.spark:spark-mllib_2.11:$sparkVersion")
    compile("org.apache.spark:spark-sql_2.11:$sparkVersion")
    compile("org.apache.hadoop:hadoop-aws:$hadoopAws")
    compile("com.amazonaws:aws-java-sdk:$awsJavaSdk")
    compile("mrpowers:spark-daria:$sparkDariaVersion")
    compile("com.datastax.spark:spark-cassandra-connector_2.11:2.4.1")

    compile(files("libs/CassandraJDBC42.jar"))
    compile(files("libs/cassandra-driver-core-3.1.1.jar"))
    compile(files("libs/cassandra-driver-extras-3.1.1.jar"))
    compile(files("libs/cassandra-driver-mapping-3.1.1.jar"))

    testImplementation("org.scalatest:scalatest_2.11:3.0.6")
    testImplementation("org.scalamock:scalamock_2.11:4.1.0")
    testImplementation("org.mockito:mockito-core:2.18.3")
    testImplementation("log4j:log4j:1.2.17")
    testImplementation("org.pegdown:pegdown:1.6.0")
}

