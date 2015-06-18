package ar.wololo.pokemon.test

import org.scalatest.FunSuite
import ar.wololo.pokemon.dominio._

class AnalisisDeRutinasTest extends FunSuite {
  
  def fixture = Fixt

  test("Si a un pokemon sólo lo hago hacer una rutina, esa es la mejor, salvo que no pueda hacerla") {
    val pocionado = fixture.rutinaPocionado
    val mejorRutina = SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List(pocionado), MaxEnergia)
    assert(mejorRutina === Some(pocionado.nombre))
    print("JAJAJAJ")
    val pokeKo = fixture.pikachu.copy(estado = Ko)
    assert(SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(pokeKo, List[Rutina](fixture.rutinaPocionado), MaxEnergia) === None)
  }

  test("Si pikachu es macho y su condicion evolutiva es SubirDeNivel le conviene la rutina de pocionado para aumentar energía") {
    val pocionado = fixture.rutinaPocionado
    val intercambiado = fixture.rutinaIntercambio
    val mejorRutina = SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List(pocionado, intercambiado), MinPeso)
    assert(mejorRutina === Some(pocionado.nombre))
  }
  
  test("Si no paso rutinas el análisis arroja excepción para cualquier criterio") {
    assert(SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List[Rutina](), MaxEnergia) === None)
    assert(SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List[Rutina](), MaxNivel) === None)
    assert(SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List[Rutina](), MinPeso) === None)
  }

  test("Si pikachu está knockout el análisis no retorna ningún nombre") {
    val pokeKo = fixture.pikachu.copy(estado = Ko)
    assert(SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(pokeKo, List[Rutina](fixture.rutinaNado, fixture.rutinaIntercambio, fixture.rutinaPocionado), MinPeso) === None)
  }
  
  test("Conviene más nadar lo máximo posible para subir de nivel que usar pociones") {
    val pocionado = fixture.rutinaPocionado
    val nado = fixture.rutinaNado
    val mejorRutina = SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List(pocionado, nado), MaxNivel)
    assert(mejorRutina === Some(nado.nombre))
  }
  
  test("Que el pokemon no pueda realizar una rutina no impide que realice otras") {
    val pocionado = fixture.rutinaPocionado
    val nado = fixture.rutinaNado
    val nadoQueNoPuedeHacer = fixture.rutinaPhelps
    assert(SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List(nadoQueNoPuedeHacer), MaxNivel) === None)
   
    val mejorRutina = SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List(nadoQueNoPuedeHacer, pocionado, nado), MaxNivel)
    assert(mejorRutina === Some(nado.nombre))
  }
  

}