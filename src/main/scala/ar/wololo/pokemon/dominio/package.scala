package ar.wololo.pokemon

import ar.wololo.pokemon.dominio.CriterioRutina
import ar.wololo.pokemon.dominio.Pokemon
import ar.wololo.pokemon.dominio.Actividad
import ar.wololo.pokemon.dominio.MayorNivelPosible
import ar.wololo.pokemon.dominio.MayorEnergiaPosible
import ar.wololo.pokemon.dominio.MenorPesoPosible
import scala.util.Try
import ar.wololo.pokemon.dominio.Rutina
import scala.util.Success

package object pokemon {

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