package ar.wololo.pokemon.test

import org.scalatest.FunSuite
import ar.wololo.pokemon.dominio.Fuego
import ar.wololo.pokemon.dominio.Electrico
import ar.wololo.pokemon.dominio.Especie
import ar.wololo.pokemon.dominio.EspecieCreacionBuilderException

/**
 * @author ivan
 */
class EspecieFactoryTest extends FunSuite {
  test("No puedo crear una especie vacía") {
    intercept[EspecieCreacionBuilderException](Fixt.fabricaDeEspecies.build)
  }
  test("No puedo crear una especie sin tipos") {
    intercept[EspecieCreacionBuilderException](Fixt.fabricaDeEspecies.setIncrementos(1, 1, 1, 1).setPesoMaximoSaludable(1).setResistenciaEvolutiva(1).build)
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