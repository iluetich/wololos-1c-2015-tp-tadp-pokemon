package ar.wololo.pokemon.dominio
import ar.wololo.pokemon.dominio._

case class Especie(val tipoPrincipal: Tipo,
  val tipoSecundario: Tipo,
  val incrementoFuerza: Int,
  val incrementoVelocidad: Int,
  val incrementoPeso: Int,
  val incrementoEnergiaMax: Int,
  val pesoMaximoSaludable: Int,
  val resistenciaEvolutiva: Int,
  val condicionEvolutiva: CondicionEvolutiva = null,
  val especieEvolucion: Especie = null) {

  def evolucionarA(pokemon: Pokemon): Pokemon = pokemon.copy(especie = especieEvolucion)

  def experienciaParaNivel(nivel: Int): Integer = {
    nivel match {
      case 1 => resistenciaEvolutiva
      case n => 2 * experienciaParaNivel(n - 1) + resistenciaEvolutiva
    }
  }

  def subirDeNivelA(pokemon: Pokemon): Pokemon = {
    val nivelNuevo = pokemon.nivel + 1
    val fuerzaNueva = Math.min(pokemon.fuerza + incrementoFuerza, 100)
    val energiaMaxNueva = pokemon.energiaMax + incrementoEnergiaMax
    val pesoNuevo = Math.min(pokemon.peso + incrementoPeso, pesoMaximoSaludable)
    val pokemonMejorado = pokemon.copy(nivel = nivelNuevo, fuerza = fuerzaNueva, energiaMax = energiaMaxNueva)

    condicionEvolutiva match {
      case c: SubirDeNivel if (c.nivelParaEvolucionar == nivelNuevo) => evolucionarA(pokemonMejorado)
      case _ => pokemonMejorado
    }
  }

  def aumentaExperienciaDe(pokemon: Pokemon, cantidad: Integer): Pokemon = {
    cantidad match {
      case n if n == 0 => pokemon
      case n if n > 0 =>
        val expAcum = pokemon.experiencia + cantidad
        val expSgteNivel = experienciaParaNivel(pokemon.nivel + 1)

        if (expAcum >= expSgteNivel) {
          val pokemonConExpAumentada = pokemon.copy(experiencia = expSgteNivel)
          subirDeNivelA(pokemonConExpAumentada).aumentaExperiencia(expAcum - expSgteNivel)
        } else {
          pokemon.copy(experiencia = pokemon.experiencia + expAcum)
        }
    }
  }
}