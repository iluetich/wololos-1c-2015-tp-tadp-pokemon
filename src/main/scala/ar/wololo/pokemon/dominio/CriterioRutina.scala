package ar.wololo.pokemon.dominio

import scala.util.Try

/*
 * Modelo como singletons dado que los criterios son state-less, y lo 
 * usarán los pokemon para tener en cuenta si una rutina es mejor que otra.
 * La idea es utilizar polimorfismo paramétrico en el Pokemon, haciendo más flexible
 * el agregado de nuevos criterios al sistema, considerando que el Pokemon, 
 * como estructura, no variará en el tiempo y es inmutable.
 */

abstract class CriterioRutina {
  def obtenerMejorRutina(rutinasYPokemones: Seq[(Rutina, Try[Pokemon])]): Rutina
}
case object MaxNivel extends CriterioRutina {
  def obtenerMejorRutina(rutinasYPokemones: Seq[(Rutina, Try[Pokemon])]) = rutinasYPokemones.maxBy { _._2.get.nivel }._1
}
case object MaxEnergia extends CriterioRutina {
  def obtenerMejorRutina(rutinasYPokemones: Seq[(Rutina, Try[Pokemon])]) = rutinasYPokemones.maxBy { _._2.get.energia }._1
}
case object MinPeso extends CriterioRutina {
  def obtenerMejorRutina(rutinasYPokemones: Seq[(Rutina, Try[Pokemon])]) = rutinasYPokemones.minBy { _._2.get.peso }._1
}