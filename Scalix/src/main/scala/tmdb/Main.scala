package tmdb


import scala.io.Source



object Main extends App {
  import Finder._

  var cache_actor = collection.mutable.Map[FullName, Int]()
  var cache_actor_indirect = collection.mutable.Map[Int, FullName]();
  var cache_movies = collection.mutable.Map[Int, Set[(Int, String)]]()

  val content = Source.fromURL("https://api.themoviedb.org/3/person/3894?api_key=0a3f9b1422abb8a0284f388056b138de&language=en-US")
  println(content.mkString)

  val lis_1 = findActorId("Leonardo", "DiCaprio", cache_actor, cache_actor_indirect)
  cache_actor += (("Leonardo", "DiCaprio") -> lis_1.head)
  println(cache_actor)

  val lis_2 = findActorMovies(6193)
  cache_movies += (6193 -> lis_2)
  println(cache_movies)
  
  val lis_3 = findMovieDirector(550)
  println(lis_3)
  val lis_4 = request(FullName("Emma", "Stone"), FullName("Ryan", "Gosling"), cache_actor, cache_actor_indirect)
  println(lis_4)


}
