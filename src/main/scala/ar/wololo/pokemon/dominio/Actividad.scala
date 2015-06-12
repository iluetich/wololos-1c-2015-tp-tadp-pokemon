package ar.wololo.pokemon.dominio

abstract class Actividad
case class RealizarUnAtaque(val ataqueARealizar: Ataque) extends Actividad
case class LevantarPesas(val kg: Int) extends Actividad
case class Nadar(val minutos: Int) extends Actividad
case class AprenderAtaque(val ataqueAAprender: Ataque) extends Actividad
case class UsarPiedra(val piedra: Piedra) extends Actividad
object UsarPocion extends Actividad
object UsarAntidoto extends Actividad
object UsarEther extends Actividad
object ComerHierro extends Actividad
object ComerCalcio extends Actividad
object ComerZinc extends Actividad
object Descansar extends Actividad
object FingirIntercambio extends Actividad