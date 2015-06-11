package ar.wololo.pokemon.test

import ar.wololo.pokemon.dominio.Pokemon
import ar.wololo.pokemon.dominio.Dormido
import ar.wololo.pokemon.dominio.Ataque
import ar.wololo.pokemon.dominio.Fuego
import ar.wololo.pokemon.dominio.Agua
import ar.wololo.pokemon.dominio.Macho
import ar.wololo.pokemon.dominio.SubirDeNivel
import org.scalatest.FunSuite

class CrearPockemonTest extends FunSuite {

  test("Se crea un Pokemon") {
    var picachu = new Pokemon(Dormido(3), List[Ataque](), Fuego, Agua,
      20, 30, Macho, 30, 1000, 5, 100, 20, SubirDeNivel)
    assert(picachu.isInstanceOf[Pokemon] === true)
  }
}