package ar.wololo.pokemon.test
import ar.wololo.pokemon.dominio._
import org.scalatest.FunSuite
import scala.util.{Try,Success,Failure}
import scala.util.Success
import scala.util.Failure

class RutinaTest extends FunSuite {
  
  def fixture = new {
      val pikachu = new Pokemon(Bueno, List[Ataque](), Electrico, Normal ,
      1, 0, Macho, 30, 1000, 5, 100, 20, SubirDeNivel, 0, 0)
      
      val charmander = new Pokemon(Envenenado, List[Ataque](), Fuego, Tierra ,
      1, 0, Hembra, 990, 1000, 5, 80, 50, SubirDeNivel, 0, 0)
      
      val scuartul = new Pokemon(Dormido(3), List[Ataque](), Agua, Normal ,
      7, 80, Hembra, 500, 800, 8, 90, 40, SubirDeNivel, 0, 0)
      
      val bulvasor = new Pokemon(Ko, List[Ataque](), Planta, Bicho ,
      6, 0, Macho, 400, 1200, 9, 30, 20, SubirDeNivel, 0, 0)
      
      val rutinaDeConsumo = new Rutina(List(UsarPocion,UsarAntidoto))
      val rutinaLarga = new Rutina(List(UsarEther,ComerZinc,ComerCalcio,ComerHierro,UsarPocion))
      
  }
  
  
  
  test("un charmander realiza una rutina de consumo y se cura y ademas aumenta su energia"){
    
    val charmanderConsumidor = fixture.charmander.realizarRutina(fixture.rutinaDeConsumo)
    
    assert(charmanderConsumidor.map(_.energia) == Success(1000))
    assert(charmanderConsumidor.map(_.estado) == Success(Bueno))
  }
  
   test("un scuartul realiza una rutina  larga y duerme 3 turnos, luego realiza actividades"){
    
    val scuartulDespierto = fixture.scuartul.realizarRutina(fixture.rutinaLarga)
    
    assert(scuartulDespierto.map(_.velocidad) == Success(40))
    assert(scuartulDespierto.map(_.fuerza) == Success(95))
    assert(scuartulDespierto.map(_.estado) == Success(Bueno))
  }
   
    test("un bulvasor realiza una rutina pero esta KO asique lanza exepcion"){
    
    val bulvasorKO = fixture.bulvasor.realizarRutina(fixture.rutinaLarga)
    
    assert(bulvasorKO== Failure(EstaKo(fixture.bulvasor)))

  }
    
    test("un pikachu se entrena mucho y hace toda la rutina como corresponde"){
    
    val pikachuOK = fixture.pikachu.realizarRutina(fixture.rutinaLarga)
    
    assert(pikachuOK.map(_.velocidad) == Success(25))
    assert(pikachuOK.map(_.fuerza) == Success(105))
    assert(pikachuOK.map(_.estado) == Success(Bueno))
    assert(pikachuOK.map(_.energia) == Success(80))
  }
}
  
