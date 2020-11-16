package tmdb

import scala.io.Source



object Main extends App {
  import Finder._

  val content = Source.fromURL("https://api.themoviedb.org/3/person/3894?api_key=0a3f9b1422abb8a0284f388056b138de&language=en-US")
  println(content.mkString)
  val lis_1 = findActorId("Leonardo", "DiCaprio")
  println(lis_1)
  val lis_2 = findActorMovies(6193)
  println(lis_2)
  val lis_3 = findMovieDirector(550)
  println(lis_3)
  val lis_4 = request(FullName("Emma", "Stone"), FullName("Ryan", "Gosling"))
  println(lis_4)

}
