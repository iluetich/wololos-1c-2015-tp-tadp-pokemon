package ar.wololo.pockemon.test

import ar.wololo.pockemon.dominio.Pokemon
import ar.wololo.pockemon.dominio.dormido
import ar.wololo.pockemon.dominio.Ataque
import ar.wololo.pockemon.dominio.Fuego
import ar.wololo.pockemon.dominio.Agua
import ar.wololo.pockemon.dominio.Macho
import ar.wololo.pockemon.dominio.SubirDeNivel
import org.scalatest.FunSuite

class CrearPockemonTest extends FunSuite {
  
  test("Se crea un Pokemon") {
    var picachu = new Pokemon( dormido, List[Ataque](), Fuego, Agua,
                20, 30, Macho, 30, 1000, 5, 100, 20, SubirDeNivel)
    assert(picachu.isInstanceOf[Pokemon])
  }
}