package ar.wololo.pokemon.dominio

import scala.util.Try
import scala.util.Success

class SuperSistemaDeAnalisis {
  
  case class NoHuboRutinaHacibleException(pokemon: Pokemon) extends Exception

  def obtenerMejorRutinaSegun(pokemon: Pokemon,
    rutinas: Seq[Rutina],
    criterio: CriterioRutina): Try[Rutina] = {

    var estadosYRutinas: Seq[(Try[Pokemon], Rutina)] = null

    try {
      estadosYRutinas = rutinas.map { rutina => (pokemon.hacerRutina(rutina), rutina) }.filter { case (estadoPokemon, _) => estadoPokemon.isSuccess }
    } catch {
      case e: Exception => throw new NoHuboRutinaHacibleException(pokemon)
    }

    criterio match {
      case MayorNivelPosible => Success(estadosYRutinas.maxBy { case (estadoPokemon, _) => estadoPokemon.get.nivel }._2)
      case MayorEnergiaPosible => Success(estadosYRutinas.maxBy { case (estadoPokemon, _) => estadoPokemon.get.energia }._2)
      case MenorPesoPosible => Success(estadosYRutinas.minBy { case (estadoPokemon, _) => estadoPokemon.get.peso }._2)
    }
  }

}