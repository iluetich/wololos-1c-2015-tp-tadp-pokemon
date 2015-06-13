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
    val picachu = new Pokemon( estado = Dormido(3), listaAtaques = List[Ataque](), tipoPrincipal = Fuego,
        tipoSecundario = Agua, nivel = 1, experiencia = 0, genero = Macho, energia = 30, energiaMax = 1000,
        peso = 5, fuerza = 100, velocidad = 20, condicionEvolutiva = SubirDeNivel(100), resistenciaEvolutiva = 3, null)
//    val picachuGana2Exp = new Pokemon( estado = Dormido(3), listaAtaques = List[Ataque](), tipoPrincipal = Fuego,
//        tipoSecundario = Agua, nivel = 1, experiencia = 0 + 2, genero = Macho, energia = 30,
//        energiaMax = 1000, peso = 5, fuerza = 100, velocidad = 20, condicionEvolutiva = SubirDeNivel(100), resistenciaEvolutiva = 3, null)
//    val picachuGana10Exp = new Pokemon( estado = Dormido(3), listaAtaques = List[Ataque](), tipoPrincipal = Fuego,
//        tipoSecundario = Agua, nivel = 4, experiencia = 0 + 10, genero = Macho, energia = 30,
//        energiaMax = 1000, peso = 5, fuerza = 100, velocidad = 20, condicionEvolutiva = SubirDeNivel(100), resistenciaEvolutiva = 3, null)
    
    
    val picachuCon2Exp = picachu.aumentaExperiencia(2)
    assert(picachuCon2Exp.experiencia == 2)
//    System.out.println("nivel: " + picachu.aumentaExperiencia(10).nivel)
    val picachuCon10Exp = picachu.aumentaExperiencia(10)
    assert(picachuCon10Exp.experiencia == 10)
    
  }
  
  "Un picachu que le falte un nivel para evolucionar gana suficiente exp para evolucionar" should "debe evolucionar en raychu" in {
    val raychu = new Pokemon (null, null, null, null, 1, 0, null, 100, 100, 100, 100, 100, null, 100)
    val picachu = new Pokemon( estado = Dormido(3), listaAtaques = List[Ataque](), tipoPrincipal = Fuego,
        tipoSecundario = Agua, nivel = 5, experiencia = 0, genero = Macho, energia = 30, energiaMax = 1000,
        peso = 5, fuerza = 100, velocidad = 20, condicionEvolutiva = SubirDeNivel(6), resistenciaEvolutiva = 10, raychu)
    
    val posibleRaychu = picachu.aumentaExperiencia(1270) // i did the math... =D
    assert(picachu.experienciaParaNivel(6) == 1270)
    assert(posibleRaychu == raychu)
  }
  
  
  
}
