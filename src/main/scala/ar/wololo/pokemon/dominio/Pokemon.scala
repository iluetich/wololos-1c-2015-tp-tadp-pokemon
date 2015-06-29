package ar.wololo.pokemon.dominio

import scala.util.Try

case class Pokemon(
    val estado: EstadoPokemon,
    val listaAtaques: List[(Ataque, Int, Int)], //Ataque, puntos de ataque actual, puntos de ataque maximos
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
  val condicionEvolutiva: Option[CondicionEvolutiva] = especie.condicionEvolutiva

  def aumentaExperiencia(cantidad: Long): Pokemon = especie.aumentaExperienciaDe(this, cantidad)
  def evolucionar: Pokemon = especie.evolucionarA(this)
  def realizarRutina(rutina: Rutina): Try[Pokemon] = rutina.esHechaPor(this)

  def aumentaExpEnBaseAGenero(): Pokemon = genero.aumentaExperiencia(this)
  def teIntercambiaron(): Pokemon = genero.fingiIntercambio(this)

  def modificaPeso(cantidad: Int): Pokemon = this.copy(peso = this.peso + cantidad).verificarParams()
  def modificaVelocidad(cantidad: Int): Pokemon = this.copy(velocidad = Math.min(this.velocidad + cantidad, this.velocidadMax)).verificarParams()
  def modificaEnergia(cantidad: Int): Pokemon = this.copy(energia = Math.min(this.energia + cantidad, this.energiaMax)).verificarParams()
  def modificaFuerza(cantidad: Int): Pokemon = this.copy(fuerza = Math.min(this.fuerza + cantidad, this.fuerzaMax)).verificarParams()
  def modificaListaAtaque(listaNueva: List[(Ataque, Int, Int)]): Pokemon = this.copy(listaAtaques = listaNueva)

  def cambiaAEstado(nuevoEstado: EstadoPokemon): Pokemon = this.copy(estado = nuevoEstado)

  def evaluarEfectos(piedra: Piedra): Pokemon = condicionEvolutiva.fold { this } { _.evaluarEfectosPiedra(this, piedra) }

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

  implicit class aumentosPA(ataque: (Ataque, Int, Int)) {
    def aumentarPAMax(i: Int) = (ataque._1, ataque._2, ataque._3 + i) // aumento paMax
    def regenerar = (ataque._1, ataque._3, ataque._3) // paAct = paMAx
    def sosIgualA(unAtaque: Ataque) = ataque._1.eq(unAtaque) && ataque._2 > 0
    def teUtiliza(pokemon: Pokemon) = ataque._1.teUtiliza(pokemon)
  }

  def descansar(): Pokemon = {
    val nuevaListaAtaque = this.listaAtaques.map { _.regenerar }
    val pokeAfectado = energia match {
      case energia if energia < energiaMax * 0.5 => this.cambiaAEstado(Dormido(3))
      case _ => this
    }

    pokeAfectado.modificaListaAtaque(nuevaListaAtaque)
  }

  def aumentaPAMaximo(cant: Int): Pokemon = {
    val nuevaListaAtaque = this.listaAtaques.map { _.aumentarPAMax(cant) }
    this.modificaListaAtaque(nuevaListaAtaque)
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

  def podesLevantar(kg: Int): Boolean = kg < (10 * this.fuerza + 1)

  def aprenderAtaque(ataqueAAprender: (Ataque, Int, Int)): Pokemon = {
    if (ataqueAAprender._1.tePuedeAprender(this))
      this.modificaListaAtaque(ataqueAAprender :: this.listaAtaques)
    else
      this.cambiaAEstado(Ko)
  }

  def realizarAtaque(ataqueARealizar: Ataque): Pokemon = {
    listaAtaques
      .find { _.sosIgualA(ataqueARealizar) }
      .fold { throw PokemonNoConoceMovONoTienePA(this) } { _.teUtiliza(this) }
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
