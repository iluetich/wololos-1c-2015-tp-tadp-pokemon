package ar.wololo.pokemon.dominio

abstract class CondicionEvolutiva { 
  def evaluarEfectosPiedra(unPokemon: Pokemon, piedra :Piedra): Pokemon 
  def evaluaCondicion(unPokemon: Pokemon): Boolean
}
/*
 * TODO Implementar lógica de cada condición
 * FIXME! Cómo vamos a usar estas condiciones?
 */

case class SubirDeNivel(val nivelParaEvolucionar: Integer) extends CondicionEvolutiva {
  def evaluaCondicion(unPokemon: Pokemon): Boolean = unPokemon.nivel >= nivelParaEvolucionar
  
  def evaluarEfectosPiedra(unPokemon: Pokemon, piedra :Piedra): Pokemon = unPokemon
}

object Intercambiar extends CondicionEvolutiva { 
  def evaluaCondicion(unPokemon: Pokemon): Boolean = true
  
  def evaluarEfectosPiedra(unPokemon: Pokemon, piedra :Piedra): Pokemon = unPokemon 
}

object UsarUnaPiedra extends CondicionEvolutiva {
  def evaluaCondicion(unPokemon: Pokemon): Boolean = true
  
  def evaluarEfectosPiedra(unPokemon: Pokemon, piedra :Piedra):Pokemon = piedra match{
    case piedraEvolutiva : PiedraEvolutiva => piedraEvolutiva.aplicaEfectos(unPokemon)
    case _ => unPokemon
  } 
}

object UsarUnaPiedraLunar extends CondicionEvolutiva { 
  def evaluaCondicion(unPokemon: Pokemon): Boolean = true
  
  def evaluarEfectosPiedra(unPokemon: Pokemon, piedra :Piedra) = piedra match{
    case PiedraLunar => PiedraLunar.aplicaEfectos(unPokemon)
    case _ => unPokemon
  } 
}

object NoEvoluciona extends CondicionEvolutiva {
  def evaluaCondicion(unPokemon: Pokemon): Boolean = true
  
  def evaluarEfectosPiedra(unPokemon: Pokemon, piedra :Piedra) = unPokemon 
}