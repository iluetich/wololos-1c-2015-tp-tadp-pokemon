package ar.wololo.pokemon.dominio

abstract class Genero{
  def fingiIntercambio(unPokemon: Pokemon):Pokemon
  def aumentaExperiencia(unPokemon: Pokemon):Pokemon
}
object Macho extends Genero{
  def fingiIntercambio(unPokemon : Pokemon):Pokemon = unPokemon.modificaPeso(1)
  def aumentaExperiencia(unPokemon : Pokemon):Pokemon = unPokemon.aumentaExperiencia(20)
}
object Hembra extends Genero{
  def fingiIntercambio(unPokemon :Pokemon):Pokemon = unPokemon.modificaPeso(-10)
  def aumentaExperiencia(unPokemon : Pokemon):Pokemon = unPokemon.aumentaExperiencia(40)
}