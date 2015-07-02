package ar.wololo.pokemon.test

import org.scalatest.FunSuite
import ar.wololo.pokemon.dominio._
import Tipos._

class AnalisisDeRutinasTest extends FunSuite {

  def fixture = Fixt
  def obtenerMejorRutinaSegun(rutinas: Seq[Rutina])(criterio: Criterio) = SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu)(rutinas)(criterio)

  test("Si a un pokemon sólo lo hago hacer una rutina, y la puede hacer, esa es la mejor") {
    val pocionado = fixture.rutinaPocionado
    val mejorRutina = obtenerMejorRutinaSegun(List(pocionado))(criteriosDeAnalisis.maxEnergia)
    assert(mejorRutina === Some(pocionado.nombre))
  }

  test("Si pikachu es macho y su condicion evolutiva es SubirDeNivel le conviene la rutina de pocionado para aumentar energía") {
    val pocionado = fixture.rutinaPocionado
    val intercambiado = fixture.rutinaIntercambio
    val mejorRutina = obtenerMejorRutinaSegun(List(pocionado, intercambiado))(criteriosDeAnalisis.minPeso)
    assert(mejorRutina === Some(pocionado.nombre))
  }

  test("Si no paso rutinas el análisis arroja excepción para cualquier criterio") {
    def analisisAux(criterio: Criterio) = obtenerMejorRutinaSegun(List[Rutina]())(criterio)

    assert(analisisAux(criteriosDeAnalisis.maxEnergia).isEmpty)
    assert(analisisAux(criteriosDeAnalisis.maxNivel).isEmpty)
    assert(analisisAux(criteriosDeAnalisis.minPeso).isEmpty)
  }

  test("Si pikachu está knockout el análisis no retorna ningún nombre") {
    val pokeKo = fixture.pikachu.copy(estado = Ko)
    assert(SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(pokeKo)(List[Rutina](fixture.rutinaNado, fixture.rutinaIntercambio, fixture.rutinaPocionado))(criteriosDeAnalisis.minPeso) === None)
  }

  test("Conviene más nadar lo máximo posible para subir de nivel que usar pociones") {
    val pocionado = fixture.rutinaPocionado
    val nado = fixture.rutinaNado
    val mejorRutina = obtenerMejorRutinaSegun(List(pocionado, nado))(criteriosDeAnalisis.maxNivel)
    assert(mejorRutina === Some(nado.nombre))
  }

  test("Que el pokemon no pueda realizar una rutina no impide que realice otras") {
    val pocionado = fixture.rutinaPocionado
    val nado = fixture.rutinaNado
    val nadoQueNoPuedeHacer = fixture.rutinaPhelps
    assert(obtenerMejorRutinaSegun(List(nadoQueNoPuedeHacer))(criteriosDeAnalisis.maxNivel).isEmpty)

    val mejorRutina = obtenerMejorRutinaSegun(List(nadoQueNoPuedeHacer, pocionado, nado))(criteriosDeAnalisis.maxNivel)
    assert(mejorRutina === Some(nado.nombre))
  }
}
