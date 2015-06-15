package ar.wololo.pokemon.test

import org.scalatest.FunSuite
import ar.wololo.pokemon.dominio.Pokemon
import ar.wololo.pokemon.dominio.Macho
import ar.wololo.pokemon.dominio.Electrico
import ar.wololo.pokemon.dominio.SubirDeNivel
import ar.wololo.pokemon.dominio.Rutina
import ar.wololo.pokemon.dominio.Nadar
import ar.wololo.pokemon.dominio.Actividad
import ar.wololo.pokemon.dominio.SuperSistemaDeAnalisis
import ar.wololo.pokemon.dominio.FingirIntercambio
import ar.wololo.pokemon.dominio.Ko
import ar.wololo.pokemon.dominio.SuperSistemaDeAnalisis.NoHuboRutinaHacibleException
import ar.wololo.pokemon.dominio.MinPeso
import ar.wololo.pokemon.dominio.SuperSistemaDeAnalisis.NoHuboRutinaHacibleException
import ar.wololo.pokemon.dominio.MaxEnergia
import ar.wololo.pokemon.dominio.Bueno
import ar.wololo.pokemon.dominio.SuperSistemaDeAnalisis.NoHuboRutinaHacibleException
import ar.wololo.pokemon.dominio.UsarPocion
import ar.wololo.pokemon.dominio.MaxNivel
import ar.wololo.pokemon.dominio.SuperSistemaDeAnalisis.NoHuboRutinaHacibleException

class AnalisisDeRutinasTest extends FunSuite {
  
  def fixture = Fixt

  test("Si a un pokemon sólo lo hago hacer una rutina, esa es la mejor, salvo que no pueda hacerla") {
    val pocionado = fixture.rutinaPocionado
    val mejorRutina = SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List(pocionado), MaxEnergia)
    assert(mejorRutina === pocionado.nombre)
    
    val pokeKo = fixture.pikachu.copy(estado = Ko)
    intercept[NoHuboRutinaHacibleException](SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(pokeKo, List[Rutina](fixture.rutinaPocionado), MaxEnergia))
  }

  test("Si pikachu es macho y su condicion evolutiva es SubirDeNivel le conviene la rutina de pocionado para aumentar energía") {
    val pocionado = fixture.rutinaPocionado
    val intercambiado = fixture.rutinaIntercambio
    val mejorRutina = SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List(pocionado, intercambiado), MinPeso)
    assert(mejorRutina === pocionado.nombre)
  }
  
  test("Si no paso rutinas el análisis arroja excepción para cualquier criterio") {
    intercept[NoHuboRutinaHacibleException](SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List[Rutina](), MaxEnergia))
    intercept[NoHuboRutinaHacibleException](SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List[Rutina](), MaxNivel))
    intercept[NoHuboRutinaHacibleException](SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List[Rutina](), MinPeso))
  }

  test("Si pikachu está knockout el análisis arroja una excepcion") {
    val pokeKo = fixture.pikachu.copy(estado = Ko)
    intercept[NoHuboRutinaHacibleException](SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(pokeKo, List[Rutina](fixture.rutinaNado, fixture.rutinaIntercambio, fixture.rutinaPocionado), MinPeso))
  }
  
  test("Conviene más nadar lo máximo posible para subir de nivel que usar pociones") {
    val pocionado = fixture.rutinaPocionado
    val nado = fixture.rutinaNado
    val mejorRutina = SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List(pocionado, nado), MaxNivel)
    assert(mejorRutina === nado.nombre)
  }
  
  test("Que el pokemon no pueda realizar una rutina no impide que realice otras") {
    val pocionado = fixture.rutinaPocionado
    val nado = fixture.rutinaNado
    val nadoQueNoPuedeHacer = fixture.rutinaPhelps
    intercept[NoHuboRutinaHacibleException](SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List(nadoQueNoPuedeHacer), MaxNivel))
   
    val mejorRutina = SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List(nadoQueNoPuedeHacer, pocionado, nado), MaxNivel)
    assert(mejorRutina === nado.nombre)
  }
  

}