package ar.wololo.pokemon.dominio

case class Ataque(val nombre: String,
                  val efecto: Pokemon => Pokemon,
                  val tipo: Tipo) {

  def tePuedeAprender(pokemon: Pokemon): Boolean
       = List(Normal, pokemon.tipoPrincipal, pokemon.tipoSecundario)
         .contains(tipo)

  def teUtiliza(pokemon: Pokemon): Pokemon = {
    val pokeAfectado = efecto(pokemon.reducirPa(this))
    var experiencia = 0

    tipo match {
      case pokeAfectado.tipoSecundario => pokeAfectado.aumentaExpEnBaseAGenero()
      case t =>
        t match {
          case Dragon => experiencia = 80
          case pokeAfectado.tipoPrincipal => experiencia = 50
        }
        pokeAfectado.aumentaExperiencia(experiencia)
    }
  }
}