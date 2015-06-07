package ar.wololo.pokemon.dominio
import scala.util.{ Try, Success, Failure }

case class Pokemon(val estado: EstadoPokemon,
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
  val condicionEvolutiva: CondicionEvolutiva) {

  def hacerRutina(rutina: Rutina): Try[Pokemon] = {
    rutina.esHechaPor(this)
  }

  def hacerActividad(actividad: Actividad): Try[Pokemon] = { //MOCK ACTIVIDAD
    Success(this)
  }

  def aumentaPAMaximo(cant: Int): Pokemon = {
    this.listaAtaques.foreach { ataque => ataque.aumentaPAMaximo(cant) }
    this
  }

  def descansa(): Pokemon = {
    this.listaAtaques.foreach { ataque => ataque.regenerate() }
    if (this.energia < this.energiaMax * 0.5)
      this.copy(estado = Dormido)
    else
      this
  }

  //metodo para que no rompa (ESTA MAL) 
  def evolucionar(condEvolucion: CondicionEvolutiva): Pokemon = {
    this
  }
  //
}

trait ResultadoActividad
case class NoPaso(pokemon: Pokemon, descripcion: String) extends ResultadoActividad
case class Paso(pokemon: Pokemon) extends ResultadoActividad

class Gimnasio() {
  def realizaActividad(pokemon: Pokemon, actividad: Actividad): ResultadoActividad = pokemon.estado match {
    case Ko => NoPaso(pokemon, "no pudo completar por estar ko")
    case Dormido => Paso(pokemon) //falta registrar que la cantidad de veces que durmio
    case _ => actividad match {
      case UsarPocion => Paso(pokemon.copy(energia = Math.min(pokemon.energia + 50, pokemon.energiaMax)))
      case UsarAntidoto => pokemon.estado match {
        case Envenenado => Paso(pokemon.copy(estado = Bueno))
        case _ => Paso(pokemon)
      }
      case UsarEther => Paso(pokemon.copy(estado = Bueno))
      case ComerHierro => Paso(pokemon.copy(fuerza = pokemon.fuerza + 5))
      case ComerCalcio => Paso(pokemon.copy(velocidad = pokemon.velocidad + 5))
      case ComerZinc => Paso(pokemon.aumentaPAMaximo(2))
      case Descansar => Paso(pokemon.descansa)
      case FingirIntercambio => pokemon.condicionEvolutiva match {
        case Intercambiar => Paso(pokemon.evolucionar(Intercambiar)) //metodo de pablo, evoluciona mandandole intercambiar, el metodo debe devolver el pokemon evolucionado
        case _ => pokemon.genero match {
          case Macho => Paso(pokemon.copy(peso = pokemon.peso + 1))
          case Hembra => Paso(pokemon.copy(peso = Math.max(0, pokemon.peso - 10)))
        }
      }
    }
  }
}

class Tipo
object Fuego extends Tipo
object Agua extends Tipo
object Planta extends Tipo
object Tierra extends Tipo
object Hielo extends Tipo
object Roca extends Tipo
object Electrico extends Tipo
object Psiquico extends Tipo
object Pelea extends Tipo
object Fantasma extends Tipo
object Volador extends Tipo
object Bicho extends Tipo
object Veneno extends Tipo
object Dragon extends Tipo
object Normal extends Tipo

abstract class EstadoPokemon
object Bueno extends EstadoPokemon
object Dormido extends EstadoPokemon
object Paralizado extends EstadoPokemon
object Envenenado extends EstadoPokemon
object Ko extends EstadoPokemon

class Ataque(val efecto: Pokemon => Pokemon,
  val tipo: Tipo,
  var puntosAtaque: Integer,
  var puntosAtaqueMax: Integer) {

  def aumentaPAMaximo(cantidad: Int) { this.puntosAtaqueMax = this.puntosAtaqueMax + cantidad }

  def regenerate() { this.puntosAtaque = this.puntosAtaqueMax }
}

abstract class CondicionEvolutiva {
  def evaluaCondicion(unPokemon: Pokemon): Boolean
}
object SubirDeNivel extends CondicionEvolutiva {
  def evaluaCondicion(unPokemon: Pokemon) = true // Implementar
}
object Intercambiar extends CondicionEvolutiva {
  def evaluaCondicion(unPokemon: Pokemon) = true // Implementar
}
object UsarUnaPiedra extends CondicionEvolutiva {
  def evaluaCondicion(unPokemon: Pokemon) = true // Implementar
}

abstract class Piedra
class PiedraEvolutiva(val tipo: Tipo) extends Piedra
class PiedraLunar extends Piedra

abstract class Actividad
object RealizarUnAtaque extends Actividad
object LevantarPesas extends Actividad
object Nadar extends Actividad
object AprenderAtaque extends Actividad
object UsarPiedra extends Actividad
object UsarPocion extends Actividad
object UsarAntidoto extends Actividad
object UsarEther extends Actividad
object ComerHierro extends Actividad
object ComerCalcio extends Actividad
object ComerZinc extends Actividad
object Descansar extends Actividad
object FingirIntercambio extends Actividad

class Genero
object Macho extends Genero
object Hembra extends Genero
