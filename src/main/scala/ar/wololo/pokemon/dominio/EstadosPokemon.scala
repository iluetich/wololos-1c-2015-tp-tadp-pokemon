package ar.wololo.pokemon.dominio

abstract class EstadoPokemon
object Bueno extends EstadoPokemon
object Paralizado extends EstadoPokemon
object Envenenado extends EstadoPokemon
object Ko extends EstadoPokemon
case class Dormido(val turnos: Int) extends EstadoPokemon
