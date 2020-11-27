#Projet Scalaflix
#THIEBAUT KEVIN
#MAHFOUDH Khalil


#Package tmdb

Première version du projet. Elle permet d'aller chercher les informations de TMDB via des requêtes puis de stocker les résultats des requêtes dans des fichiers "Actor<id>.txt" et "Movie<id>.txt" (cache). Des dictionnaires ont aussi été réaliser pour cacher les résultats des méthodes. 

#Package architecture2

C'est la version "objet" du projet. Elle permet de créer :

- Des objets "Actor" en spécifiant le prénom, le nom et une instance d'objet cache. En effet, le cache stockera les informations de cet acteur dans un dictionnaire relié

- Des objets "Movie" en spécifiant l'id du film.

- Des objets "Cache" afin de cacher les id d'acteurs et les films auxquels il a participé.