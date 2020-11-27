package architecture2

class Cache {
  var cache_actor = collection.mutable.Map[(String, String), Int]()     //(Name, id)
  var cache_movies = collection.mutable.Map[Int, Set[(Int, String)]]()  //(id_actor, set_films)


  def addCacheActor(name: (String,String), id:Int){
    cache_actor += ((name,id))
  }

  def addCachemovie(id:Int, movies:Set[(Int, String)]){
    cache_movies += ((id, movies))
  }

}
