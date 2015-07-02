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

  def experienciaParaNivel(nivel: Integer): Long = {
    nivel match {
      case n if n == 0 || n == 1 => 0
      case n => 2 * experienciaParaNivel(n - 1) + resistenciaEvolutiva
    }
  }

  def subirDeNivelA(pokemon: Pokemon): Pokemon = {
    val pokemonMejorado = pokemon.copy(
      nivel = pokemon.nivel + 1,
      fuerza = Math.min(pokemon.fuerza + incrementoFuerza, pokemon.fuerzaMax),
      energiaMax = pokemon.energiaMax + incrementoEnergiaMax,
      peso = Math.min(pokemon.peso + incrementoPeso, pesoMaximoSaludable))

    condicionEvolutiva.fold { pokemonMejorado } { condicion => condicion.subioDeNivel(pokemonMejorado) }
  }

}
