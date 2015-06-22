package ar.wololo.pokemon.dominio

abstract class Piedra { def afectarA(pokemon: Pokemon): Pokemon }

object PiedraLunar extends Piedra { def afectarA(pokemon: Pokemon) = pokemon.evolucionar }
case class PiedraEvolutiva(val tipo: Tipo) extends Piedra {
  def afectarA(pokemon: Pokemon) = this.tipo match {
    case pokemon.tipoPrincipal => pokemon.evolucionar
    case tipoDistinto if tipoDistinto.leGanasA(pokemon.tipoPrincipal) || tipoDistinto.leGanasA(pokemon.tipoSecundario) => pokemon.copy(estado = Envenenado)
    case _ => pokemon
  }
}

