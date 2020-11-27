package architecture2


import org.json4s.native.JsonMethods.parse

import scala.collection.immutable.Set
import scala.io.Source


class Actor(val name:String, val surname:String, var cache:Cache){

  val id:Int = getId.head
  val movies: Set[(Int, String)]= getMovies

  def getId: Option[Int] ={
    try {
      if (cache.cache_actor.contains((name, surname))){
        return Option(cache.cache_actor((name,surname)))
      }
      println("requête")
      val request = Source.fromURL("https://api.themoviedb.org/3/search/person?api_key=0a3f9b1422abb8a0284f388056b138de&language=en-US&query=" +
        name + "%20" + surname + "&page=1&include_adult=false")
      val JSON = request.mkString
      val id = Some(Integer.parseInt((parse(JSON) \ "results" \ "id").children.head.values.toString))
      cache.cache_actor += ((name, surname) -> id.value)
      id

    } catch {
      case _: Exception => None
    }
  }

  def getMovies: Set[(Int, String)]={

    try {
      if (cache.cache_movies.contains(id)){
        return cache.cache_movies(id)
      }
      val request = Source.fromURL("https://api.themoviedb.org/3/person/" + id +
        "/movie_credits?api_key=0a3f9b1422abb8a0284f388056b138de&language=en-US")
      val JSON = request.mkString

      val titres = (parse(JSON) \ "cast" \ "title").children
      val ids = (parse(JSON) \ "cast" \ "id").children
      var ens = Set((Integer.parseInt(ids.head.values.toString), titres.head.values.toString))
      for (i <- 1 until titres.length) {
        ens += ((Integer.parseInt(ids(i).values.toString), titres(i).values.toString))
      }
      cache.cache_movies += (id -> ens)
      ens
    }catch{
      case _: Exception => throw UninitializedFieldError("Error : The list does not exist.")
    }
  }

  def request(actor2: Actor): Set[(String, String)] = {

    var ens: Set[(String, String)] = Set()

    for (i <- this.movies) {
      if (actor2.movies.contains(i)) {
        ens += ((new Movie(i._1).movieDirectors.head._2, i._2))
      }
    }
    ens
  }

  override def toString: String = name + " " + surname
}

