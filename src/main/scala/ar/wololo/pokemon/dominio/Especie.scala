package ar.wololo.pokemon.dominio

case class Especie(val tipoPrincipal: Tipo,
  val tipoSecundario: Tipo,
  val incrementoFuerza: Int,
  val incrementoVelocidad: Int,
  val incrementoPeso: Int,
  val incrementoEnergiaMax: Int,
  val resistenciaEvolutiva: Int,
  val condicionEvolutiva: CondicionEvolutiva,
  val especieEvolucion: Especie = null) {

  def subirDeNivelA(pokemon: Pokemon): Pokemon = {
    val nivelNuevo = pokemon.nivel + 1
    val fuerzaNueva = pokemon.fuerza + incrementoFuerza
    val pesoNuevo = pokemon.peso + incrementoPeso
    val energiaMaxNueva = pokemon.energiaMax + incrementoEnergiaMax
    val pokemonMejorado = pokemon.copy(nivel = nivelNuevo, fuerza = fuerzaNueva, peso = pesoNuevo, energiaMax = energiaMaxNueva)

    condicionEvolutiva match {
      case c: SubirDeNivel if (c.nivelParaEvolucionar == nivelNuevo) => evolucionarA(pokemonMejorado)
      case _ => pokemonMejorado
    }
  }

  def evolucionarA(pokemon: Pokemon): Pokemon = {
    pokemon.copy(especie = especieEvolucion)
  }

  protected def experienciaParaNivel(nivel: Int): Integer = {
    nivel match {
      case 0 => resistenciaEvolutiva
      case n => 2 * experienciaParaNivel(n - 1) + resistenciaEvolutiva
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