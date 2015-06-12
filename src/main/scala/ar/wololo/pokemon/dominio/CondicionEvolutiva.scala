package ar.wololo.pokemon.dominio

abstract class CondicionEvolutiva { def evaluaCondicion(unPokemon: Pokemon): Boolean }
/*
 * TODO Implementar lógica de cada condición
 */

object SubirDeNivel extends CondicionEvolutiva { def evaluaCondicion(unPokemon: Pokemon) = true }
object Intercambiar extends CondicionEvolutiva { def evaluaCondicion(unPokemon: Pokemon) = true }
object UsarUnaPiedra extends CondicionEvolutiva { def evaluaCondicion(unPokemon: Pokemon) = true }
object UsarUnaPiedraLunar extends CondicionEvolutiva { def evaluaCondicion(unPokemon: Pokemon) = true }