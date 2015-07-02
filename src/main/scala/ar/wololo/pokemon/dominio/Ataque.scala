package ar.wololo.pokemon.dominio

case class Ataque(val nombre: String,
    val efecto: Option[Pokemon => Pokemon] = None,
    val tipo: Tipo) {

  def tePuedeAprender(pokemon: Pokemon): Boolean = {
    !pokemon.sabesAtaque(this) && List(Normal, pokemon.tipoPrincipal, pokemon.tipoSecundario).contains(tipo)
  }

  def teUtiliza(pokemon: Pokemon): Pokemon = {
    val pokemonHabiendoUsadoAtaque = pokemon.reducirPa(this)
    var experiencia = 0

    val pokemonConMasExperiencia = tipo match {
      case Dragon => pokemonHabiendoUsadoAtaque.aumentaExperiencia(80)
      case pokemonHabiendoUsadoAtaque.tipoSecundario => pokemonHabiendoUsadoAtaque.aumentaExpEnBaseAGenero()
      case pokemonHabiendoUsadoAtaque.tipoPrincipal => pokemonHabiendoUsadoAtaque.aumentaExperiencia(50)
    }
    efecto.fold { pokemonConMasExperiencia } { _(pokemonConMasExperiencia) }
  }
}
