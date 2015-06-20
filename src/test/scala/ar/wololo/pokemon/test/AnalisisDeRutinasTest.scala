package ar.wololo.pokemon.test

import org.scalatest.FunSuite
import ar.wololo.pokemon.dominio._

class AnalisisDeRutinasTest extends FunSuite {

  def fixture = Fixt
  def obtenerMejorRutinaSegun(rutinas: Seq[Rutina])(criterio: CriterioRutina) = SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu)(rutinas)(criterio)

  test("Si a un pokemon sólo lo hago hacer una rutina, y la puede hacer, esa es la mejor") {
    val pocionado = fixture.rutinaPocionado
    val mejorRutina = obtenerMejorRutinaSegun(List(pocionado))(MaxEnergia)
    assert(mejorRutina === Some(pocionado.nombre))
  }

  test("Si pikachu es macho y su condicion evolutiva es SubirDeNivel le conviene la rutina de pocionado para aumentar energía") {
    val pocionado = fixture.rutinaPocionado
    val intercambiado = fixture.rutinaIntercambio
    val mejorRutina = obtenerMejorRutinaSegun(List(pocionado, intercambiado))(MinPeso)
    assert(mejorRutina === Some(pocionado.nombre))
  }

  test("Si no paso rutinas el análisis arroja excepción para cualquier criterio") {
    def analisisAux(criterio: CriterioRutina) = obtenerMejorRutinaSegun(List[Rutina]())(criterio)
    
    assert(analisisAux(MaxEnergia).isEmpty)
    assert(analisisAux(MaxNivel).isEmpty)
    assert(analisisAux(MinPeso).isEmpty)
  }

  test("Si pikachu está knockout el análisis no retorna ningún nombre") {
    val pokeKo = fixture.pikachu.copy(estado = Ko)
    assert(SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(pokeKo)(List[Rutina](fixture.rutinaNado, fixture.rutinaIntercambio, fixture.rutinaPocionado))(MinPeso) === None)
  }

  test("Conviene más nadar lo máximo posible para subir de nivel que usar pociones") {
    val pocionado = fixture.rutinaPocionado
    val nado = fixture.rutinaNado
    val mejorRutina = obtenerMejorRutinaSegun(List(pocionado, nado))(MaxNivel)
    assert(mejorRutina === Some(nado.nombre))
  }

  test("Que el pokemon no pueda realizar una rutina no impide que realice otras") {
    val pocionado = fixture.rutinaPocionado
    val nado = fixture.rutinaNado
    val nadoQueNoPuedeHacer = fixture.rutinaPhelps
    assert(obtenerMejorRutinaSegun(List(nadoQueNoPuedeHacer))(MaxNivel).isEmpty)

    val mejorRutina = obtenerMejorRutinaSegun(List(nadoQueNoPuedeHacer, pocionado, nado))(MaxNivel)
    assert(mejorRutina === Some(nado.nombre))
  }

}
