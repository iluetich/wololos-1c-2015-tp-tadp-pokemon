package ar.wololo.pokemon.test

import ar.wololo.pokemon.dominio.Pokemon
<<<<<<< HEAD
=======
import ar.wololo.pokemon.dominio.Dormido
>>>>>>> refs/remotes/origin/actividades
import ar.wololo.pokemon.dominio.Ataque
import ar.wololo.pokemon.dominio.Fuego
import ar.wololo.pokemon.dominio.Agua
import ar.wololo.pokemon.dominio.Macho
import ar.wololo.pokemon.dominio.SubirDeNivel
import org.scalatest.FunSuite
import ar.wololo.pokemon.dominio.Dormido

class CrearPockemonTest extends FunSuite {

  test("Se crea un Pokemon") {
    var picachu = new Pokemon(Dormido, List[Ataque](), Fuego, Agua,
      20, 30, Macho, 30, 1000, 5, 100, 20, SubirDeNivel)
    assert(picachu.isInstanceOf[Pokemon] === true)
  }
}
