package tmdb

import scala.collection.immutable._

import scala.io.Source
import org.json4s._
import org.json4s.native.JsonMethods._

import java.io.PrintWriter
import java.nio.file.{Paths, Files}

object Finder extends App{
  final val path_origin = "C:\\Users\\Khalil Mahfoudh\\IdeaProjects\\Scalix\\src\\data\\"
  def findActorId(name: String, surname: String, cache_actor: collection.mutable.Map[(String, String), Int], cache_actor_indirect: collection.mutable.Map[Int, (String, String)]): Option[Int] = {
    var id: Int = -1
    cache_actor.get((name, surname)) match {
      case None => {
        try {
          val request = Source.fromURL("https://api.themoviedb.org/3/search/person?api_key=0a3f9b1422abb8a0284f388056b138de&language=en-US&query=" +
            name + "%20" + surname + "&page=1&include_adult=false")
          val JSON = request.mkString
          id = Integer.parseInt((parse(JSON) \ "results" \ "id").children.head.values.toString)
          cache_actor += ((name, surname) -> id)
          cache_actor_indirect += (id -> (name, surname))
        }
      }
      case Some(cache_id) => {
        id = cache_id
      }
    }
    try {
      if (!Files.exists(Paths.get(path_origin+"actor" + id + ".txt"))){
        println("Envoi de requete")
        val request2 = Source.fromURL("https://api.themoviedb.org/3/person/" + id +
          "/movie_credits?api_key=0a3f9b1422abb8a0284f388056b138de&language=en-US")
        val JSON2 = request2.mkString
        val out = new PrintWriter(path_origin+"actor" + id + ".txt")
        out.write(JSON2)
        out.close()
      }
    Some(id)
    } catch {
      case _: Exception => None
    }
  }

  def findActorMovies(id: Int): Set[(Int, String)] = {

    var JSON = ""
    if (Files.exists(Paths.get(path_origin+"actor" + id.toString + ".txt"))){
      println("Utilisation du cache")
      val file = Source.fromFile(path_origin+"actor" + id.toString + ".txt")
      JSON = file.mkString
    }else{
      println("Envoi de requete")
      val request = Source.fromURL("https://api.themoviedb.org/3/person/" + id +
        "/movie_credits?api_key=0a3f9b1422abb8a0284f388056b138de&language=en-US")
      JSON = request.mkString
      val out = new PrintWriter(path_origin+"actor" + id.toString + ".txt")
      out.write(JSON)
      out.close()
    }

    val titres = (parse(JSON) \ "cast" \ "title").children
    val ids = (parse(JSON) \ "cast" \ "id").children
    var ens = Set((Integer.parseInt(ids.head.values.toString), titres.head.values.toString))
    for (i <- 1 until titres.length) {
      ens += ((Integer.parseInt(ids(i).values.toString), titres(i).values.toString))
    }

    ens
  }

  def findMovieDirector(id: Int): Option[(Int, String)] = {


    try {
      var JSON = ""
      if (Files.exists(Paths.get(path_origin+"movie" + id.toString + ".txt"))){
        println("Utilisation du cache")
        val file = Source.fromFile(path_origin+"movie" + id.toString + ".txt")
        JSON = file.mkString
      }else{
        println("Envoi de requete")
        val request = Source.fromURL("https://api.themoviedb.org/3/movie/" + id +
          "/credits?api_key=0a3f9b1422abb8a0284f388056b138de")
        JSON = request.mkString
        val out = new PrintWriter(path_origin+"movie" + id.toString + ".txt")
        out.write(JSON)
        out.close()
      }

      val parsed = parse(JSON)
      val crew = (parsed \ "crew" \ "job").children
      val index = crew.indexOf(JString("Director"))
      Option((Integer.parseInt((parsed \ "crew" \ "id").children(index).values.toString),
        (parsed \ "crew" \ "name").children(index).values.toString))
    } catch {
      case _: Exception => None
    }
  }

  def request(actor1: FullName, actor2: FullName, cache_actor: collection.mutable.Map[(String, String), Int], cache_actor_indirect: collection.mutable.Map[Int, (String, String)]): Set[(String, String)] = {

    val idActor1 = findActorId(actor1.name, actor1.surname, cache_actor, cache_actor_indirect)
    val idActor2 = findActorId(actor2.name, actor2.surname, cache_actor, cache_actor_indirect)

    val moviesActor1 = findActorMovies(idActor1.getOrElse(0))
    val moviesActor2 = findActorMovies(idActor2.getOrElse(0))

    var ens: Set[(String, String)] = Set()

    for (i <- moviesActor1) {
      if (moviesActor2.contains(i)) {
        ens += ((findMovieDirector(i._1).head._2, i._2))
      }
    }
    ens

  }
}
