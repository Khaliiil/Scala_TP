package architecture2

import java.io.PrintWriter
import java.nio.file.{Files, Paths}

import org.json4s.native.JsonMethods.parse

import scala.collection.immutable.Set
import scala.io.Source


class Actor(val name:String, val surname:String){

  final val path_origin = "C:\\Users\\Khalil Mahfoudh\\IdeaProjects\\Scalix\\src\\data\\"
  val id:Int = getId.head

  def getId: Option[Int] ={
    try {
      val request = Source.fromURL("https://api.themoviedb.org/3/search/person?api_key=0a3f9b1422abb8a0284f388056b138de&language=en-US&query=" +
        name + "%20" + surname + "&page=1&include_adult=false")
      val JSON = request.mkString
      val id = Some(Integer.parseInt((parse(JSON) \ "results" \ "id").children.head.values.toString))

      if (!Files.exists(Paths.get(path_origin+"actor" + id.value + ".txt"))){
        println("Envoi de requete")
        val request2 = Source.fromURL("https://api.themoviedb.org/3/person/" + id.value +
          "/movie_credits?api_key=0a3f9b1422abb8a0284f388056b138de&language=en-US")
        val JSON2 = request2.mkString
        val out = new PrintWriter(path_origin+"actor" + id.value + ".txt")
        out.write(JSON2)
        out.close()
      }

      id

    } catch {
      case _: Exception => None
    }
  }

  def getMovies: Set[(Int, String)]={
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

  override def toString: String = name + " " + surname
}

object Actor {

  val bruce = new Actor("Bruce", "Willis")
}