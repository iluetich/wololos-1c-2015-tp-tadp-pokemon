package ar.wololo.pokemon.dominio

import scala.util.Try

case class Pokemon(
  val estado: EstadoPokemon,
  val listaAtaques: List[Ataque],
  val tipoPrincipal: Tipo,
  val tipoSecundario: Tipo,
  val nivel: Integer,
  val experiencia: Integer,
  val genero: Genero,
  val energia: Integer,
  val energiaMax: Integer,
  val peso: Integer,
  val fuerza: Integer,
  val velocidad: Integer,
  val condicionEvolutiva: CondicionEvolutiva,
  val experienciaSaltoNivel: Integer,
  val resistenciaEvolutiva: Integer) {

  def ganarExperiencia(expGanada: Integer): Pokemon = {
    var experienciaSaltoNivelFutura = this.experienciaSaltoNivel
    var experienciaFutura = this.experiencia + expGanada

    var nuevoNivel: Integer = if (experienciaFutura >= experienciaSaltoNivelFutura) {
      experienciaSaltoNivelFutura += resistenciaEvolutiva
      var saltoNivel: Integer = 1
      while (experienciaFutura >= experienciaSaltoNivelFutura) {
        experienciaSaltoNivelFutura += resistenciaEvolutiva
        saltoNivel += 1
      }
      this.nivel + saltoNivel
    } else {
      this.nivel
    }
    copy(experiencia = experienciaFutura, experienciaSaltoNivel = experienciaSaltoNivelFutura, nivel = nuevoNivel)
  }

  override def equals(unPokemon: Any): Boolean = {
    var that: Pokemon = unPokemon.asInstanceOf[Pokemon]
    return that.estado == this.estado &&
      that.listaAtaques == this.listaAtaques &&
      that.tipoPrincipal == this.tipoPrincipal &&
      that.tipoSecundario == this.tipoSecundario &&
      that.nivel == this.nivel &&
      that.experiencia == this.experiencia &&
      that.genero == this.genero &&
      that.energia == this.energia &&
      that.energiaMax == this.energiaMax &&
      that.peso == this.peso &&
      that.fuerza == this.fuerza &&
      that.velocidad == this.velocidad &&
      that.condicionEvolutiva == this.condicionEvolutiva &&
      that.experienciaSaltoNivel == this.experienciaSaltoNivel &&
      that.resistenciaEvolutiva == this.resistenciaEvolutiva
  }

  def aumentaPAMaximo(cant: Int): Pokemon = {
    listaAtaques.foreach { ataque => ataque.aumentaPAMaximo(cant) }
    this
  }

  def verificarParams(): Pokemon = {
    if (energia < 0)
      throw NoPuedeEnergiaMenorACero(this)
    if (peso < 0)
      throw NoPuedePesoInvalido(this)
    if (fuerza < 0 || fuerza > 100)
      throw NoPuedeFuerzaInvalida(this)
    if (velocidad < 0 || velocidad > 100)
      throw NoPuedeVelocidadInvalida(this)
    this
  }

  def descansa(): Pokemon = {
    this.listaAtaques.foreach { ataque => ataque.regenerate() }
    if (energia < energiaMax * 0.5)
      this.copy(estado = Dormido(3))
    else
      this
  }

  def sufriEfectosSecundarios(ataque: Ataque): Pokemon = {
    ataque.efecto(this)
  }

  //metodo para que no rompa (ESTA MAL) 
  def evolucionar(condEvolucion: CondicionEvolutiva): Pokemon = {
    this
  }
  //
  def evolucionar(): Pokemon = {
    this //ACA SE TIENE QUE HACER LA EVOLUCION POR PIEDRA Y RETORNAR LA EVOLUCION
  }

  def realizarRutina(rutina: Rutina): Try[Pokemon] = {
    rutina.esHechaPor(this)
  }

  def realizarActividad(actividad: Actividad): Pokemon = this.estado match {
    case Ko => throw EstaKo(this)
    case e: Dormido => {
      if (e.turnos > 0)
        this.copy(estado = Dormido(e.turnos - 1))
      else {
        this.copy(estado = Bueno).realizarActividad(actividad)
      }
    }
    case _ => actividad match {
      case UsarPocion => this.copy(energia = Math.min(this.energia + 50, this.energiaMax))
      case UsarAntidoto => this.estado match {
        case Envenenado => this.copy(estado = Bueno)
        case _ => this
      }
      case UsarEther => this.copy(estado = Bueno)
      case ComerHierro => this.copy(fuerza = this.fuerza + 5).verificarParams()
      case ComerCalcio => this.copy(velocidad = this.velocidad + 5).verificarParams()
      case ComerZinc => this.aumentaPAMaximo(2)
      case Descansar => this.descansa
      case FingirIntercambio => this.condicionEvolutiva match {
        case Intercambiar => this.evolucionar() //Como me intercambiaron, evoluciono porque mi condicion evolutiva es Intercambiar. [Requerimiento-TP]
        case _ => this.genero match {
          case Macho => this.copy(peso = this.peso + 1).verificarParams()
          case Hembra => this.copy(peso = this.peso - 10).verificarParams()
        }
      }
      case actividad: UsarPiedra => this.condicionEvolutiva match {
        case UsarUnaPiedraLunar => actividad.piedra match {
          case PiedraLunar => this.evolucionar()
          case _ => this
        }
        case UsarUnaPiedra => actividad.piedra match {
          case p: PiedraEvolutiva => p.tipo match {
            case this.tipoPrincipal => this.evolucionar()
            case tipoDistinto => {
              val piedraDaniaPokemon = tipoDistinto.leGanaA.count { tipo => tipo == this.tipoPrincipal | tipo == this.tipoSecundario }
              if (piedraDaniaPokemon > 0)
                this.copy(estado = Envenenado)
              else
                this
            }
          }
          case _ => throw new Exception("Me llego una piedra de tipo desconocida. BOOM!")
        }
      }
      case actividad: LevantarPesas => this.estado match {
        case Paralizado => this.copy(estado = Ko)
        case _ => {
          if (actividad.kg < (10 * this.fuerza + 1))
            (this.tipoPrincipal, this.tipoSecundario) match {
              case (Pelea, _) | (_, Pelea) => this.ganarExperiencia(actividad.kg * 2)
              case (Fantasma, _) | (_, Fantasma) => throw FantasmaNoPuedeLevantarPesas(this)
              case _ => this.ganarExperiencia(actividad.kg)
            }
          else
            this.copy(estado = Paralizado)
        }
      }
      case actividad: Nadar => (this.tipoPrincipal, this.tipoSecundario) match {
        case (Agua, _) => this.copy(energia = this.energia - actividad.minutos, velocidad = this.velocidad + Math.round(actividad.minutos / 60)).verificarParams().ganarExperiencia(actividad.minutos * 200)
        case (Fuego, _) | (_, Fuego) | (Tierra, _) | (_, Tierra) | (Roca, _) | (_, Roca) => this.copy(estado = Ko)
        case _ => this.copy(energia = this.energia - actividad.minutos).verificarParams().ganarExperiencia(actividad.minutos * 200)
      }
      case actividad: RealizarUnAtaque => {
        val resultadoAtaque = this.listaAtaques.find { ataque => (ataque.nombre == actividad.ataqueARealizar.nombre && ataque.puntosAtaque > 0) }
        resultadoAtaque match {
          case None => throw PokemonNoConoceMovONoTienePA(this)
          case Some(resultadoAtaque) => {
            resultadoAtaque.reduciPa 
            val pokemonAfectado = this.sufriEfectosSecundarios(resultadoAtaque).verificarParams()
            resultadoAtaque.tipo match { 
              case Dragon => pokemonAfectado.ganarExperiencia(80)  
              case pokemonAfectado.tipoPrincipal => pokemonAfectado.ganarExperiencia(50)
              case pokemonAfectado.tipoSecundario => pokemonAfectado.genero match {
                case Macho => pokemonAfectado.ganarExperiencia(20)
                case Hembra => pokemonAfectado.ganarExperiencia(40)
              }
            }
          }
        }
      }
      case actividad: AprenderAtaque => actividad.ataqueAAprender.tipo match {
        case Normal | this.tipoPrincipal | this.tipoSecundario => this.copy(listaAtaques = actividad.ataqueAAprender :: this.listaAtaques)
        case _ => this.copy(estado = Ko)
      }
    }
  }
}
