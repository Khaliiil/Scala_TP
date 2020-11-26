package tmdb


import scala.io.Source



object Main extends App {
  import Finder._

  var cache_actor = collection.mutable.Map[(String, String), Int]()
  var cache_actor_indirect = collection.mutable.Map[Int, (String, String)]();
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

  object pairOrdering extends Ordering[(Int, Int, Int)] {
    def compare(a:(Int, Int, Int), b:(Int, Int, Int)) = a._3 compare b._3
  }

  val pairOfActors = for ((id_actor1, films1) <- cache_movies;
       (id_actor2, films2) <- cache_movies;
       intersection = films1.intersect(films2).size
       ) yield (id_actor1, id_actor2, intersection)
  val maxFilmsTogether = pairOfActors.max(pairOrdering)._3
  val ans = for ((id_actor1, id_actor2, len) <- pairOfActors if len == maxFilmsTogether) yield (cache_actor_indirect(id_actor1), cache_actor_indirect(id_actor2))
}
