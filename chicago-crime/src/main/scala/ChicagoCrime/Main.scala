package ChicagoCrime

object Main extends App with SparkSupport {
  val transformer = Transformer()
  transformer.start()
}
