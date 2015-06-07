package ar.wololo.pokemon.dominio

/*
 * Modelo como singletons dado que los criterios son state-less, y lo 
 * usarán los pokemon para tener en cuenta si una rutina es mejor que otra.
 * La idea es utilizar polimorfismo paramétrico en el Pokemon, haciendo más flexible
 * el agregado de nuevos criterios al sistema, considerando que el Pokemon, 
 * como estructura, no variará en el tiempo y es inmutable.
 */

class Rutina(val actividades: Seq[Actividad]) {
  def aplicateA(pokemon: Pokemon): Pokemon = null
}

abstract class CriterioRutina { }
case object MayorNivelPosible extends CriterioRutina
case object MayorEnergiaPosible extends CriterioRutina
case object MenorPesoPosible extends CriterioRutina