package ar.wololo.pokemon.dominio

import scala.util.Try
import scala.util.Success

object SuperSistemaDeAnalisis {

  /*
   * XXX Podría implementarse con fold.
   * TODO Determinar con nacho la interfaz del pokemon, para saber si retorna un
   * resultado de ejecución, o bien, un Try[]
   */
  
  case class NoHuboRutinaHacibleException(pokemon: Pokemon) extends Exception

  def obtenerMejorRutinaSegun(pokemon: Pokemon,
    rutinas: Seq[Rutina],
    criterio: CriterioRutina): Try[Rutina] = {

    var estadosYRutinas = rutinas.map { rutina => (rutina, pokemon.hacerRutina(rutina)) }.filter { case (_, e) => e.isSuccess }

    if (estadosYRutinas.isEmpty)
      throw NoHuboRutinaHacibleException(pokemon)

    val mejorResultado = criterio match {
      case c: MayorAtributo => estadosYRutinas.maxBy { case (_, estadoPokemon) => c match {
        case MaxNivel => estadoPokemon.get.nivel
        case MaxEnergia => estadoPokemon.get.energia
      }}
      case c: MenorAtributo => estadosYRutinas.minBy { case (_, estadoPokemon) => c match {
        case MinPeso => estadoPokemon.get.peso
      }}
    }
    Success(mejorResultado._1)
  }
}