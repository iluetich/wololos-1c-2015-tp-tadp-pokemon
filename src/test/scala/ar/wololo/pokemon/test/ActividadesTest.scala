
package ar.wololo.pokemon.test
import ar.wololo.pokemon.dominio._
import org.scalatest.FunSuite

class ActividadesTest extends FunSuite {
  
  def fixture = new {
      val pikachu = new Pokemon(Bueno, List[Ataque](), Electrico, Normal ,
      1, 0, Macho, 30, 1000, 5, 100, 20, SubirDeNivel)
      
      val charmander = new Pokemon(Envenenado, List[Ataque](), Fuego, Normal ,
      1, 0, Hembra, 990, 1000, 5, 80, 50, SubirDeNivel)
  }
  
  
  test("un pokemon realiza actividad Usar Pocion y se recarga 50 de vida"){
    
    val pikachuCurado = fixture.pikachu.realizarActividad(UsarPocion)
    
    assert(pikachuCurado.energia == 80)
  }
  
  test("pokemon realiza actividad UsarPocion y se reestablece toda la vida por que le falta 10"){
    
    val charmanderCurado = fixture.charmander.realizarActividad(UsarPocion)
    
    assert(charmanderCurado.energia == 1000)
    assert(charmanderCurado.energia == charmanderCurado.energiaMax)
  }
    
  test("pokemon Envenenado realiza Actividad Usar Antidoto y se recupera"){
    
    val charmanderCurado = fixture.charmander.realizarActividad(UsarAntidoto)
    
    assert(fixture.charmander.estado == Envenenado)
    assert(charmanderCurado.estado == Bueno)
  }
}