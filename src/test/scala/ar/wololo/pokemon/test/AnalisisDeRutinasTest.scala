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

class AnalisisDeRutinasTest extends FunSuite {
  
  def fixture = new {
    var pikachu = new Pokemon(Bueno, null, Electrico, null, 1, 100, Macho, 100, 100, 1000, 100, 100, SubirDeNivel(100), 0, 0)
    val rutinaNado = new Rutina(List[Actividad](Nadar(1), Nadar(1), Nadar(1), Nadar(1)))
    val rutinaPocionado = new Rutina(List[Actividad](UsarPocion, UsarPocion, UsarPocion, UsarPocion))
    val rutinaIntercambio = new Rutina(List[Actividad](FingirIntercambio, FingirIntercambio, FingirIntercambio))
  }

  test("Si a un pokemon sólo lo hago hacer una rutina, esa es la mejor, salvo que no pueda hacerla") {
    val pocionado = fixture.rutinaPocionado
    val mejorRutina = SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List(pocionado), MaxEnergia)
    assert(mejorRutina.get === pocionado)
    
    val pokeKo = fixture.pikachu.copy(estado = Ko)
    intercept[NoHuboRutinaHacibleException](SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(pokeKo, List[Rutina](fixture.rutinaPocionado), MaxEnergia))
  }

  test("Si pikachu es macho y su condicion evolutiva es SubirDeNivel y debe elegir entre rutinas de UsarPocion y FingirIntercambio, es mejor UsarPocion para aumentar energía") {
    val pocionado = fixture.rutinaPocionado
    val intercambiado = fixture.rutinaIntercambio
    val mejorRutina = SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List(pocionado, intercambiado), MinPeso)
    assert(mejorRutina.get == pocionado)
  }
  
  test("Si no paso rutinas sobre las cuales analizar la mejor, tira excepción") {
    intercept[NoHuboRutinaHacibleException](SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(fixture.pikachu, List[Rutina](), MaxEnergia))
  }

  test("Si pikachu está knockout no hay mejor rutina para el") {
    var pokeKo = fixture.pikachu.copy(estado = Ko)
    intercept[NoHuboRutinaHacibleException](SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(pokeKo, List[Rutina](fixture.rutinaNado, fixture.rutinaIntercambio, fixture.rutinaPocionado), MinPeso))
  }

}