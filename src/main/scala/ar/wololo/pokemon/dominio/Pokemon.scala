package ar.wololo.pokemon.dominio

import scala.util.Try

case class Pokemon(
    val estado: EstadoPokemon,
    val listaAtaques: List[(Ataque, Int, Int)], //Ataque, puntos de ataque actual, puntos de ataque maximos
    val experiencia: Long,
    val genero: Genero,
    val energia: Int,
    val energiaMaxBase: Int,
    val pesoBase: Int,
    val fuerzaBase: Int,
    val velocidadBase: Int,
    val especie: Especie) {

  val velocidadMax = 100 //constante de enunciado
  val fuerzaMax = 100 //constante de enunciado
  val tipoPrincipal = especie.tipoPrincipal
  val tipoSecundario = especie.tipoSecundario
  val pesoMaximoSaludable = especie.pesoMaximoSaludable
  val condicionEvolutiva = especie.condicionEvolutiva

  /*
   * Los atributos del estilo "...Base" son los que va acumulando el
   * pokemon a lo largo de la realización de actividades (No tienen sumado los incrementos).
   * Para ser consistente con la consigna del TP en cuanto a evoluciones,
   * que dice que es retroactiva para los incrementos, transformo los "atributos" 
   * a cálculos que utilizan los incrementos de la especie actual.
   * Si evoluciona, usará la nueva especie.
   */
  
  def nivel: Int = especie.getNivelPara(this)
  def peso: Int = pesoBase + especie.incrementoPeso * nivel
  def fuerza: Int = fuerzaBase + especie.incrementoFuerza * nivel
  def velocidad: Int = velocidadBase + especie.incrementoVelocidad * nivel
  def energiaMax: Int = energiaMaxBase + especie.incrementoEnergiaMax * nivel
  
  def aumentaExperiencia(exp: Long): Pokemon = especie.aumentaExperiencia(this, exp)
  def evolucionar: Pokemon = especie.evolucionarA(this) // puede que haya que cambiar
  def realizarRutina(rutina: Rutina): Try[Pokemon] = rutina.esHechaPor(this)
  def aumentaExpEnBaseAGenero(): Pokemon = genero.aumentaExperiencia(this)
  def teIntercambiaron(): Pokemon = especie.intercambiaronA(this)
  def evaluarEfectos(piedra: Piedra): Pokemon = especie.evaluarEfectos(piedra, this)

  def modificaPeso(cantidad: Int): Pokemon = this.copy(pesoBase = this.pesoBase + cantidad).verificarParams()
  def modificaVelocidad(cantidad: Int): Pokemon = this.copy(velocidadBase = Math.min(this.velocidadBase + cantidad, this.velocidadMax)).verificarParams()
  def modificaEnergia(cantidad: Int): Pokemon = this.copy(energia = Math.min(this.energia + cantidad, this.energiaMax)).verificarParams()
  def modificaFuerza(cantidad: Int): Pokemon = this.copy(fuerzaBase = Math.min(this.fuerzaBase + cantidad, this.fuerzaMax)).verificarParams()
  def modificaListaAtaque(listaNueva: List[(Ataque, Int, Int)]): Pokemon = this.copy(listaAtaques = listaNueva)

  def cambiaAEstado(nuevoEstado: EstadoPokemon): Pokemon = this.copy(estado = nuevoEstado)
  def podesLevantar(kg: Long): Boolean = kg < (10 * this.fuerza + 1)
  def sabesAtaque(ataque: Ataque) = listaAtaques.map { _._1 }.contains(ataque)

  def verificarParams(): Pokemon = {
    if (energia < 0)
      throw NoPuedeEnergiaMenorACero(this)
    if (peso < 0 || peso > pesoMaximoSaludable)
      throw NoPuedePesoInvalido(this)
    if (fuerza < 0)
      throw NoPuedeFuerzaInvalida(this)
    if (velocidad < 0)
      throw NoPuedeVelocidadInvalida(this)
    this
  }

  def reducirPa(ataque: Ataque): Pokemon = {
    val listaAtaquesNueva = listaAtaques.map {
      case (`ataque`, puntosAtaque, pAMax) => (ataque, puntosAtaque - 1, pAMax)
      case atk => atk
    }
    this.modificaListaAtaque(listaAtaquesNueva)
  }

  def realizarActividad(actividad: Pokemon => Pokemon): Pokemon = {
    estado match {
      case Ko => throw EstaKo(this)
      case e: Dormido => e.turnos match {
        case t if t > 0 => cambiaAEstado(Dormido(t - 1))
        case 0 => actividad(cambiaAEstado(Bueno))
      }
      case _: EstadoPokemon => actividad(this)
    }
  }

}

