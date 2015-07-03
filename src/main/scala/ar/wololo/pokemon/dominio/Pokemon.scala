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
  val tipoSecundario = especie.tipoSecundario.getOrElse(tipoPrincipal)
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
  
  /*
   * Acá estaría bueno tirar una meta-magia como en Ruby, pero la API de reflection
   * no es tan copada, y no da mezclar meta-programación con un dominio específico.
   */
  
  private def getAtributoActual(unAtributo: Int, unIncremento: Int) = unAtributo + unIncremento * nivel
  
  def peso = getAtributoActual(pesoBase, especie.incrementoPeso)
  def fuerza = getAtributoActual(fuerzaBase, especie.incrementoFuerza)
  def velocidad = getAtributoActual(velocidadBase, especie.incrementoVelocidad)
  def energiaMax = getAtributoActual(energiaMaxBase, especie.incrementoEnergiaMax)

  def nivel = especie.getNivelPara(this)
  def sosDeTipo(tipo: Tipo) = List(tipoPrincipal, tipoSecundario).contains(tipo)
  
  def modificaPeso(cantidad: Int) = this.copy(pesoBase = this.pesoBase + cantidad).verificarParams
  def modificaVelocidad(cantidad: Int) = this.copy(velocidadBase = Math.min(this.velocidadBase + cantidad, this.velocidadMax)).verificarParams
  def modificaEnergia(cantidad: Int) = this.copy(energia = Math.min(this.energia + cantidad, this.energiaMax)).verificarParams
  def modificaFuerza(cantidad: Int) = this.copy(fuerzaBase = Math.min(this.fuerzaBase + cantidad, this.fuerzaMax)).verificarParams
  def modificaListaAtaque(listaNueva: List[(Ataque, Int, Int)]) = this.copy(listaAtaques = listaNueva)

  def aumentaExperiencia(exp: Long) = especie.aumentaExperiencia(this, exp)
  def aumentaExpEnBaseAGenero() = genero.aumentaExperiencia(this)
  def evolucionar = especie.evolucionarA(this)
  def evaluarEfectos(piedra: Piedra) = especie.evaluarEfectos(piedra, this)
  def realizarRutina(rutina: Rutina) = rutina.esHechaPor(this)
  def teIntercambiaron() = especie.intercambiaronA(this)

  def cambiaAEstado(nuevoEstado: EstadoPokemon) = this.copy(estado = nuevoEstado)
  def podesLevantar(kg: Long) = kg < (10 * this.fuerza + 1)
  def sabesAtaque(ataque: Ataque) = listaAtaques.map { _._1 }.contains(ataque)

  def verificarParams = {
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

