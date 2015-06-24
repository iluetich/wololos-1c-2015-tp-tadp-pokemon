package ar.wololo.pokemon.test

import ar.wololo.pokemon.dominio._
import org.scalatest.FunSuite
import org.scalatest.FlatSpec

class SetSpec extends FlatSpec {
  val especiePokeMix = new Especie(tipoPrincipal = Hielo, tipoSecundario = Roca,
      incrementoFuerza = 3, incrementoVelocidad = 2, incrementoPeso = 4, 
      incrementoEnergiaMax = 1000, pesoMaximoSaludable = 200, 
      resistenciaEvolutiva = 5, condicionEvolutiva = NoEvoluciona,
      especieEvolucion = null)
  val especiePoke = new Especie(tipoPrincipal = Fuego, tipoSecundario = Veneno,
      incrementoFuerza = 4, incrementoVelocidad = 1, incrementoPeso = 1, 
      incrementoEnergiaMax = 100, pesoMaximoSaludable = 100, 
      resistenciaEvolutiva = 3, condicionEvolutiva = new SubirDeNivel(5),
      especieEvolucion = especiePokeMix)
  val pikachu = new Pokemon( estado = new Dormido(3), listaAtaques = List[Ataque](),
      nivel = 1, experiencia = 0,
      genero = Macho, energia = 30, energiaMax = 1000, peso = 5, fuerza = 7,
      velocidad = 20, especie = especiePoke)
  "Un pichachu que gana experiencia" should "si picachu gana 2 de experiencia entonces aumenta solo 2 la experiencia" in {
    assert( pikachu.aumentaExperiencia(2) === pikachu.copy(nivel = 1, experiencia = 2))
  }
  it should "si picachu gana 5 de experiencia entonces la experiencia debería aumentar a 5 y el nivel a 2" in {
    val pikachuMejorado = pikachu.aumentaExperiencia(5)
    assert( pikachuMejorado equals pikachu.copy(nivel = 2, experiencia = 5,
                                                fuerza = 11, energiaMax = 1100, peso = 6) )
    assert( pikachuMejorado.especie.experienciaParaNivel(pikachuMejorado.nivel +1) === 9)
  }
  it should "si picachu gana 10 de experiencia entonces la experiencia debería aumentar a 10 y el nivel pasar 4 y el salto de nivel a 12" in {
    val pikachuMejorado = pikachu.aumentaExperiencia(10)
    assert( pikachuMejorado === pikachu.copy(nivel = 3, experiencia = 10,
                                             fuerza = 15, energiaMax = 1200, peso = 7) )
    assert( pikachuMejorado.especie.experienciaParaNivel(pikachuMejorado.nivel + 1) === 21 ) 
  }
  it should "si picachu gana 5 y despues 5 de experiencia tiene que ser igual a ganar 10 todo junto" in {
    assert( pikachu.aumentaExperiencia(5).aumentaExperiencia(5) === pikachu.aumentaExperiencia(10))
  }
  it should "evolucionar cuanda gana 110 de experiencia de evolucionar por condicionEvolutiva (SubirNivel(5))" in {
    val pikachuMejorado = pikachu.aumentaExperiencia(110)
    assert ( pikachuMejorado == pikachu.copy( nivel = 5, experiencia = 110, especie = especiePokeMix,
                                              fuerza = 23, energiaMax = 1400, peso = 9) )
    assert ( pikachuMejorado.especie.experienciaParaNivel(pikachuMejorado.nivel) ===  75)
  }
}
