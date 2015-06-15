package ar.wololo.pokemon.test

import org.scalatest.FunSuite
import ar.wololo.pokemon.dominio._

/**
 * @author ivan
 */
class PokemonFactoryTest extends FunSuite {
  test("Si pido crear un pokemón sin parámetros necesarios tira excepción") {
    val fabricaDePokes = new PokemonFactory
    intercept[BuildFactoryException](fabricaDePokes.build)
  }
  test("No puedo armar un pokemón con experiencia desorbitante") {
    val fabricaDePokes = new PokemonFactory
    val especiePoke = new Especie(Fuego, Veneno, 1, 1, 1, 1, 100, 25, null, null)
    intercept[ExperienciaFactoryException](fabricaDePokes.setEspecie(especiePoke).setEnergiaMax(100).setEnergia(100).setNivel(1).setExperiencia(1000))
  }
  test("No puedo armar un pokemón con experiencia ínfima y nivel alto") {
    val fabricaDePokes = new PokemonFactory
    val especiePoke = new Especie(Fuego, Veneno, 1, 1, 1, 1, 100, 25, null, null)
    intercept[ExperienciaFactoryException](fabricaDePokes.setEspecie(especiePoke).setNivel(10).setExperiencia(0).isInstanceOf[PokemonFactory])
  }
  test("No puedo armar un pokemón con ataques que no pueda aprender") {
    val fabricaDePokes = new PokemonFactory
    val especiePoke = new Especie(Fuego, Veneno, 1, 1, 1, 1, 100, 200, null, null)
    intercept[AtaqueFactoryException](fabricaDePokes.setEspecie(especiePoke).setAtaques(List(Fixt.embestida, Fixt.impactrueno)))
  }
  test("Si armo un poke con criterio, la fábrica me lo da") {
    val fabricaDePokes = new PokemonFactory
    val especiePoke = new Especie(Electrico, Normal, 1, 1, 1, 1, 100, 200, null, null)
    val pokeBien = fabricaDePokes.setEstado(Bueno)
      .setEnergiaMax(100)
      .setEnergia(100)
      .setFuerza(90)
      .setGenero(Macho)
      .setEspecie(especiePoke)
      .setPeso(30)
      .setNivel(2)
      .setExperiencia(250)
      .setVelocidad(76)
      .build
    assert(pokeBien.isInstanceOf[Pokemon])
  }
}