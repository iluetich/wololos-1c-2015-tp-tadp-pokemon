package ar.wololo.pokemon.dominio

abstract class CondicionEvolutiva { def evaluaCondicion(unPokemon: Pokemon): Boolean }
/*
 * TODO Implementar lógica de cada condición
 * FIXME! Cómo vamos a usar estas condiciones?
 */

case class SubirDeNivel(val nivelParaEvolucionar: Integer) extends CondicionEvolutiva {
  def evaluaCondicion(unPokemon: Pokemon): Boolean = unPokemon.nivel >= nivelParaEvolucionar
}
object Intercambiar extends CondicionEvolutiva { def evaluaCondicion(unPokemon: Pokemon): Boolean = true }
object UsarUnaPiedra extends CondicionEvolutiva { def evaluaCondicion(unPokemon: Pokemon) = true }
object UsarUnaPiedraLunar extends CondicionEvolutiva { def evaluaCondicion(unPokemon: Pokemon) = true }