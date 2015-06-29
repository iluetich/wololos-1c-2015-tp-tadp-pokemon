package ar.wololo.pokemon.test

import ar.wololo.pokemon.dominio._
import org.scalatest.FunSuite
import org.scalatest.FlatSpec

class SetSpec extends FlatSpec {
  val pikaka = Fixture.pikaka
  "Un pichachu que gana experiencia" should "si picachu gana 2 de experiencia entonces aumenta solo 2 la experiencia" in {
    assert( pikaka.aumentaExperiencia(2) === pikaka.copy(nivel = 1, experiencia = 2))
  }
  it should "si picachu gana 5 de experiencia entonces la experiencia debería aumentar a 5 y el nivel a 2" in {
    val pikachuMejorado = pikaka.aumentaExperiencia(5)
    assert( pikachuMejorado equals pikaka.copy(nivel = 2, experiencia = 5,
                                               fuerza = 11, energiaMax = 1100, peso = 6) )
    assert( pikachuMejorado.especie.experienciaParaNivel(pikachuMejorado.nivel +1) === 9)
  }
  it should "si picachu gana 10 de experiencia entonces la experiencia debería aumentar a 10 y el nivel pasar 4 y el salto de nivel a 12" in {
    val pikachuMejorado = pikaka.aumentaExperiencia(10)
    assert( pikachuMejorado === pikaka.copy(nivel = 3, experiencia = 10,
                                            fuerza = 15, energiaMax = 1200, peso = 7) )
    assert( pikachuMejorado.especie.experienciaParaNivel(pikachuMejorado.nivel + 1) === 21 ) 
  }
  it should "si picachu gana 5 y despues 5 de experiencia tiene que ser igual a ganar 10 todo junto" in {
    assert( pikaka.aumentaExperiencia(5).aumentaExperiencia(5) === pikaka.aumentaExperiencia(10))
  }
  it should "evolucionar cuanda gana 110 de experiencia de evolucionar por condicionEvolutiva (SubirNivel(5))" in {
    val pikachuMejorado = pikaka.aumentaExperiencia(110)
    assert ( pikachuMejorado == pikaka.copy( nivel = 5, experiencia = 110, especie = Fixture.especiePokeMix,
                                              fuerza = 23, energiaMax = 1400, peso = 9) )
    assert ( pikachuMejorado.especie.experienciaParaNivel(pikachuMejorado.nivel) ===  75)
  }
}
