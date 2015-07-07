package ar.wololo.pokemon.test

import ar.wololo.pokemon.dominio._
import org.scalatest.FunSuite
import org.scalatest.FlatSpec

class SetSpec extends FlatSpec {
  val pikaka = Fixt.pikaka
  "Un pichachu que gana experiencia" should "si picachu gana 2 de experiencia entonces aumenta solo 2 la experiencia" in {
    assert(pikaka.aumentaExperiencia(2).experiencia == 2)
  }
  it should "si picachu gana 5 de experiencia entonces la experiencia debería aumentar a 5 y el nivel a 2" in {
    val pikachuMejorado = pikaka.aumentaExperiencia(5)
    assert(pikachuMejorado.experiencia == 5)
    assert(pikachuMejorado.nivel == 2)
    assert(pikachuMejorado.especie.experienciaParaNivel(pikachuMejorado.nivel + 1) == 9)
  }
  it should "si picachu gana 10 de experiencia entonces la experiencia debería aumentar a 10 y el nivel pasar 3 y el salto de nivel a 12" in {
    assert(pikaka.fuerza == 4)
    val pikachuMejorado = pikaka.aumentaExperiencia(10)
    assert(pikachuMejorado.fuerza == 12)
    assert(pikachuMejorado.experiencia == 10)
    assert(pikachuMejorado.nivel == 3)
    assert(pikachuMejorado.especie.experienciaParaNivel(pikachuMejorado.nivel + 1) === 21)
  }
  it should "si picachu gana 5 y despues 5 de experiencia tiene que ser igual a ganar 10 todo junto" in {
    assert(pikaka.aumentaExperiencia(5).aumentaExperiencia(5).experiencia == 10)
  }
  it should "evolucionar cuanda gana 110 de experiencia de evolucionar por condicionEvolutiva (SubirNivel(5))" in {
    val pikachuMejorado = pikaka.aumentaExperiencia(110)
    assert(pikachuMejorado.experiencia == 110)
    assert(pikachuMejorado.especie.eq(Fixt.especiePokeMix))
    assert(pikachuMejorado.especie.experienciaParaNivel(pikachuMejorado.nivel) === 75)
  }
}
