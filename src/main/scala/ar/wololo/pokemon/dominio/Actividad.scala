package ar.wololo.pokemon.dominio

import Tipos._

/*
* Actividades como funciones.
* No es necesario crear nuevos objetos ni clases para agregar nuevas actividades. Aquellas que tenían
* atributos se modelaron como funciones aplicables parcialmente. 
*/

object activity {

  implicit class aumentosPA(ataque: (Ataque, Int, Int)) {
    def aumentarPAMax(i: Int) = (ataque._1, ataque._2, ataque._3 + i) // aumento paMax
    def regenerar = (ataque._1, ataque._3, ataque._3) // paAct = paMAx
    def sosIgualA(unAtaque: Ataque) = ataque._1.eq(unAtaque) && ataque._2 > 0
    def teUtiliza(pokemon: Pokemon) = ataque._1.teUtiliza(pokemon)
  }

  val usarPocion: Actividad = _.modificaEnergia(50)
  val usarEther: Actividad = _.cambiaAEstado(Bueno)
  val comerHierro: Actividad = _.modificaFuerza(5)
  val comerCalcio: Actividad = _.modificaVelocidad(5)
  val fingirIntercambio: Actividad = _.teIntercambiaron
  val usarPiedra: Piedra => Actividad = piedra => _.evaluarEfectos(piedra)

  val comerZinc: Actividad = pokemon => {
    val ataquesMejorados = pokemon.listaAtaques.map { case (atk, pAAct, pAMax) => (atk, pAAct, pAMax + 2) }
    pokemon.modificaListaAtaque(ataquesMejorados)
  }

  val descansar: Actividad = pokemon => {
    val ataquesRegenerados = pokemon.listaAtaques.map { case (atk, _, pAMax) => (atk, pAMax, pAMax) }
    val pokemonConAtaquesRegenerados = pokemon.modificaListaAtaque(ataquesRegenerados)

    pokemonConAtaquesRegenerados.energia match {
      case n if n < pokemonConAtaquesRegenerados.energiaMax / 2 => pokemonConAtaquesRegenerados.cambiaAEstado(Dormido(3))
      case _ => pokemonConAtaquesRegenerados
    }
  }

  val nadar: Integer => Actividad = minutos => pokemon => {
    (pokemon.tipoPrincipal, pokemon.tipoSecundario) match {
      case (tP, tS) if Agua.leGanasA(tP) || Agua.leGanasA(tS) => pokemon.cambiaAEstado(Ko)
      case (tP, tS) =>
        val pokemonAfectado = (tP, tS) match {
          case (Agua, _) | (_, Agua) => pokemon.modificaVelocidad(minutos)
          case _ => pokemon
        }
        pokemonAfectado.modificaEnergia(-minutos).aumentaExperiencia(200)
    }
  }

  val aprenderAtaque: TernaAtaque => Actividad = ataque => pokemon => {
    if (ataque._1.tePuedeAprender(pokemon))
      pokemon.modificaListaAtaque(ataque :: pokemon.listaAtaques)
    else
      pokemon.cambiaAEstado(Ko)
  }

  val realizarAtaque: Ataque => Actividad = ataque => pokemon => {
    pokemon.listaAtaques
      .find { _.sosIgualA(ataque) }
      .fold { throw PokemonNoConoceMovONoTienePA(pokemon) } { _.teUtiliza(pokemon) }
  }

  val usarAntidoto: Actividad = pokemon =>
    pokemon.estado match {
      case Envenenado => pokemon.cambiaAEstado(Bueno)
      case _ => pokemon
    }

  val levantarPesas: Long => Actividad = kg => pokemon => pokemon.estado match {
    case Paralizado => pokemon.cambiaAEstado(Ko)
    case _: EstadoPokemon =>
      if (pokemon.podesLevantar(kg)) {
        (pokemon.tipoPrincipal, pokemon.tipoSecundario) match {
          case (Pelea, _) | (_, Pelea) => pokemon.aumentaExperiencia(kg * 2)
          case (Fantasma, _) | (_, Fantasma) => throw FantasmaNoPuedeLevantarPesas(pokemon)
          case _ => pokemon.aumentaExperiencia(kg)
        }
      } else {
        pokemon.cambiaAEstado(Paralizado)
      }
  }
}