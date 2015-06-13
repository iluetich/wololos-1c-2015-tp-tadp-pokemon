package ar.wololo.pokemon.test
import ar.wololo.pokemon.dominio._
import org.scalatest.FunSuite
import scala.util.{Try,Success,Failure}
import scala.util.Success
import scala.util.Failure

class RutinaTest extends FunSuite {
  
  def fixture = new {
      val pikachu = new Pokemon(Bueno, List[Ataque](), Electrico, Normal ,
      1, 0, Macho, 30, 1000, 5, 100, 20, SubirDeNivel(100), 0, null)
      
      val charmander = new Pokemon(Envenenado, List[Ataque](), Fuego, Tierra ,
      1, 0, Hembra, 400, 1000, 5, 80, 50, SubirDeNivel(100), 0, null)
      
      val scuartul = new Pokemon(Dormido(3), List[Ataque](), Agua, Normal ,
      7, 80, Hembra, 500, 800, 8, 90, 40, SubirDeNivel(100), 0, null)
      
      val bulvasor = new Pokemon(Ko, List[Ataque](), Planta, Bicho ,
      6, 0, Macho, 400, 1200, 9, 30, 20, SubirDeNivel(100), 0, null)
      
      val rutinaDeConsumo = new Rutina(List(UsarPocion,UsarAntidoto))
      val rutinaLarga = new Rutina(List(UsarEther,ComerZinc,ComerCalcio,ComerHierro,UsarPocion))
      val rutinaConDescanso = new Rutina(List(ComerCalcio,UsarAntidoto,Descansar,ComerHierro,UsarPocion))
      
  }
  
  
  
  test("un charmander realiza una rutina de consumo y se cura y ademas aumenta su energia"){
    
    val charmanderConsumidor = fixture.charmander.realizarRutina(fixture.rutinaDeConsumo)
    
    assert(charmanderConsumidor.map(_.energia) == Success(450))
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
    
    test("un pikachu se entrena mucho y se excede en fuerza"){
    
    val pikachuOK = fixture.pikachu.realizarRutina(fixture.rutinaLarga)
    
    assert(pikachuOK.isFailure)
  }
    
    test("pokemon descansa en medio de la rutina"){
    
    val charmander = fixture.charmander
    
    val embestida = charmander.listaAtaques.find { ataque => ataque.nombre == "Embestida"}
    embestida match {
      case Some(embestida) => assert(embestida.puntosAtaque == 7)
      case _ =>
    }
    
    val charmanderEntrenado = charmander.realizarRutina(fixture.rutinaConDescanso)
        
    val embestida2 = charmander.listaAtaques.find { ataque => ataque.nombre == "Embestida"}
    embestida2 match {
      case Some(embestida2) => assert(embestida2.puntosAtaque == 10)
      case _ =>
    }
    
    assert(charmanderEntrenado.map(_.estado)==Success(Dormido(1)))
    assert(charmanderEntrenado.map(_.velocidad)==Success(55))
  }
}
  
