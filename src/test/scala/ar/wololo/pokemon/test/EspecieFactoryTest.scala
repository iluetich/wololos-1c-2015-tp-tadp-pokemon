package ar.wololo.pokemon.test

import org.scalatest.FunSuite
import ar.wololo.pokemon.dominio.EspecieCreacionFactoryException
import ar.wololo.pokemon.dominio.EspecieCreacionFactoryException
import ar.wololo.pokemon.dominio.Fuego
import ar.wololo.pokemon.dominio.Electrico
import ar.wololo.pokemon.dominio.Especie

/**
 * @author ivan
 */
class EspecieFactoryTest extends FunSuite {
  test("No puedo crear una especie vacía") {
    intercept[EspecieCreacionFactoryException](Fixt.fabricaDeEspecies.build)
  }
  test("No puedo crear una especie sin tipos") {
    intercept[EspecieCreacionFactoryException](Fixt.fabricaDeEspecies.setIncrementos(1, 1, 1, 1).setPesoMaximoSaludable(1).setResistenciaEvolutiva(1).build)
  }
  test("Puedo crear una especie") {
    val especie = Fixt.fabricaDeEspecies
      .setTipos(Fuego, Electrico)
      .setIncrementos(1, 1, 1, 1)
      .setPesoMaximoSaludable(1)
      .setResistenciaEvolutiva(1)
      .build
    assert(especie.isInstanceOf[Especie])

  }
}