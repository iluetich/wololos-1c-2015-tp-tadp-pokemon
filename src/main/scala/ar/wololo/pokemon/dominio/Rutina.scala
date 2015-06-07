package ar.wololo.pokemon.dominio
import scala.util.{Try,Success,Failure}

class Rutina(val actividades:List[Actividad]) {  
  
  def esHechaPor(pokemon:Pokemon): Try[Pokemon] = {
    actividades.foldLeft(Success(pokemon):Try[Pokemon]) {
      (semilla,actividad) => semilla.flatMap(_.hacerActividad(actividad))
    }
  }
   
}