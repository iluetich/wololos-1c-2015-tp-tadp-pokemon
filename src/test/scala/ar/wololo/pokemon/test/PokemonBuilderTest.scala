package ar.wololo.pokemon.test

import org.scalatest.FunSuite
import ar.wololo.pokemon.dominio._

/**
 * @author ivan
 */
class PokemonFactoryTest extends FunSuite {
  test("Si pido crear un pokemón sin parámetros necesarios tira excepción") {
    intercept[BuilderException](Fixt.fabricaDePokes.build)
  }
  test("No puedo armar un pokemón con experiencia desorbitante") {
    intercept[ExperienciaBuilderException](Fixt.fabricaDePokes.setEstado(Bueno).setEspecie(Fixt.especiePikachu).setNivel(1).setEnergia(55).setExperiencia(1000))
  }
  test("No puedo armar un pokemón con experiencia ínfima y nivel alto") {
    intercept[ExperienciaBuilderException](Fixt.fabricaDePokes.setEspecie(Fixt.especiePikachu).setNivel(10).setExperiencia(0).isInstanceOf[PokemonBuilder])
  }
  test("No puedo armar un pokemón con ataques que no pueda aprender") {
    intercept[AtaqueBuilderException](Fixt.fabricaDePokes.setEspecie(Fixt.especieHitmonchan).setAtaques(List(Fixt.embestida, Fixt.impactrueno)))
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
  test("armo un pokemon con ataque impactrueno y tiene que tener los mismo pa que el impactrueno del fixture"){
    assert(Fixt.impactrueno._2 == 1)
    
    val fabricaDePokes = new PokemonBuilder
    val especiePokemon = Fixt.especiePikachu

    val pikachu = fabricaDePokes.setEstado(Bueno)
    .setEspecie(Fixt.especiePikachu)
    .setAtaques(List(Fixt.impactrueno, Fixt.embestida))
    .setNivel(1)
    .setExperiencia(0)
    .setGenero(Macho)
    .setEnergia(30)
    .build
   
   val impactrueno = pikachu.listaAtaques.find { ataque => ataque._1 == Fixt.impactrueno._1}
    impactrueno match {
      case Some(impactrueno) => assert(impactrueno._2 == 1)
      case None => assert(false)
    }      
  }
}
