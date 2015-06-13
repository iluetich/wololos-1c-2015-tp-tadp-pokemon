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

  def experienciaParaNivel(nivel: Int): Integer = {
			nivel match {
			case 0 => resistenciaEvolutiva
			case n => 2 * experienciaParaNivel(n - 1) + resistenciaEvolutiva
			}
	}
	
	def evolucionarA(pokemon: Pokemon): Pokemon = {
			pokemon.copy(especie = especieEvolucion)
	}
	
  def subirDeNivelA(pokemon: Pokemon): Pokemon = {
    val nivelNuevo = pokemon.nivel + 1
    val fuerzaNueva = pokemon.fuerza + incrementoFuerza
    val energiaMaxNueva = pokemon.energiaMax + incrementoEnergiaMax
    val pokemonMejorado = pokemon.copy(nivel = nivelNuevo, fuerza = fuerzaNueva, energiaMax = energiaMaxNueva).aumentaPeso(incrementoPeso)

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

  /*
   * [..] También cada especie determina un Peso Máximo, que indica qué tanto Peso
   * pueden ganar sus miembros antes de que afecte su salud [...] -> Página 3 del TP
   */
  
  def aumentaPesoDe(pokemon: Pokemon, cantidad: Integer): Pokemon = {
    val pesoFuturo = pokemon.peso + cantidad
    if (pesoFuturo > pesoMaximoSaludable)
      //Afecta su salud, pero el requerimiento no especifica cómo.
      //Duplico código porque no sé si hay que hacer algo distinto o no.
      pokemon.copy(peso = pesoFuturo)
    else
      pokemon.copy(peso = pesoFuturo)
  }
}