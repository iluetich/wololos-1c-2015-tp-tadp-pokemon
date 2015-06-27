package ar.wololo.pokemon.dominio

abstract class CondicionEvolutiva {
  def evaluarEfectosPiedra(unPokemon: Pokemon, piedra: Piedra): Pokemon = unPokemon
  def subioDeNivel(unPokemon: Pokemon): Pokemon = unPokemon
  def intercambiaronA(unPokemon: Pokemon): Pokemon = unPokemon.teIntercambiaron()
}

object NoEvoluciona extends CondicionEvolutiva
object Intercambiar extends CondicionEvolutiva {
  override def intercambiaronA(unPokemon: Pokemon) = unPokemon.evolucionar
}

//agrego cond evolutiva que falta por que sino todos los pokemones al llegarle la piedra lunar evolucionan
object UsarPiedraLunar extends CondicionEvolutiva {
  override def evaluarEfectosPiedra(unPokemon: Pokemon, piedra: Piedra) = piedra match{
    case PiedraLunar => piedra.afectarA(unPokemon)
    case _ => unPokemon
  }
}

//solo si le llega una piedra que no es la piedra lunar ahi afecta a un pokemon
object UsarUnaPiedra extends CondicionEvolutiva {
  override def evaluarEfectosPiedra(unPokemon: Pokemon, piedra: Piedra) = piedra match{
    case PiedraLunar => unPokemon
    case _ => piedra.afectarA(unPokemon)
  }
}
case class SubirDeNivel(val nivelParaEvolucionar: Integer) extends CondicionEvolutiva {
  override def subioDeNivel(unPokemon: Pokemon) = unPokemon.nivel match {
    case n if n >= nivelParaEvolucionar => unPokemon.evolucionar
    case _ => unPokemon
  }
}

