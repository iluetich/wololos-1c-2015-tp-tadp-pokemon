package ar.wololo.pokemon.dominio

case class Ataque(val nombre: String,
    val efecto: Pokemon => Pokemon,
    val tipo: Tipo,
    var puntosAtaque: Integer,
    var puntosAtaqueMax: Integer) {

  def aumentaPaMax(cant: Int): Ataque = copy(puntosAtaqueMax = puntosAtaqueMax + cant)
  def regenerar: Ataque = copy(puntosAtaque = puntosAtaqueMax)
  def teUsa(pokemon: Pokemon): Pokemon = {
    val ataqueUsado = copy(puntosAtaque = puntosAtaque - 1)
    val pokeAfectado = efecto(pokemon)
    pokeAfectado.copy(listaAtaques = ataqueUsado :: pokemon.listaAtaques.filterNot { _.eq(this) })
  }
}