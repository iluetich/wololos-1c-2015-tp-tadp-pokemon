package ar.wololo.pokemon

import ar.wololo.pokemon.dominio.CriterioRutina
import ar.wololo.pokemon.dominio.Pokemon
import ar.wololo.pokemon.dominio.Actividad
import ar.wololo.pokemon.dominio.MayorNivelPosible
import ar.wololo.pokemon.dominio.MayorEnergiaPosible
import ar.wololo.pokemon.dominio.MenorPesoPosible
import scala.util.Try
import ar.wololo.pokemon.dominio.Rutina

package object pokemon {

//  private def mejorRutinaSegun(pokemon: Pokemon,
//    primerRutina: Rutina,
//    segundaRutina: Rutina,
//    criterio: CriterioRutina): Try[Rutina] = {
//    
//    criterio match {
//      case MayorNivelPosible => {
//        val estado_1 = pokemon.realizarRutina(primerRutina)
//        val estado_2 = pokemon.realizarRutina(segundaRutina)
//        for {
//          poke <- estado_1
//          otroPoke <- estado_2
//        } yield if (poke.nivel > otroPoke.nivel)
//          primerRutina
//        else
//          segundaRutina
//      }
//    }
//  }

  def mejorRutinaSegun(pokemon: Pokemon,
    rutinas: Seq[Rutina],
    criterio: CriterioRutina): Try[Rutina] = {
    criterio match {
      case MayorNivelPosible => null
      case MayorEnergiaPosible => null
      case MenorPesoPosible => null
    }
  }

}