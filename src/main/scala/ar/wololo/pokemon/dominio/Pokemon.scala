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
  
  def aumentaPAMaximo(cant :Int):Pokemon = {
    this.listaAtaques.foreach{ataque => ataque.aumentaPAMaximo(cant)}
    this
  }
  
  def descansa():Pokemon = {
    this.listaAtaques.foreach{ataque => ataque.regenerate()}
    if(this.energia < this.energiaMax * 0.5)
      this.copy(estado = Dormido)
    else
      this
  }
  
  def sufriEfectosSecundarios(ataque :Ataque):Pokemon = {
    ataque.efecto(this)   
  }
  
  //metodo para que no rompa (ESTA MAL) 
  def evolucionar(condEvolucion :CondicionEvolutiva):Pokemon ={
    this
  }
  //
  def evolucionar(piedra :Piedra):Pokemon ={
    this
  }
}

trait ResultadoActividad 
case class NoPaso(pokemon :Pokemon,descripcion :String) extends ResultadoActividad
case class Paso(pokemon :Pokemon) extends ResultadoActividad


class Gimnasio(){
   def realizarActividad(pokemon :Pokemon,actividad : Actividad):ResultadoActividad = pokemon.estado match {
    case Ko => NoPaso (pokemon,"no pudo completar por estar ko")
    case Dormido => Paso (pokemon) //falta registrar que la cantidad de veces que durmio
    case _ => actividad match{
      case UsarPocion => Paso( pokemon.copy(energia = Math.min(pokemon.energia + 50, pokemon.energiaMax)))
      case UsarAntidoto => pokemon.estado match{
        case Envenenado => Paso( pokemon.copy(estado = Bueno))
        case _ => Paso (pokemon)
      }
      case UsarEther => Paso( pokemon.copy(estado = Bueno))
      case ComerHierro => Paso (pokemon.copy(fuerza = pokemon.fuerza + 5))
      case ComerCalcio => Paso (pokemon.copy(velocidad = pokemon.velocidad +5))
      case ComerZinc => Paso (pokemon.aumentaPAMaximo(2))
      case Descansar => Paso (pokemon.descansa)
      case FingirIntercambio => pokemon.condicionEvolutiva match {
        case Intercambiar => Paso(pokemon.evolucionar(Intercambiar)) //metodo de pablo, evoluciona mandandole intercambiar, el metodo debe devolver el pokemon evolucionado
        case _ => pokemon.genero match{
          case Macho => Paso(pokemon.copy(peso = pokemon.peso +1))
          case Hembra => Paso(pokemon.copy(peso = Math.max(0, pokemon.peso - 10)))
        }
      }
    }
  }
  
   def realizarActividad(pokemon :Pokemon, actividad :Actividad, ataqueARealizar :String):ResultadoActividad = actividad match { 
     case RealizarUnAtaque => {
       val resultadoAtaque  = pokemon.listaAtaques.find { ataque => ataque.nombre == ataqueARealizar; ataque.puntosAtaque > 0 }
       resultadoAtaque match{
         case None => NoPaso (pokemon, "el pokemon no conoce el movimiento o no tien PA") 
         case Some(resultadoAtaque) => {
           resultadoAtaque.reduciPa
           val pokemonAfectado = pokemon.sufriEfectosSecundarios(resultadoAtaque)
           resultadoAtaque.tipo match{
               case Dragon => Paso(pokemonAfectado.copy(experiencia = pokemon.experiencia + 80)) //el experiencia te tiene que hacer evolucionar si tu condEvolutiva es por exp, Aca Invocar Metodo Pablo 
               case pokemonAfectado.objetoPrincipal => Paso(pokemonAfectado.copy(experiencia = pokemonAfectado.experiencia +50)) 
               case pokemonAfectado.objetoSecundario => pokemonAfectado.genero match{
                 case Macho => Paso(pokemonAfectado.copy(experiencia = pokemonAfectado.experiencia +20))
                 case Hembra => Paso(pokemonAfectado.copy(experiencia = pokemonAfectado.experiencia +40))
               }                                         
           }
         } 
       }        
     }       
   }
   
   def realizarActividad(pokemon :Pokemon, actividad :Actividad, cantidad:Int):ResultadoActividad = actividad match { 
     case LevantarPesas => pokemon.estado match {
       case Paralizado => Paso(pokemon.copy(estado= Ko))
       case _ => {
         if(cantidad > 10 * pokemon.fuerza)
           (pokemon.objetoPrincipal , pokemon.objetoSecundario) match {
             case (Pelea ,_) | (_,Pelea) => Paso(pokemon.copy(experiencia = pokemon.experiencia + cantidad *2))
             case (Fantasma,_)|(_,Fantasma) => NoPaso(pokemon,"los pokemon tipo fantasma no pueden levantar pesas")
             case _ => Paso(pokemon.copy(experiencia = pokemon.experiencia + cantidad))
           }
         else
           Paso(pokemon.copy(estado= Paralizado))
       }
     }
     case Nadar => (pokemon.objetoPrincipal , pokemon.objetoSecundario) match {
       case (Agua ,_) => Paso(pokemon.copy(experiencia = pokemon.experiencia + cantidad *200 , energia= Math.max(0, pokemon.energia - cantidad),velocidad = pokemon.velocidad + Math.round(cantidad/60)))
       case (Fuego,_)|(_,Fuego)|(Tierra,_)|(_,Tierra)|(Roca,_)|(_,Roca) => Paso(pokemon.copy(estado= Ko))
       case _ => Paso(pokemon.copy(experiencia = pokemon.experiencia + cantidad *200 , energia= Math.max((0), pokemon.energia - cantidad)))
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

class Ataque(val nombre: String,
             val efecto: Pokemon => Pokemon,
             val tipo: Tipo,
             var puntosAtaque: Integer,
             var puntosAtaqueMax: Integer){
  
  def aumentaPAMaximo(cantidad :Int){this.puntosAtaqueMax = this.puntosAtaqueMax + cantidad}
  
  def regenerate() {this.puntosAtaque = this.puntosAtaqueMax}
  
  
  def reduciPa():Ataque = {
    this.puntosAtaque = this.puntosAtaque -1
    this  
  }
}

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
 object UsarUnaPiedraLunar extends CondicionEvolutiva{
   def evaluaCondicion(unPokemon: Pokemon) = true // Implementar VA POR ENUNCIADO
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