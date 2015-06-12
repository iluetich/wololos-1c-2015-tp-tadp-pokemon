package ar.wololo.pokemon.dominio

import scala.util.Try

case class Pokemon(
  val estado: EstadoPokemon,
  val listaAtaques: List[Ataque],
  val objetoPrincipal: Tipo,
  val objetoSecundario: Tipo,
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
      that.objetoPrincipal == this.objetoPrincipal &&
      that.objetoSecundario == this.objetoSecundario &&
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
    case _: Dormido => {
      val estado = this.estado.asInstanceOf[Dormido]
      if (estado.turnos > 0)
        this.copy(estado = Dormido(estado.turnos - 1))
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
        case Intercambiar => this.evolucionar(Intercambiar) //metodo de pablo, evoluciona mandandole intercambiar, el metodo debe devolver el pokemon evolucionado
        case _ => this.genero match {
          case Macho => this.copy(peso = this.peso + 1).verificarParams()
          case Hembra => this.copy(peso = this.peso - 10).verificarParams()
        }
      }
      case _: UsarPiedra => this.condicionEvolutiva match {
        case UsarUnaPiedraLunar => actividad.asInstanceOf[UsarPiedra].piedra match {
          case PiedraLunar => this.evolucionar
          case _ => this
        }
        case UsarUnaPiedra => actividad.asInstanceOf[UsarPiedra].piedra.asInstanceOf[PiedraEvolutiva].tipo match {
          case this.objetoPrincipal => this.evolucionar()
          case _ => {
            val piedraDaniaPokemon = actividad.asInstanceOf[UsarPiedra].piedra.asInstanceOf[PiedraEvolutiva].tipo.leGanaA.count { tipo => tipo == this.objetoPrincipal | tipo == this.objetoSecundario }
            if (piedraDaniaPokemon > 0)
              this.copy(estado = Envenenado)
            else
              this
          }
        }
      }
      case _: LevantarPesas => this.estado match {
        case Paralizado => this.copy(estado = Ko)
        case _ => {
          if (actividad.asInstanceOf[LevantarPesas].kg < (10 * this.fuerza + 1))
            (this.objetoPrincipal, this.objetoSecundario) match {
              case (Pelea, _) | (_, Pelea) => this.copy(experiencia = this.experiencia + actividad.asInstanceOf[LevantarPesas].kg * 2)
              case (Fantasma, _) | (_, Fantasma) => throw FantasmaNoPuedeLevantarPesas(this)
              case _ => this.copy(experiencia = this.experiencia + actividad.asInstanceOf[LevantarPesas].kg)
            }
          else
            this.copy(estado = Paralizado)
        }
      }
      case _: Nadar => (this.objetoPrincipal, this.objetoSecundario) match {
        case (Agua, _) => this.copy(experiencia = this.experiencia + actividad.asInstanceOf[Nadar].minutos * 200, energia = this.energia - actividad.asInstanceOf[Nadar].minutos, velocidad = this.velocidad + Math.round(actividad.asInstanceOf[Nadar].minutos / 60)).verificarParams()
        case (Fuego, _) | (_, Fuego) | (Tierra, _) | (_, Tierra) | (Roca, _) | (_, Roca) => this.copy(estado = Ko)
        case _ => this.copy(experiencia = this.experiencia + actividad.asInstanceOf[Nadar].minutos * 200, energia = this.energia - actividad.asInstanceOf[Nadar].minutos).verificarParams()
      }
      case _: RealizarUnAtaque => {
        val resultadoAtaque = this.listaAtaques.find { ataque => (ataque.nombre == actividad.asInstanceOf[RealizarUnAtaque].ataqueARealizar.nombre && ataque.puntosAtaque > 0) }
        resultadoAtaque match {
          case None => throw PokemonNoConoceMovONoTienePA(this)
          case Some(resultadoAtaque) => {
            resultadoAtaque.reduciPa
            val pokemonAfectado = this.sufriEfectosSecundarios(resultadoAtaque).verificarParams()
            resultadoAtaque.tipo match {
              case Dragon => pokemonAfectado.copy(experiencia = this.experiencia + 80) //el experiencia te tiene que hacer evolucionar si tu condEvolutiva es por exp, Aca Invocar Metodo Pablo 
              case pokemonAfectado.objetoPrincipal => pokemonAfectado.copy(experiencia = pokemonAfectado.experiencia + 50)
              case pokemonAfectado.objetoSecundario => pokemonAfectado.genero match {
                case Macho => pokemonAfectado.copy(experiencia = pokemonAfectado.experiencia + 20)
                case Hembra => pokemonAfectado.copy(experiencia = pokemonAfectado.experiencia + 40)
              }
            }
          }
        }
      }
      case _: AprenderAtaque => actividad.asInstanceOf[AprenderAtaque].ataqueAAprender.tipo match {
        case Normal | this.objetoPrincipal | this.objetoSecundario => this.copy(listaAtaques = actividad.asInstanceOf[AprenderAtaque].ataqueAAprender :: this.listaAtaques)
        case _ => this.copy(estado = Ko)
      }
    }
  }
}
