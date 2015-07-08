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

  private def descansar(): Pokemon = {
    val nuevaListaAtaque = this.listaAtaques.map { case (ataque: Ataque, _, paMax: Int) => new Tuple3(ataque, paMax, paMax) }
    val pokeAfectado = energia match {
      case energia if energia < energiaMax * 0.5 => this.cambiaAEstado(Dormido(3))
      case _ => this
    }

    pokeAfectado.modificaListaAtaque(nuevaListaAtaque)
  }

  private def aumentaPAMaximo(cant: Int): Pokemon = {
    val nuevaListaAtaque = this.listaAtaques.map { case (ataque: Ataque, pa: Int, paMax: Int) => (ataque, pa, paMax + cant) }
    this.modificaListaAtaque(nuevaListaAtaque)
  }

  def nadar(minutos: Int): Pokemon = {
    (tipoPrincipal, tipoSecundario) match {
      case (Fuego, _) | (_, Fuego) | (Tierra, _) | (_, Tierra) | (Roca, _) | (_, Roca) => this.cambiaAEstado(Ko)
      case (tP, tS) =>
        val pokeAfectado = (tP, tS) match {
          case (Agua, _) | (_, Agua) => {
            this.modificaVelocidad(minutos)
          }
          case _ => this
        }
        pokeAfectado.modificaEnergia(-minutos).aumentaExperiencia(minutos * 200)
    }
  }
  
  def levantaSiPodes(kg: Int): Pokemon = {
    if (this.podesLevantar(kg)) {
      (this.tipoPrincipal, this.tipoSecundario) match {
        case (Pelea, _) | (_, Pelea) => this.aumentaExperiencia(kg * 2)
        case (Fantasma, _) | (_, Fantasma) => throw FantasmaNoPuedeLevantarPesas(this)
        case _ => this.aumentaExperiencia(kg)
      }
    } else {
      this.cambiaAEstado(Paralizado)
    }
  }
  
  def podesLevantar(kg: Int): Boolean = kg < (10 * this.fuerza + 1)

  def aprenderAtaque(ataqueAAprender: (Ataque, Int, Int)): Pokemon = {
    if (ataqueAAprender._1.tePuedeAprender(this))
      this.modificaListaAtaque(ataqueAAprender :: this.listaAtaques)
    else
      this.cambiaAEstado(Ko)
  }

  def realizarAtaque(ataqueARealizar: Ataque): Pokemon = {
    listaAtaques.find { case (ataque, pa, _) => ataque.equals(ataqueARealizar) && pa > 0 }
      .fold { throw PokemonNoConoceMovONoTienePA(this) } { case (atk, _, _) => atk.teUtiliza(this) }
  }
  
  def aprendeAtaque(ataqueAAprender: (Ataque,Int,Int)):Pokemon = ataqueAAprender._1.tipo match {
     case Normal | this.tipoPrincipal | this.tipoSecundario => this.copy(listaAtaques = ataqueAAprender :: this.listaAtaques)
     case _ => this.copy(estado = Ko)
  }
  

  def reducirPa(ataque: Ataque): Pokemon = {
    val listaAtaquesNueva = listaAtaques.map {
      case (attack: Ataque, puntosAtaque, pAMax) if attack.equals(ataque) => (attack, puntosAtaque - 1, pAMax)
      case atk => atk
    }
    this.modificaListaAtaque(listaAtaquesNueva)
  }

  def realizarActividad(actividad: Actividad): Pokemon = {
    this.estado match {
      case Ko => throw EstaKo(this)
      case e: Dormido if e.turnos > 0 => this.cambiaAEstado(Dormido(e.turnos - 1))
      case e: Dormido => this.cambiaAEstado(Bueno).realizarActividad(actividad)
      case _: EstadoPokemon => actividad match {
        case UsarPocion => this.modificaEnergia(50)
        case UsarEther => this.cambiaAEstado(Bueno)
        case ComerHierro => this.modificaFuerza(5)
        case ComerCalcio => this.modificaVelocidad(5)
        case ComerZinc => this.aumentaPAMaximo(2)
        case Descansar => this.descansar
        case UsarAntidoto => this.estado match {
          case Envenenado => this.cambiaAEstado(Bueno)
          case _: EstadoPokemon => this
        }
        case FingirIntercambio => this.teIntercambiaron()
        case actividad: UsarPiedra => this.evaluarEfectos(actividad.piedra)
        case actividad: Nadar => this.nadar(actividad.minutos)
        case actividad: LevantarPesas => this.estado match {
          case Paralizado => this.cambiaAEstado(Ko)
          case _: EstadoPokemon => this.levantaSiPodes(actividad.kg)
        }
        case RealizarUnAtaque(ataqueARealizar) => this.realizarAtaque(ataqueARealizar)
        case AprenderAtaque(ataqueAAprender) => this.aprendeAtaque(ataqueAAprender)
      }
    }
  }
}

