package ar.wololo.pokemon.dominio

import scala.util.Try
import scala.util.Success

object SuperSistemaDeAnalisis {

  /*
   * XXX Podría implementarse con fold.
   */

  case class NoHuboRutinaHacibleException(pokemon: Pokemon) extends Exception

  def obtenerMejorRutinaSegun(pokemon: Pokemon, rutinas: Seq[Rutina], criterio: CriterioRutina): Option[String] = {
    val rutinasYPokemones = rutinas.map { rutina => (rutina, pokemon.realizarRutina(rutina)) }.filter { case (_, e) => e.isSuccess }
    rutinasYPokemones.size match {
      case 0 => None
      case _ => Some(criterio.obtenerMejorRutina(rutinasYPokemones).fold { "No hubo mejor rutina" } { rutina => rutina.nombre })
    }
  }
}