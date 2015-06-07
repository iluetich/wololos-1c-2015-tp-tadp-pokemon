package ar.wololo.pockemon.dominio

class Pokemon ( val estado: EstadoPokemon,
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

class EstadoPokemon
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