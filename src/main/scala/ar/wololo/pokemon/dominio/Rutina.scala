package ar.wololo.pokemon.dominio

import scala.util.{ Try, Success, Failure }

class Rutina(val actividades: List[Actividad]) {

  def esHechaPor(pokemon: Pokemon): Try[Pokemon] = {
    actividades.foldLeft(Success(pokemon): Try[Pokemon]) { (resultadoAnterior, actividad) =>
      resultadoAnterior.map(_.realizarActividad(actividad))
    }
  }

}