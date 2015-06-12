package ar.wololo.pokemon.dominio

abstract class Piedra
case class PiedraEvolutiva(val tipo: Tipo) extends Piedra
object PiedraLunar extends Piedra