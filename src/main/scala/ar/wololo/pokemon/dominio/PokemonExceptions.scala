package ar.wololo.pokemon.dominio

case class EstaKo(val pokemon: Pokemon) extends Exception
case class FantasmaNoPuedeLevantarPesas(val pokemon: Pokemon) extends Exception
case class PokemonNoConoceMovONoTienePA(val pokemon: Pokemon) extends Exception
case class NoPuedeFuerzaInvalida(val pokemon: Pokemon) extends Exception
case class NoPuedeVelocidadInvalida(val pokemon: Pokemon) extends Exception
case class NoPuedePesoInvalido(val pokemon: Pokemon) extends Exception
case class NoPuedeEnergiaMenorACero(val pokemon: Pokemon) extends Exception