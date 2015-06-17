package ar.wololo.pokemon.test

import org.scalatest.FunSuite
import ar.wololo.pokemon.dominio._

/**
 * @author ivan
 */
class PokemonFactoryTest extends FunSuite {
  test("Si pido crear un pokemón sin parámetros necesarios tira excepción") {
    intercept[BuildFactoryException](Fixt.fabricaDePokes.build)
  }
  test("No puedo armar un pokemón con experiencia desorbitante") {
    intercept[ExperienciaFactoryException](Fixt.fabricaDePokes.setEstado(Bueno).setEspecie(Fixt.especiePikachu).setNivel(1).setEnergia(55).setExperiencia(1000))
  }
  test("No puedo armar un pokemón con experiencia ínfima y nivel alto") {
    intercept[ExperienciaFactoryException](Fixt.fabricaDePokes.setEspecie(Fixt.especiePikachu).setNivel(10).setExperiencia(0).isInstanceOf[PokemonFactory])
  }
  test("No puedo armar un pokemón con ataques que no pueda aprender") {
    intercept[AtaqueFactoryException](Fixt.fabricaDePokes.setEspecie(Fixt.especieHitmonchan).setAtaques(List(Fixt.embestida, Fixt.impactrueno)))
  }
  test("Si armo un poke con criterio, la fábrica me lo da") {
    val pokeBien = Fixt.fabricaDePokes.setEstado(Bueno)
      .setGenero(Macho)
      .setEspecie(Fixt.especiePikachu)
      .setNivel(2)
      .setEnergia(100)
      .setExperiencia(250)
      .build
    assert(pokeBien.isInstanceOf[Pokemon])
  }
}