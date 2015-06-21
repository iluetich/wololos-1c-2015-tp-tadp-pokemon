package ar.wololo.pokemon.dominio

abstract class CondicionEvolutiva {
  def evaluarEfectosPiedra(unPokemon: Pokemon, piedra: Piedra): Pokemon = unPokemon
  def subioDeNivel(unPokemon: Pokemon): Pokemon = unPokemon
  def intercambiaronA(unPokemon: Pokemon): Pokemon = unPokemon.teIntercambiaron()
}
/*
 * TODO Implementar lógica de cada condición
 * FIXME! Cómo vamos a usar estas condiciones?
 */
object NoEvoluciona extends CondicionEvolutiva
object Intercambiar extends CondicionEvolutiva {
  override def intercambiaronA(unPokemon: Pokemon) = unPokemon.evolucionar
}
object UsarUnaPiedra extends CondicionEvolutiva {
  override def evaluarEfectosPiedra(unPokemon: Pokemon, piedra: Piedra) = piedra.afectarA(unPokemon)
}
case class SubirDeNivel(val nivelParaEvolucionar: Integer) extends CondicionEvolutiva {
  override def subioDeNivel(unPokemon: Pokemon) = unPokemon.nivel match {
    case n if n >= nivelParaEvolucionar => unPokemon.evolucionar
    case _ => unPokemon
  }
}

