package ar.wololo.pokemon.dominio

/*
* Actividades como funciones.
* No es necesario crear nuevos objetos ni clases para agregar nuevas actividades. Aquellas que tenían
* atributos se modelaron como funciones aplicables parcialmente. 
*/

object activity {

  type TernaAtaque = (Ataque, Int, Int)
  type Actividad = Pokemon => Pokemon

  val usarPocion: Actividad = _.modificaEnergia(50)
  val usarEther: Actividad = _.cambiaAEstado(Bueno)
  val comerHierro: Actividad = _.modificaFuerza(5)
  val comerCalcio: Actividad = _.modificaVelocidad(5)
  val comerZinc: Actividad = _.aumentaPAMaximo(2)
  val descansar: Actividad = _.descansar
  val fingirIntercambio: Actividad = pokemon => pokemon.teIntercambiaron
  val nadar: Integer => Actividad = minutos => _.nadar(minutos)
  val usarPiedra: Piedra => Actividad = piedra => _.evaluarEfectos(piedra)
  val aprenderAtaque: TernaAtaque => Actividad = ataque => _.aprenderAtaque(ataque)
  val realizarAtaque: Ataque => Actividad = ataque => _.realizarAtaque(ataque)
  val usarAntidoto: Actividad = pokemon =>
    pokemon.estado match {
      case Envenenado => pokemon.cambiaAEstado(Bueno)
      case _ => pokemon
    }
  val levantarPesas: Integer => Actividad = kg => pokemon => pokemon.estado match {
    case Paralizado => pokemon.cambiaAEstado(Ko)
    case _ => pokemon.levantaSiPodes(kg)
  }
}