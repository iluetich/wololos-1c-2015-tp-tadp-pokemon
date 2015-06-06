package ar.wololo.pockemon.test

import org.scalatest.FlatSpec
import ar.wololo.pockemon.dominio.Pokemon

class CrearPockemonTest extends FlatSpec {
  "A Pockemon" should "create" in {
    val picachu = new Pokemon
    assert(picachu.isInstanceOf[Pokemon])
  }
}