package ar.wololo.pokemon.dominio

trait ResultadoActividad 
case class NoPaso(pokemon :Pokemon,descripcion :String) extends ResultadoActividad
case class Paso(pokemon :Pokemon) extends ResultadoActividad

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
  def evolucionar():Pokemon ={
    this //ACA SE TIENE QUE HACER LA EVOLUCION POR PIEDRA Y RETORNAR LA EVOLUCION
  }
  
  def realizarActividad(actividad : Actividad):ResultadoActividad = this.estado match {
    case Ko => NoPaso (this,"no pudo completar por estar ko")
    case Dormido => Paso (this) //falta registrar que la cantidad de veces que durmio
    case _ => actividad match{
      case UsarPocion => Paso( this.copy(energia = Math.min(this.energia + 50, this.energiaMax)))
      case UsarAntidoto => this.estado match{
        case Envenenado => Paso( this.copy(estado = Bueno))
        case _ => Paso (this)
      }
      case UsarEther => Paso( this.copy(estado = Bueno))
      case ComerHierro => Paso (this.copy(fuerza = this.fuerza + 5))
      case ComerCalcio => Paso (this.copy(velocidad = this.velocidad +5))
      case ComerZinc => Paso (this.aumentaPAMaximo(2))
      case Descansar => Paso (this.descansa)
      case FingirIntercambio => this.condicionEvolutiva match {
        case Intercambiar => Paso(this.evolucionar(Intercambiar)) //metodo de pablo, evoluciona mandandole intercambiar, el metodo debe devolver el pokemon evolucionado
        case _ => this.genero match{
          case Macho => Paso(this.copy(peso = this.peso +1))
          case Hembra => Paso(this.copy(peso = Math.max(0, this.peso - 10)))
        }
      }
      case _ : UsarPiedra => this.condicionEvolutiva match{
          case UsarUnaPiedraLunar => actividad.asInstanceOf[UsarPiedra].piedra match{
                 case PiedraLunar => Paso(this.evolucionar)
                 case _ => Paso(this)
          }
          case UsarUnaPiedra => actividad.asInstanceOf[UsarPiedra].piedra.asInstanceOf[PiedraEvolutiva].tipo match{
                 case this.objetoPrincipal => Paso(this.evolucionar())
                 case _ =>{
                       val piedraDaniaPokemon = actividad.asInstanceOf[UsarPiedra].piedra.asInstanceOf[PiedraEvolutiva].tipo.leGanaA.count{tipo => tipo == this.objetoPrincipal | tipo == this.objetoSecundario}
                       if(piedraDaniaPokemon > 0)
                       Paso(this.copy(estado= Envenenado))
                 else 
                       Paso(this)
                 }
          } 
      }
      case _ : LevantarPesas => this.estado match {
           case Paralizado => Paso(this.copy(estado= Ko))
           case _ => {
                 if(actividad.asInstanceOf[LevantarPesas].kg > 10 * this.fuerza)
                       (this.objetoPrincipal , this.objetoSecundario) match {
                           case (Pelea ,_) | (_,Pelea) => Paso(this.copy(experiencia = this.experiencia + actividad.asInstanceOf[LevantarPesas].kg *2))
                           case (Fantasma,_)|(_,Fantasma) => NoPaso(this,"los pokemon tipo fantasma no pueden levantar pesas")
                           case _ => Paso(this.copy(experiencia = this.experiencia + actividad.asInstanceOf[LevantarPesas].kg))
                       }
                 else
                       Paso(this.copy(estado= Paralizado))
           }
     }
     case _ : Nadar => (this.objetoPrincipal , this.objetoSecundario) match {
           case (Agua ,_) => Paso(this.copy(experiencia = this.experiencia + actividad.asInstanceOf[Nadar].minutos *200 , energia= Math.max(0, this.energia - actividad.asInstanceOf[Nadar].minutos),velocidad = this.velocidad + Math.round(actividad.asInstanceOf[Nadar].minutos/60)))
           case (Fuego,_)|(_,Fuego)|(Tierra,_)|(_,Tierra)|(Roca,_)|(_,Roca) => Paso(this.copy(estado= Ko))
           case _ => Paso(this.copy(experiencia = this.experiencia + actividad.asInstanceOf[Nadar].minutos *200 , energia= Math.max((0), this.energia - actividad.asInstanceOf[Nadar].minutos)))
     }
     case _ : RealizarUnAtaque => {
       val resultadoAtaque  = this.listaAtaques.find { ataque => (ataque.nombre == actividad.asInstanceOf[RealizarUnAtaque].ataqueARealizar.nombre && ataque.puntosAtaque > 0) }
       resultadoAtaque match{
         case None => NoPaso (this, "el pokemon no conoce el movimiento o no tien PA") 
         case Some(resultadoAtaque) => {
           resultadoAtaque.reduciPa
           val pokemonAfectado = this.sufriEfectosSecundarios(resultadoAtaque)
           resultadoAtaque.tipo match{
               case Dragon => Paso(pokemonAfectado.copy(experiencia = this.experiencia + 80)) //el experiencia te tiene que hacer evolucionar si tu condEvolutiva es por exp, Aca Invocar Metodo Pablo 
               case pokemonAfectado.objetoPrincipal => Paso(pokemonAfectado.copy(experiencia = pokemonAfectado.experiencia +50)) 
               case pokemonAfectado.objetoSecundario => pokemonAfectado.genero match{
                 case Macho => Paso(pokemonAfectado.copy(experiencia = pokemonAfectado.experiencia +20))
                 case Hembra => Paso(pokemonAfectado.copy(experiencia = pokemonAfectado.experiencia +40))
               }                                         
           }
         } 
       }        
     }
     case _ : AprenderAtaque => actividad.asInstanceOf[AprenderAtaque].ataqueAAprender.tipo match{
       case Normal | this.objetoPrincipal | this.objetoSecundario => Paso(this.copy(listaAtaques =  actividad.asInstanceOf[AprenderAtaque].ataqueAAprender :: this.listaAtaques))
       case _ => Paso(this.copy(estado = Ko))  
     }
    }
  }
}


trait Tipo{
  def leGanaA():List[Tipo]
}
object Fuego extends Tipo{
  def leGanaA():List[Tipo] = List(Planta , Hielo , Bicho )
}
object Agua extends Tipo{
  def leGanaA():List[Tipo] = List(Fuego , Tierra , Roca )
}
object Planta extends Tipo{
  def leGanaA():List[Tipo] = List(Agua , Tierra , Roca )
}
object Tierra extends Tipo{
  def leGanaA():List[Tipo] = List(Fuego , Electrico , Veneno , Roca )
}
object Hielo extends Tipo{
  def leGanaA():List[Tipo] = List(Planta , Tierra , Volador , Dragon )
}
object Roca extends Tipo{
  def leGanaA():List[Tipo] = List(Fuego , Hielo , Volador , Bicho )
}
object Electrico extends Tipo{
  def leGanaA():List[Tipo] = List(Agua , Volador )
}
object Psiquico extends Tipo{
  def leGanaA():List[Tipo] = List(Pelea , Veneno )
}
object Pelea extends Tipo{
  def leGanaA():List[Tipo] = List(Normal , Hielo , Roca )
}
object Fantasma extends Tipo{
  def leGanaA():List[Tipo] = List(Psiquico , Fantasma )
}
object Volador extends Tipo{
  def leGanaA():List[Tipo] = List(Planta , Pelea , Bicho )
}
object Bicho extends Tipo{
  def leGanaA():List[Tipo] = List(Planta , Psiquico )
}
object Veneno extends Tipo{
  def leGanaA():List[Tipo] = List(Planta )
}
object Dragon extends Tipo{
  def leGanaA():List[Tipo] = List(Dragon )
}
object Normal extends Tipo{
  def leGanaA():List[Tipo] = List( )
}


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
 case class PiedraEvolutiva (val tipo: Tipo) extends Piedra
 object PiedraLunar extends Piedra
 
 abstract class Actividad
 case class RealizarUnAtaque(val ataqueARealizar:Ataque) extends Actividad
 case class LevantarPesas(val kg:Int) extends Actividad
 case class Nadar(val minutos:Int) extends Actividad
 case class AprenderAtaque(val ataqueAAprender:Ataque) extends Actividad
 case class UsarPiedra(val piedra:Piedra) extends Actividad
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