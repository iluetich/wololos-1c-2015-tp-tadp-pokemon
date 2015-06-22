package ar.wololo.pokemon.dominio

abstract class Genero {
  def fingiIntercambio(unPokemon: Pokemon): Pokemon
  def aumentaExperiencia(unPokemon: Pokemon): Pokemon
  protected def fingiIntercambio(unPokemon: Pokemon, peso: Int) = unPokemon.modificaPeso(peso)
  protected def aumentaExperiencia(unPokemon: Pokemon, exp: Int) = unPokemon.aumentaExperiencia(exp)
}
object Macho extends Genero {
  def fingiIntercambio(poke: Pokemon) = fingiIntercambio(poke, 1)
  def aumentaExperiencia(poke: Pokemon) = aumentaExperiencia(poke, 20)
}
object Hembra extends Genero {
  def fingiIntercambio(poke: Pokemon) = fingiIntercambio(poke, -10)
  def aumentaExperiencia(poke: Pokemon) = aumentaExperiencia(poke, 40)
}