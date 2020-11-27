package tmdb

import tmdb.Main.{cache_actor, cache_actor_indirect, cache_movies}

import scala.collection.IterableOnce.iterableOnceExtensionMethods

object CacheQuery {
  def queryPairOfActors(cache_movies: collection.mutable.Map[Int, Set[(Int, String)]] , cache_actor_indirect: collection.mutable.Map[Int, FullName]): Set[(FullName, FullName)] = {
    object pairOrdering extends Ordering[(Int, Int, Int)] {
      def compare(a:(Int, Int, Int), b:(Int, Int, Int)) = a._3 compare b._3
    }

    val pairOfActors = for ((id_actor1, films1) <- cache_movies;
                            (id_actor2, films2) <- cache_movies;
                            intersection = films1.intersect(films2).size
                            if id_actor1 < id_actor2
                            ) yield (id_actor1, id_actor2, intersection)
    val maxFilmsTogether = pairOfActors.max(pairOrdering)._3
    val ans = for ((id_actor1, id_actor2, len) <- pairOfActors if len == maxFilmsTogether) yield (cache_actor_indirect(id_actor1), cache_actor_indirect(id_actor2))
    ans.toSet
  }

  def queryPairOfActors2(cache_movies: collection.mutable.Map[Int, Set[(Int, String)]] , cache_actor_indirect: collection.mutable.Map[Int, (String, String)]): Set[((String, String), (String, String))] = {
    object pairOrdering extends Ordering[(Int, Int, Int)] {
      def compare(a:(Int, Int, Int), b:(Int, Int, Int)) = a._3 compare b._3
    }
    val pairOfActors = cache_movies.flatMap(
      pair1 => cache_movies.map(
      pair2 => (pair1._1, pair2._1, pair1._2.intersect(pair2._2).size)))
      .filter(triple => triple._1 < triple._2)
    val maxFilmsTogether = pairOfActors.max(pairOrdering)._3
    val ans = pairOfActors.withFilter(
      triple => triple._3 == maxFilmsTogether).map(
      triple => (cache_actor_indirect(triple._1), cache_actor_indirect(triple._2)))
    ans.toSet
  }
}
