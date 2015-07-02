package ar.wololo.pokemon.dominio
import ar.wololo.pokemon.dominio._

case class Especie(val tipoPrincipal: Tipo,
    val tipoSecundario: Tipo = null,
    val incrementoFuerza: Int,
    val incrementoVelocidad: Int,
    val incrementoPeso: Int,
    val incrementoEnergiaMax: Int,
    val pesoMaximoSaludable: Int,
    val resistenciaEvolutiva: Int,
    val condicionEvolutiva: Option[CondicionEvolutiva] = None,
    val especieEvolucion: Option[Especie] = None) {

  def evolucionarA(pokemon: Pokemon): Pokemon = especieEvolucion.fold { pokemon } { especieAEvolucionar => pokemon.copy(especie = especieAEvolucionar) }

  def intercambiaronA(pokemon: Pokemon): Pokemon = condicionEvolutiva.fold { pokemon } { condicion => condicion.intercambiaronA(pokemon) }

  def evaluarEfectos(piedra: Piedra, pokemon: Pokemon): Pokemon = condicionEvolutiva.fold { pokemon } { condicion => condicion.evaluarEfectosPiedra(pokemon, piedra) }

  def getNivelPara(pokemon: Pokemon) = (1 until 100)
    .find { nivel => pokemon.experiencia >= experienciaParaNivel(nivel - 1) && pokemon.experiencia < experienciaParaNivel(nivel + 1) }
    .fold { 100 } { nivel => nivel }

  def experienciaParaNivel(nivel: Integer): Long = {
    nivel match {
      case n if n == 0 || n == 1 => 0
      case n => 2 * experienciaParaNivel(n - 1) + resistenciaEvolutiva
    }
  }

  def aumentaExperiencia(pokemon: Pokemon, exp: Long) = {
    val pokemonConExpAumentada = pokemon.copy(experiencia = pokemon.experiencia + exp)
    if (getNivelPara(pokemonConExpAumentada) > getNivelPara(pokemon))
      condicionEvolutiva.fold { pokemonConExpAumentada } { condicion => condicion.subioDeNivel(pokemonConExpAumentada) }
    else
      pokemonConExpAumentada
  }
}
