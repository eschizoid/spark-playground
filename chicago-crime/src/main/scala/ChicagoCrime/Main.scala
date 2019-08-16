package ChicagoCrime

object Main extends App with SparkSupport {
  val chicagoCrimeStreamWriter = ChicagoCrimeStreamWriter()
  val communityAreaWriter = CommunityAreaWriter()
  communityAreaWriter.start()
  //chicagoCrimeStreamWriter.start()
}
