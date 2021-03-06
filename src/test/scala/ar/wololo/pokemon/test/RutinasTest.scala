package ar.wololo.pokemon.test
import ar.wololo.pokemon.dominio._
import org.scalatest.FunSuite
import scala.util.{ Try, Success, Failure }
import scala.util.Success
import scala.util.Failure

class RutinaTest extends FunSuite {

  val fixture = Fixt
  val rutinaDeConsumo = new Rutina("rutina de consumo", List(UsarPocion, UsarAntidoto))
  val rutinaLarga = new Rutina("rutina larga", List(ComerZinc, ComerCalcio, ComerHierro, UsarPocion))
  val rutinaConDescanso = new Rutina("rutina con descanso", List(ComerCalcio, UsarAntidoto, Descansar, ComerHierro, UsarPocion))

  test("un charmander realiza una rutina de consumo y se cura y ademas aumenta su energia") {

    val charmanderConsumidor = fixture.charmander.realizarRutina(rutinaDeConsumo)

    assert(charmanderConsumidor.map(_.energia) == Success(80))
    assert(charmanderConsumidor.map(_.estado) == Success(Bueno))
  }

  test("un scuartul realiza una rutina  larga y duerme 3 turnos, luego realiza actividades") {

    val scuartulDespierto = fixture.squirtle.realizarRutina(rutinaLarga)

    assert(scuartulDespierto.map(_.velocidad) == Success(14))
    assert(scuartulDespierto.map(_.fuerza) == Success(7))
    assert(scuartulDespierto.map(_.energia) == Success(130))
    assert(scuartulDespierto.map(_.estado) == Success(Bueno))
  }

  test("un bulvasor realiza una rutina pero esta KO asique lanza exepcion") {

    val bulvasorKO = fixture.bulbasaur.realizarRutina(rutinaLarga)

    assert(bulvasorKO == Failure(EstaKo(fixture.bulbasaur)))

  }

  test("pokemon descansa en medio de la rutina") {

    val charmander = fixture.charmander
    val charmanderEntrenado = charmander.realizarRutina(rutinaConDescanso)

    print(charmander.peso + "\n")
    print(charmanderEntrenado.get.peso + "\n")
    assert(charmanderEntrenado.map(_.estado) == Success(Dormido(1)))
    assert(charmanderEntrenado.map(_.velocidad) == Success(6))
  }
}  
  
