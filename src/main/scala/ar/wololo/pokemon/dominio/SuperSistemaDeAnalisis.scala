package ar.wololo.pokemon.dominio

import scala.util.Try
import scala.util.Success

object SuperSistemaDeAnalisis {
/*
 * Criterios como objetos
 */
  def obtenerMejorRutinaSegun(pokemon: Pokemon)(rutinas: Seq[Rutina])(criterio: CriterioRutina): Option[String] = {
    val rutinasYPokemones = rutinas.map { rutina => (rutina, pokemon.realizarRutina(rutina)) }.filter { case (_, e) => e.isSuccess }
    rutinasYPokemones.size match {
      case 0 => None
      case _ => Some(criterio.obtenerMejorRutina(rutinasYPokemones).nombre)
    }
  }
/*
 * Criterios como funciones
 */
//  def obtenerMejorRutinaSegun(pokemon: Pokemon) (rutinas: Seq[Rutina], mejorRutina: Seq[(Rutina, Pokemon)] => Rutina): Option[String] = {
//    val rutinasYPokemones = aplicarA(pokemon, rutinas)
//    rutinasYPokemones.size match {
//      case 0 => None
//      case _ => Some(mejorRutina(rutinasYPokemones).nombre)
//    }
//  }
//
//  private def aplicarA(pokemon: Pokemon, rutinas: Seq[Rutina]): Seq[(Rutina, Pokemon)] = rutinas.map { rutina => (rutina, pokemon.realizarRutina(rutina)) }
//    .filter { case (_, e) => e.isSuccess }
//    .map { case (r, e) => (r, e.get) }
}