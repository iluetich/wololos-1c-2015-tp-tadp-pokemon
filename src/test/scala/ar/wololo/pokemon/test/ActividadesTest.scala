
package ar.wololo.pokemon.test
import ar.wololo.pokemon.dominio._
import org.scalatest.FunSuite

class ActividadesTest extends FunSuite {

  val fixture = Fixt

  test("un pokemon realiza actividad Usar Pocion y se recarga 50 de vida") {

    val pikachuCurado = fixture.pikachu.realizarActividad(UsarPocion)

    assert(pikachuCurado.energia == 80)
  }

  test("pokemon realiza actividad UsarPocion y se reestablece toda la vida por que le falta 10") {

    assert(fixture.charmander.energia == 75)
    val charmanderCurado = fixture.charmander.realizarActividad(UsarPocion)

    assert(charmanderCurado.energia == 80)
    assert(charmanderCurado.energia == charmanderCurado.energiaMax)
  }

  test("pokemon Envenenado realiza Actividad Usar Antidoto y se recupera") {

    val gyaradosCurado = fixture.gyarados.realizarActividad(UsarAntidoto)

    assert(fixture.gyarados.estado == Envenenado)
    assert(gyaradosCurado.estado == Bueno)
  }

  test("pokemon Bueno realiza Actividad Usar Antidoto y no Hace nada") {

    val pikachuCurado = fixture.pikachu.realizarActividad(UsarAntidoto)

    assert(fixture.pikachu.estado == Bueno)
    assert(pikachuCurado.estado == Bueno)
  }

  test("pokemon de cualquier estado menos KO usa Ether y se pone normal") {

    val charmander = fixture.charmander.realizarActividad(UsarEther)
    val pikachu = fixture.pikachu.realizarActividad(UsarEther)

    assert(charmander.estado == Bueno)
    assert(pikachu.estado == Bueno)
  }

  test("pokemon dormido ignora 3 actividades y desp se recupera") {

    var scuartul = fixture.squirtle.realizarActividad(UsarPocion) //esta dormido

    assert(scuartul.energia == 500)

    scuartul = scuartul.realizarActividad(UsarPocion) //esta dormido

    assert(scuartul.energia == 500)

    scuartul = scuartul.realizarActividad(UsarPocion) //esta dormido

    assert(scuartul.energia == 500)

    scuartul = scuartul.realizarActividad(UsarPocion) //esta despierto

    assert(scuartul.energia == 550)
    assert(scuartul.estado == Bueno)

  }

  test("pokemon de estado KO hace cualquier actividad y tira error") {

    var tiroError = false

    try { val bulvasor = fixture.bulbasaur.realizarActividad(UsarEther) } //esta ko

    catch { case _: EstaKo => tiroError = true }
    assert(tiroError)
  }

  test("pokemon come hierro y aumenta en 5 su fuerza") {

    val pokemon = fixture.pikachu
    val pokemonConHierro = pokemon.realizarActividad(ComerHierro)

    assert(pokemon.fuerza == 2)
    assert(pokemonConHierro.fuerza == 7)
  }

  test("pokemon come calcio y aumenta en 5 su velocidad") {

    val pokemon = fixture.pikachu
    val pokemonConCalcio = pokemon.realizarActividad(ComerCalcio)

    assert(pokemon.velocidad == 9)
    assert(pokemonConCalcio.velocidad == 14)
  }

  test("pokemon al descansar recupera el maximo de PA de todos sus ataques") {

    val charmander = fixture.charmander

    val embestida = charmander.listaAtaques.find {ataque => ataque._1 == Fixt.embestida._1}
    
    embestida match {
      case Some(embestida) => assert(embestida._2 == 7)
      case None => assert(false)
    }

    val charmanderDescansado = charmander.realizarActividad(Descansar)

    val embestida2 = charmanderDescansado.listaAtaques.find { ataque => ataque._1 == Fixt.embestida._1 }
    
    embestida2 match {
      case Some(embestida2) => assert(embestida2._2 == 10)
      case None => assert(false)
    }
  }

  test("pokemon si no tiene estado Bueno y tiene menos de la mitad de la vida al realiza la actividad descansar se duerme por 3 turnos") {
    val pikachu = fixture.pikachu

    val pikachuDescansado = pikachu.realizarActividad(Descansar)

    assert(pikachuDescansado.estado == Dormido(3))

    val embestida = pikachuDescansado.listaAtaques.find { ataque => ataque._1 == Fixt.embestida._1 }
    embestida match {
      case Some(embestida) => assert(embestida._2 == 10)
    }
  }

  test("pokemon realiza un ataque de su tipo principal se baja en 1 el pa del ataque y gana 50 de exp") {
    assert(fixture.impactrueno._2 == 1)

    val pikachu = fixture.pikachu

    val actividad = new RealizarUnAtaque(fixture.impactrueno._1)

    val impactrueno2 = pikachu.listaAtaques.find { ataque => ataque._1 == fixture.impactrueno._1 }
    impactrueno2 match {
      case Some(impactrueno2) => assert(impactrueno2._2 == 1)
      case None => assert(false)
    }

    val pikachu2 = pikachu.realizarActividad(actividad)

    assert(pikachu.experiencia == 0)
    assert(pikachu2.experiencia == 50)

    val impactrueno = pikachu2.listaAtaques.find { ataque => ataque._1 == Fixt.impactrueno._1 }
    impactrueno match {
      case Some(impactrueno) => assert(impactrueno._2 == 0)
      case None => assert(false)
    }
  }

  test("pokemon realiza ataque tipo secundario y es macho gana 20 puntos de experiencia") {
    val pikachu = fixture.pikachu

    val ataque = new RealizarUnAtaque(fixture.embestida._1)

    val pikachu2 = pikachu.realizarActividad(ataque)

    assert(pikachu2.experiencia == 20)
  }

  test("pokemon realiza ataque tipo secundario y es Hembra gana 40 puntos de experiencia y aparte se aplican los efectos secundarios") {
    val charmander = fixture.charmander

    val ataque = new RealizarUnAtaque(fixture.embestida._1)

    val charmander2 = charmander.realizarActividad(ataque)

    assert(charmander2.experiencia == 40)
    assert(charmander2.estado == Dormido(3))
  }

  test("pokemon realiza ataque tipo dragon y gana 80 puntos") {
    val gyarados = fixture.gyarados

    val ataque = new RealizarUnAtaque(fixture.llama._1)

    val gyarados2 = gyarados.realizarActividad(ataque)

    assert(gyarados2.experiencia == 80)
  }

  test("pokemon no tiene Pa suficiente entonces tira error") {
    val pikachu = fixture.pikachu

    val actividad = new RealizarUnAtaque(fixture.impactrueno._1)

    val pikachu2 = pikachu.realizarActividad(actividad)

    var tiroError = false

    try { pikachu2.realizarActividad(actividad) }

    catch { case _: PokemonNoConoceMovONoTienePA => tiroError = true }

    assert(tiroError)
  }

  test("pokemon no conoce ataque entonces tira error") {
    val pikachu = fixture.pikachu

    val actividad = new RealizarUnAtaque(fixture.llama._1)

    var tiroError = false

    try { pikachu.realizarActividad(actividad) }

    catch { case _: PokemonNoConoceMovONoTienePA => tiroError = true }

    assert(tiroError)
  }

  test("pokemon realiza nadar y pierde 1 pt de energia por cada min,y por cada minuto gana 200 de exp, ademas los de agua ganan 1 de vel por cada min") {
    val actividad = Nadar(1)

    assert(fixture.gyarados.energia == 75)
    assert(fixture.lapras.velocidad == 9)
    val lapras = fixture.lapras.realizarActividad(actividad)
    val gyarados = fixture.gyarados.realizarActividad(actividad)

    assert(gyarados.experiencia == 200)
    assert(gyarados.energia == 74)

    assert(lapras.experiencia == 200)
    assert(lapras.velocidad == 10)
  }

  test("pokemon realiza nada y tipo principal o secundario pierden contra agua, entonces queda Ko y no gana experiencia") {
    val actividad = Nadar(2)

    val charmander = fixture.charmander.realizarActividad(actividad)

    assert(charmander.estado == Ko)
    assert(charmander.experiencia == 0)
  }

  test("pokemon levanta pesas, si esta bueno gana 1 de exp por kilo levantado, si tipo principal o secundario es pelea gana 2 por kilo,si es de tipo fantasma tira error") {
    val pikachu = fixture.pikachu
    val hitmonchan = fixture.hitmonchan
    val hunter = fixture.hunter

    val actividad = LevantarPesas(2)

    val pikachu2 = pikachu.realizarActividad(actividad)
    val hitmonchan2 = hitmonchan.realizarActividad(actividad)

    assert(pikachu2.experiencia == 2)
    assert(hitmonchan2.experiencia == 4)

    var tiroError = false

    try { hunter.realizarActividad(actividad) }

    catch { case _: FantasmaNoPuedeLevantarPesas => tiroError = true }

    assert(tiroError)
  }

  test("pokemon levanta pesas y esta paralizado entonces se va a Ko") {
    val pikachu = fixture.pikachu.copy(estado = Paralizado)

    val actividad = LevantarPesas(2)

    val pikachu2 = pikachu.realizarActividad(actividad)

    assert(pikachu2.estado == Ko)
  }

  test("pokemon levantar pesas levanta mas de 10kg por cada punto de fuerza entonces queda paralizado") {
    val pikachu = fixture.pikachu
    val actividad = LevantarPesas(1000)
    val pikachu2 = pikachu.realizarActividad(actividad)

    assert(pikachu2.estado == Paralizado)
  }

  test("pokemon trata de aprender un ataque normal y uno afin a su especie") {
    val voltod = fixture.voltorb
    val actividad = AprenderAtaque(fixture.impactrueno._1)

    assert(voltod.listaAtaques.size == 0)

    val voltod2 = voltod.realizarActividad(actividad)

    val contieneImpactrueno = voltod2.listaAtaques.find { ataque => ataque._1 == Fixt.impactrueno._1 }

    assert(voltod2.listaAtaques.size == 1)
    assert(contieneImpactrueno != None)

    val actividad2 = AprenderAtaque(fixture.embestida._1)
    val voltod3 = voltod2.realizarActividad(actividad2)

    val contieneEmbestida = voltod3.listaAtaques.find { ataque => ataque._1 == Fixt.embestida._1 }

    assert(voltod3.listaAtaques.size == 2)
    assert(contieneEmbestida != None)
  }

  test("pokemon trata de aprender un ataque que no es de tipo afin, entonces no aprende nada y queda Ko") {
    val voltod = fixture.voltorb
    val actividad = AprenderAtaque(fixture.llama._1)

    val voltod2 = voltod.realizarActividad(actividad)

    assert(voltod.estado == Bueno)
    assert(voltod2.listaAtaques.size == 0)
    assert(voltod2.estado == Ko)
  }

  test("pokemon realiza fingir Intercambio y no tiene condicion evolutiva Intercambiar, si es macho aumenta 1 de peso, si es hembra baja 10") {
    val pikachu = fixture.pikachu
    val hunter = fixture.hunter

    assert(pikachu.peso == 5)
    assert(hunter.peso == 12)

    val pikachu2 = pikachu.realizarActividad(FingirIntercambio)
    val hunter2 = hunter.realizarActividad(FingirIntercambio)

    assert(pikachu2.peso == 6)
    assert(hunter2.peso == 2)
  }

  test("pokemon tiene como condicion evolutiva usarUnaPiedra y realiza UsarPiedra pero la piedra le gana al tipo primario o secundario y el pokemon queda Envenenado") {
    val actividad = UsarPiedra(PiedraEvolutiva(Agua))
    val charmander = fixture.charmander
    val charmander2 = charmander.realizarActividad(actividad)

    assert(charmander.estado == Bueno)
    assert(charmander2.estado == Envenenado)
  }
}
