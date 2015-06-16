package ar.wololo.pokemon.dominio

import scala.util.Try
import scala.util.Success

object SuperSistemaDeAnalisis {

  /*
   * XXX Podría implementarse con fold.
   */

  case class NoHuboRutinaHacibleException(pokemon: Pokemon) extends Exception

  def obtenerMejorRutinaSegun(pokemon: Pokemon, rutinas: Seq[Rutina], criterio: CriterioRutina): String = {
    var rutinasYPokemones = rutinas.map { rutina => (rutina.nombre, pokemon.realizarRutina(rutina)) }.filter { case (_, e) => e.isSuccess }
    
    rutinasYPokemones.size match {
      case 0 => throw NoHuboRutinaHacibleException(pokemon)
      case _ => criterio.obtenerMejorRutina(rutinasYPokemones)
    }
  }
}