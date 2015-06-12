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

  test("Si a un pokemon sólo lo hago hacer una rutina, esa es la mejor, salvo que no pueda hacerla") {
    var pikachu = new Pokemon(Bueno, null, Electrico, null, 1, 100, Macho, 100, 100, 1000, 100, 100, SubirDeNivel(100), 0, 0)
    var rutinaPocionado = new Rutina(List[Actividad](UsarPocion, UsarPocion, UsarPocion, UsarPocion))
    var rutinaIntercambio = new Rutina(List[Actividad](FingirIntercambio, FingirIntercambio, FingirIntercambio))
    var mejorRutina = SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(pikachu, List[Rutina](rutinaPocionado), MinPeso)

    assert(mejorRutina.get === rutinaPocionado)
    
    pikachu = pikachu.copy(estado = Ko)
    
    intercept[NoHuboRutinaHacibleException](SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(pikachu, List[Rutina](rutinaPocionado), MinPeso))
  }

  test("Si pikachu es macho y su condicion evolutiva es SubirDeNivel y debe elegir entre rutinas de UsarPocion y FingirIntercambio, es mejor UsarPocion para aumentar energía") {
    var pikachu = new Pokemon(Bueno, null, Electrico, null, 1, 100, Macho, 50, 300, 1000, 100, 100, SubirDeNivel(100), 0, 0)
    var rutinaPocionado = new Rutina(List[Actividad](UsarPocion, UsarPocion, UsarPocion, UsarPocion))
    var rutinaIntercambio = new Rutina(List[Actividad](FingirIntercambio, FingirIntercambio, FingirIntercambio))
    var mejorRutina = SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(pikachu, List[Rutina](rutinaIntercambio, rutinaPocionado), MaxEnergia)

    assert(mejorRutina.get === rutinaPocionado)
  }
  
  test("Si no paso rutinas sobre las cuales analizar la mejor, tira excepción") {
    var pikachu = new Pokemon(Ko, null, Electrico, null, 1, 100, Macho, 100, 100, 1000, 100, 100, SubirDeNivel(100), 0, 0)
    
    intercept[NoHuboRutinaHacibleException](SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(pikachu, List[Rutina](), MaxEnergia))
  }

  test("Si pikachu está knockout no hay mejor rutina para el") {
    var pikachu = new Pokemon(Ko, null, Electrico, null, 1, 100, Macho, 100, 100, 1000, 100, 100, SubirDeNivel(100), 0, 0)
    var rutinaNado = new Rutina(List[Actividad](Nadar(1), Nadar(1), Nadar(1), Nadar(1)))
    var rutinaIntercambio = new Rutina(List[Actividad](FingirIntercambio, FingirIntercambio, FingirIntercambio))
    var rutinaPocionado = new Rutina(List[Actividad](UsarPocion, UsarPocion, UsarPocion, UsarPocion))
    
    intercept[NoHuboRutinaHacibleException](SuperSistemaDeAnalisis.obtenerMejorRutinaSegun(pikachu, List[Rutina](rutinaNado, rutinaIntercambio, rutinaPocionado), MinPeso))
  }

}