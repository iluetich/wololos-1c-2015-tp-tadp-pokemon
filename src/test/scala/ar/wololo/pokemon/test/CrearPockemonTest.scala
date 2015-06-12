package ar.wololo.pokemon.test

import ar.wololo.pokemon.dominio.Pokemon
import ar.wololo.pokemon.dominio.Dormido
import ar.wololo.pokemon.dominio.Ataque
import ar.wololo.pokemon.dominio.Fuego
import ar.wololo.pokemon.dominio.Agua
import ar.wololo.pokemon.dominio.Macho
import ar.wololo.pokemon.dominio.SubirDeNivel
import org.scalatest.FunSuite
import org.scalatest.FlatSpec

class SetSpec extends FlatSpec {
  "Un pichachu gue gana diferentes cantidades de experiencia" should "debe aumentar la experiencia, el nivel y la experienciaDeSaltoNivel" in {
    val picachu = new Pokemon( estado = Dormido(3), listaAtaques = List[Ataque](), objetoPrincipal = Fuego,
        objetoSecundario = Agua, nivel = 1, experiencia = 0, genero = Macho, energia = 30, energiaMax = 1000,
        peso = 5, fuerza = 100, velocidad = 20, condicionEvolutiva = SubirDeNivel, experienciaSaltoNivel = 3,
        resistenciaEvolutiva = 3)
    val picachuGana2Exp = new Pokemon( estado = Dormido(3), listaAtaques = List[Ataque](), objetoPrincipal = Fuego,
        objetoSecundario = Agua, nivel = 1, experiencia = 0 + 2, genero = Macho, energia = 30,
        energiaMax = 1000, peso = 5, fuerza = 100, velocidad = 20, condicionEvolutiva = SubirDeNivel,
        experienciaSaltoNivel = 3, resistenciaEvolutiva = 3)
    val picachuGana10Exp = new Pokemon( estado = Dormido(3), listaAtaques = List[Ataque](), objetoPrincipal = Fuego,
        objetoSecundario = Agua, nivel = 4, experiencia = 0 + 10, genero = Macho, energia = 30,
        energiaMax = 1000, peso = 5, fuerza = 100, velocidad = 20, condicionEvolutiva = SubirDeNivel,
        experienciaSaltoNivel = 12, resistenciaEvolutiva = 3)
    assert( picachu.ganarExperiencia(2).equals(picachuGana2Exp) )
    System.out.println("nivel: " + picachu.ganarExperiencia(10).nivel)
    assert( picachu.ganarExperiencia(10).equals(picachuGana10Exp) )
    
  }
  
  
  
}
