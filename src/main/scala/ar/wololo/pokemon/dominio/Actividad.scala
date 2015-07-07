package ar.wololo.pokemon.dominio

import Tipos._

abstract class Actividad
case class RealizarUnAtaque(val ataqueARealizar: Ataque) extends Actividad
case class LevantarPesas(val kg: Int) extends Actividad
case class Nadar(val minutos: Int) extends Actividad
case class AprenderAtaque(val ataqueAAprender: (Ataque,Int,Int)) extends Actividad
case class UsarPiedra(val piedra: Piedra) extends Actividad
object UsarPocion extends Actividad
object UsarAntidoto extends Actividad
object UsarEther extends Actividad
object ComerHierro extends Actividad
object ComerCalcio extends Actividad
object ComerZinc extends Actividad
object Descansar extends Actividad
object FingirIntercambio extends Actividad

/*
* Actividades como funciones.
* No es necesario crear nuevos objetos ni clases para agregar nuevas actividades. Aquellas que tenían
* atributos se modelaron como funciones aplicables parcialmente. 
*/

//object activity {
//
//  implicit class AtaqueSyntax(ataque: (Ataque, Int, Int)) {
//    def aumentarPAMax(i: Int) = (ataque._1, ataque._2, ataque._3 + i) // aumento paMax
//    def regenerar = (ataque._1, ataque._3, ataque._3) // paAct = paMAx
//    def sosIgualA(unAtaque: Ataque) = ataque._1.eq(unAtaque) && ataque._2 > 0
//    def teUtiliza(pokemon: Pokemon) = ataque._1.teUtiliza(pokemon)
//  }
//
//  val usarPocion: Actividad = _.modificaEnergia(50)
//  val usarEther: Actividad = _.cambiaAEstado(Bueno)
//  val comerHierro: Actividad = _.modificaFuerza(5)
//  val comerCalcio: Actividad = _.modificaVelocidad(5)
//  val fingirIntercambio: Actividad = _.teIntercambiaron
//  val usarPiedra: Piedra => Actividad = piedra => _.evaluarEfectos(piedra)
//
//  val comerZinc: Actividad = pokemon => {
//    val ataquesMejorados = pokemon.listaAtaques.map { _.aumentarPAMax(2) }
//    pokemon.modificaListaAtaque(ataquesMejorados)
//  }
//
//  val descansar: Actividad = pokemon => {
//    val ataquesRegenerados = pokemon.listaAtaques.map { _.regenerar }
//    val pokemonConAtaquesRegenerados = pokemon.modificaListaAtaque(ataquesRegenerados)
//
//    pokemonConAtaquesRegenerados.energia match {
//      case n if n < pokemonConAtaquesRegenerados.energiaMax / 2 => pokemonConAtaquesRegenerados.cambiaAEstado(Dormido(3))
//      case _ => pokemonConAtaquesRegenerados
//    }
//  }
//
//  val nadar: Integer => Actividad = minutos => pokemon => {
//    (pokemon.tipoPrincipal, pokemon.tipoSecundario) match {
//      case (tP, tS) if Agua.leGanasA(tP) || Agua.leGanasA(tS) => pokemon.cambiaAEstado(Ko)
//      case _ =>
//        val pokemonAfectado = if (pokemon.sosDeTipo(Agua)) pokemon.modificaVelocidad(minutos) else pokemon
//        pokemonAfectado.modificaEnergia(-minutos).aumentaExperiencia(200)
//    }
//  }
//
//  val aprenderAtaque: TernaAtaque => Actividad = ataque => pokemon => {
//    if (ataque._1.tePuedeAprender(pokemon))
//      pokemon.modificaListaAtaque(ataque :: pokemon.listaAtaques)
//    else
//      pokemon.cambiaAEstado(Ko)
//  }
//
//  val realizarAtaque: Ataque => Actividad = ataque => pokemon => {
//    pokemon.listaAtaques
//      .find { _.sosIgualA(ataque) }
//      .fold { throw PokemonNoConoceMovONoTienePA(pokemon) } { _.teUtiliza(pokemon) }
//  }
//
//  val usarAntidoto: Actividad = pokemon =>
//    pokemon.estado match {
//      case Envenenado => pokemon.cambiaAEstado(Bueno)
//      case _ => pokemon
//    }
//
//  val levantarPesas: Long => Actividad = kg => pokemon => pokemon.estado match {
//    case Paralizado => pokemon.cambiaAEstado(Ko)
//    case _: EstadoPokemon =>
//      if (pokemon.podesLevantar(kg)) {
//        if (pokemon.sosDeTipo(Pelea)) pokemon.aumentaExperiencia(kg * 2)
//        else if (pokemon.sosDeTipo(Fantasma)) throw FantasmaNoPuedeLevantarPesas(pokemon)
//        else pokemon.aumentaExperiencia(kg)
//      } else {
//        pokemon.cambiaAEstado(Paralizado)
//      }
//  }
//}