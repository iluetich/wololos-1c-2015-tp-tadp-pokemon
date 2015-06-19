package ar.wololo.pokemon.dominio

abstract class Piedra

case class PiedraEvolutiva(val tipo: Tipo) extends Piedra {
  def aplicaEfectos(pokemon : Pokemon) = this.tipo match{
    case pokemon.tipoPrincipal => pokemon.evolucionar
    case tipoDistinto =>  
         if (tipoDistinto.aQuienesLeGanas.exists { t => t == pokemon.tipoPrincipal || t == pokemon.tipoSecundario })
               pokemon.copy(estado = Envenenado)
         else
               pokemon
  }
}

object PiedraLunar extends Piedra {  
  def aplicaEfectos(pokemon : Pokemon) = pokemon.evolucionar
}