package ar.wololo.pokemon.dominio

class Ataque(val nombre: String,
             val efecto: Pokemon => Pokemon,
             val tipo: Tipo,
             var puntosAtaque: Integer,
             var puntosAtaqueMax: Integer){
  
  def aumentaPAMaximo(cantidad: Int) { this.puntosAtaqueMax += cantidad }
  
  def regenerate() { this.puntosAtaque = this.puntosAtaqueMax }
  
  def reduciPa(): Ataque = {
    this.puntosAtaque -= 1
    this  
  }
}