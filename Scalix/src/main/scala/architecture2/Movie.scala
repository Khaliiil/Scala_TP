package architecture2


import org.json4s.JString
import org.json4s.native.JsonMethods.parse

import scala.io.Source

class Movie (val id: Int) {


  val movieDirectors: Option[(Int, String)] = getMovieDirectors

  def getMovieDirectors: Option[(Int, String)] = {

    try {
        println("Envoi de requete")
        val request = Source.fromURL("https://api.themoviedb.org/3/movie/" + id +
          "/credits?api_key=0a3f9b1422abb8a0284f388056b138de")
        val JSON = request.mkString
      val parsed = parse(JSON)
      val crew = (parsed \ "crew" \ "job").children
      val index = crew.indexOf(JString("Director"))
      Option((Integer.parseInt((parsed \ "crew" \ "id").children(index).values.toString),
        (parsed \ "crew" \ "name").children(index).values.toString))
      }
    catch {
      case _: Exception => None
    }
  }

  override def toString: String = id.toString
}

