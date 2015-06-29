package ar.wololo.pokemon.dominio

import scala.util.Try
import scala.util.Success

import Tipos._

object criteriosDeAnalisis {

  val maxNivel: Criterio = _.maxBy { _._2.nivel }._1
  val maxEnergia: Criterio = _.maxBy { _._2.nivel }._1
  val minPeso: Criterio = _.minBy { _._2.peso }._1

}

object SuperSistemaDeAnalisis {

  def obtenerMejorRutinaSegun(pokemon: Pokemon)(rutinas: Seq[Rutina])(mejorRutina: Criterio): Option[String] = {
    val rutinasYPokemones = aplicarA(pokemon, rutinas)
    rutinasYPokemones.size match {
      case 0 => None
      case _ => Some(mejorRutina(rutinasYPokemones).nombre)
    }
  }

  private def aplicarA(pokemon: Pokemon, rutinas: Seq[Rutina]): Seq[(Rutina, Pokemon)] = for {
    rutina <- rutinas
    tryPoke = pokemon.realizarRutina(rutina) if tryPoke.isSuccess
  } yield (rutina, tryPoke.get)
}