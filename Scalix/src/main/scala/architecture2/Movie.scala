package architecture2

import java.io.PrintWriter
import java.nio.file.{Files, Paths}

import org.json4s.JString
import org.json4s.native.JsonMethods.parse

import scala.io.Source

class Movie (val id: Int) {
  final val path_origin = "C:\\Users\\Khalil Mahfoudh\\IdeaProjects\\Scalix\\src\\data\\"
  def getId: Int = this.id

  def getMovieDirector: Option[(Int, String)] = {


    try {
      var JSON = ""
      if (Files.exists(Paths.get(path_origin + "movie" + id.toString + ".txt"))) {
        println("Utilisation du cache")
        val file = Source.fromFile(path_origin + "movie" + id.toString + ".txt")
        JSON = file.mkString
      } else {
        println("Envoi de requete")
        val request = Source.fromURL("https://api.themoviedb.org/3/movie/" + id +
          "/credits?api_key=0a3f9b1422abb8a0284f388056b138de")
        JSON = request.mkString
        val out = new PrintWriter(path_origin + "movie" + id.toString + ".txt")
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
}