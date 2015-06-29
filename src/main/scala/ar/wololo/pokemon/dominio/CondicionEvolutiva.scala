package ar.wololo.pokemon.dominio

abstract class CondicionEvolutiva {
  def evaluarEfectosPiedra(unPokemon: Pokemon, piedra: Piedra): Pokemon = unPokemon
  def subioDeNivel(unPokemon: Pokemon): Pokemon = unPokemon
  def intercambiaronA(unPokemon: Pokemon): Pokemon = unPokemon.genero.fingiIntercambio(unPokemon)
}
/*
 *  Según enunciado: "Usar Piedra: Los miembros de Especies con esta condición 
 * sólo evolucionan cuando son expuestos a la radiación de unos objetos 
 * conocidos como "Piedras Evolutivas".
 *  Cada Piedra tiene asociado un Tipo que debe coincidir con el Tipo Principal
 * de la Especie para gatillar la evolución. La única excepción a esto son las Piedras Lunares, 
 * que hacen evolucionar Pokémon de especies arbitrarias."
 * 
 */

object NoEvoluciona extends CondicionEvolutiva
object UsarUnaPiedra extends CondicionEvolutiva { override def evaluarEfectosPiedra(unPokemon: Pokemon, piedra: Piedra) = piedra.afectarA(unPokemon) }
object Intercambiar extends CondicionEvolutiva { override def intercambiaronA(unPokemon: Pokemon) = unPokemon.evolucionar }
case class SubirDeNivel(val nivelParaEvolucionar: Integer) extends CondicionEvolutiva {
  override def subioDeNivel(unPokemon: Pokemon) = unPokemon.nivel match {
    case n if n >= nivelParaEvolucionar => unPokemon.evolucionar
    case _ => unPokemon
  }
}

