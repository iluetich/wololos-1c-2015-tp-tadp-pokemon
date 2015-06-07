package ar.wololo.pokemon.dominio

case class Pokemon ( val estado: EstadoPokemon,
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
  
}

trait ResultadoActividad 
case class NoPaso(pokemon :Pokemon,descripcion :String) extends ResultadoActividad
case class Paso(pokemon :Pokemon) extends ResultadoActividad


class Gimnasio(){
    def realizaActividad(pokemon :Pokemon,actividad : Actividad):ResultadoActividad = pokemon.estado match {
    case ko => NoPaso (pokemon,"no pudo completar por estar ko")
    case dormido => Paso (pokemon) //falta registrar que la cantidad de veces que durmio
    case _ => actividad match{
      case UsarPocion => Paso( pokemon.copy(energia = Math.min(pokemon.energia + 50, pokemon.energiaMax)))
      case UsarAntidoto => pokemon.estado match{
        case envenenado => Paso( pokemon.copy(estado = normal))
        case _ => Paso (pokemon)
      }
      case UsarEther => Paso( pokemon.copy(estado = normal))
      
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
object normal extends EstadoPokemon
object dormido extends EstadoPokemon
object paralizado extends EstadoPokemon
object envenenado extends EstadoPokemon
object ko extends EstadoPokemon

class Ataque(val efecto: Pokemon => Pokemon,
             val tipo: Tipo,
             var puntosAtaque: Integer,
             var puntosAtaqueMax: Integer)

 abstract class CondicionEvolutiva {
  def evaluaCondicion(unPokemon: Pokemon): Boolean
}
 object SubirDeNivel extends CondicionEvolutiva {
   def evaluaCondicion(unPokemon:Pokemon) =  true // Implementar
 }
 object Intercambiar extends CondicionEvolutiva {
   def evaluaCondicion(unPokemon: Pokemon) = true // Implementar
 }
 object UsarUnaPiedra extends CondicionEvolutiva {
   def evaluaCondicion(unPokemon: Pokemon) = true // Implementar
 }
 
 abstract class Piedra
 class PiedraEvolutiva (val tipo: Tipo) extends Piedra
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