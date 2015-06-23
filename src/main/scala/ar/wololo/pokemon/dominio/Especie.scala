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

  def experienciaParaNivel(nivel: Integer): Long = {
    nivel match {
      case n if n == 0 || n == 1 => 0
      case n => 2 * experienciaParaNivel(n - 1) + resistenciaEvolutiva
    }
  }

  def subirDeNivelA(pokemon: Pokemon): Pokemon = {
    val pokemonMejorado =
      pokemon.copy(nivel = pokemon.nivel + 1,
                   fuerza = Math.min(pokemon.fuerza + incrementoFuerza, pokemon.fuerzaMax),
                   energiaMax = pokemon.energiaMax + incrementoEnergiaMax,
                   peso = Math.min(pokemon.peso + incrementoPeso, pesoMaximoSaludable))

    condicionEvolutiva match {
      case c: SubirDeNivel if (c.nivelParaEvolucionar == pokemonMejorado.nivel) 
               => pokemonMejorado.evolucionar.checkLevel
      case _ => pokemonMejorado.checkLevel
    }
  }

//  def aumentaExperienciaDe(pokemon: Pokemon, cantidad: Long): Pokemon = {
//    
//    
//    
//    cantidad match {
//      case n if n == 0 => pokemon
//      case n if n > 0 =>
//        val expAcum = pokemon.experiencia + cantidad
//        val expSgteNivel = experienciaParaNivel(pokemon.nivel + 1)
//
//        if (expAcum >= expSgteNivel) {
//          val pokemonConExpAumentada = pokemon.copy(experiencia = expSgteNivel)
//          subirDeNivelA(pokemonConExpAumentada).aumentaExperiencia(expAcum - expSgteNivel)
//        } else {
//          pokemon.copy(experiencia = pokemon.experiencia + expAcum)
//        }
//    }
//  }
}