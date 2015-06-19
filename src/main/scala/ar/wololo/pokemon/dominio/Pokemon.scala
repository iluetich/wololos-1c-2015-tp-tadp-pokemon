package ar.wololo.pokemon.dominio

import scala.util.Try

case class Pokemon(
  val estado: EstadoPokemon,
  val listaAtaques: List[Ataque],
  val nivel: Int,
  val experiencia: Long,
  val genero: Genero,
  val energia: Int,
  val energiaMax: Int,
  val peso: Int,
  val fuerza: Int,
  val velocidad: Int,
  val especie: Especie) {

  val velocidadMax = 100 //constante de enunciado
  val fuerzaMax = 100 //constante de enunciado
  val tipoPrincipal: Tipo = especie.tipoPrincipal
  val tipoSecundario: Tipo = especie.tipoSecundario
  val pesoMaximoSaludable: Int = especie.pesoMaximoSaludable
  val condicionEvolutiva: CondicionEvolutiva = especie.condicionEvolutiva

  def aumentaExperiencia(cantidad: Long): Pokemon = especie.aumentaExperienciaDe(this, cantidad)
  def evolucionar: Pokemon = especie.evolucionarA(this)
  def realizarRutina(rutina: Rutina): Try[Pokemon] = rutina.esHechaPor(this)
  private def sufriEfectosSecundarios(ataque: Ataque): Pokemon = ataque.efecto(this)

  def aumentaExpEnBaseAGenero():Pokemon = genero.aumentaExperiencia(this)
  def fingiIntercambio():Pokemon = genero.fingiIntercambio(this)
  
  def modificaPeso(cantidad :Int):Pokemon = this.copy(peso = this.peso + cantidad).verificarParams()

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

  private def descansar(): Pokemon = {
    this.listaAtaques.foreach { ataque => ataque.regenerate() }
    
    energia match {
      case energia if energia < energiaMax * 0.5 => copy(estado = Dormido(3))
      case _ => this
    }
  }

  private def aumentaPAMaximo(cant: Int): Pokemon = {
    listaAtaques.foreach { ataque => ataque.aumentaPAMaximo(cant) }
    this
  }
  
  def evaluarEfectos(piedra : Piedra):Pokemon = condicionEvolutiva.evaluarEfectosPiedra(this, piedra)

  def realizarActividad(actividad: Actividad): Pokemon = {
    val futuroPokemon = this.estado match {
      case Ko => throw EstaKo(this)
      case e: Dormido if e.turnos > 0 => this.copy(estado = Dormido(e.turnos - 1))
      case e: Dormido => this.copy(estado = Bueno).realizarActividad(actividad)
      case _: EstadoPokemon => actividad match {
        case UsarPocion => this.copy(energia = Math.min(this.energia + 50, this.energiaMax))
        case UsarEther => this.copy(estado = Bueno)
        case ComerHierro => this.copy(fuerza = Math.min(this.fuerza + 5, this.fuerzaMax))
        case ComerCalcio => this.copy(velocidad = Math.min(this.velocidad + 5, this.velocidadMax))
        case ComerZinc => this.aumentaPAMaximo(2)
        case Descansar => this.descansar
        case UsarAntidoto => this.estado match {
          case Envenenado => this.copy(estado = Bueno)
          case _: EstadoPokemon => this
        }
        case FingirIntercambio => this.condicionEvolutiva match {
          case Intercambiar => this.evolucionar //Como me intercambiaron, evoluciono porque mi condicion evolutiva es Intercambiar. [Requerimiento-TP]
          case _: CondicionEvolutiva => this.fingiIntercambio()
        }
        case actividad: UsarPiedra => this.evaluarEfectos(actividad.piedra)
        case actividad: LevantarPesas => this.estado match {
          case Paralizado => this.copy(estado = Ko)
          case _: EstadoPokemon => {
            if (actividad.kg < (10 * this.fuerza + 1))
              (this.tipoPrincipal, this.tipoSecundario) match {
                case (Pelea, _) | (_, Pelea) => this.aumentaExperiencia(actividad.kg * 2)
                case (Fantasma, _) | (_, Fantasma) => throw FantasmaNoPuedeLevantarPesas(this)
                case _ => this.aumentaExperiencia(actividad.kg)
              }
            else
              this.copy(estado = Paralizado)
          }
        }
        case actividad: Nadar => (this.tipoPrincipal, this.tipoSecundario) match {
          case (Agua, _)|(_,Agua) => this.copy(energia = this.energia - actividad.minutos, velocidad = this.velocidad + actividad.minutos).verificarParams().aumentaExperiencia(actividad.minutos * 200)
          case (Fuego, _) | (_, Fuego) | (Tierra, _) | (_, Tierra) | (Roca, _) | (_, Roca) => this.copy(estado = Ko)
          case _ => this.copy(energia = this.energia - actividad.minutos).verificarParams().aumentaExperiencia(actividad.minutos * 200)
        }
        case actividad: RealizarUnAtaque => {
          val resultadoAtaque = this.listaAtaques.find { ataque => (ataque.nombre == actividad.ataqueARealizar.nombre && ataque.puntosAtaque > 0) }
          resultadoAtaque match {
            case None => throw PokemonNoConoceMovONoTienePA(this)
            case Some(resultadoAtaque) => {
              resultadoAtaque.reduciPa
              val pokemonAfectado = this.sufriEfectosSecundarios(resultadoAtaque)
              resultadoAtaque.tipo match {
                case Dragon => pokemonAfectado.aumentaExperiencia(80)
                case pokemonAfectado.tipoPrincipal => pokemonAfectado.aumentaExperiencia(50)
                case pokemonAfectado.tipoSecundario => pokemonAfectado.aumentaExpEnBaseAGenero()
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
    futuroPokemon.verificarParams() // retorno el posible pokemon habiendo realizado la actividad
  }

  override def equals(unPokemon: Any): Boolean = {
    // XXX Revisar
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
      that.especie.resistenciaEvolutiva == especie.resistenciaEvolutiva
  }
}
