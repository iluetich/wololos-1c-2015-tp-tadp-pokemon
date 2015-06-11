
package ar.wololo.pokemon.test
import ar.wololo.pokemon.dominio._
import org.scalatest.FunSuite

class ActividadesTest extends FunSuite {
  
  def fixture = new {
	    val impactrueno = new Ataque("Impactrueno",(Pokemon => Pokemon),Electrico,1,20)
      val embestida = new Ataque("Embestida",(Pokemon => Pokemon.copy(estado = Dormido(3))),Normal,7,10)
      val llama = new Ataque("Llama",(Pokemon => Pokemon),Dragon,15,30)
      
      val pikachu = new Pokemon(Bueno, List(impactrueno , embestida), Electrico, Normal ,
      1, 0, Macho, 30, 1000, 5, 90, 20, SubirDeNivel)
      
      val charmander = new Pokemon(Envenenado, List(embestida), Fuego, Normal ,
      1, 0, Hembra, 990, 1000, 5, 80, 50, SubirDeNivel)
      
      val scuartul = new Pokemon(Dormido(3), List[Ataque](), Agua, Normal ,
      7, 80, Hembra, 500, 800, 8, 90, 40, SubirDeNivel)
      
      val lapras = new Pokemon(Bueno, List[Ataque](), Agua, Dragon ,
      7, 80, Macho, 500, 800, 8, 20, 50, Intercambiar)
      
      val bulvasor = new Pokemon(Ko, List[Ataque](), Planta, Bicho ,
      6, 0, Macho, 400, 1200, 9, 30, 20, SubirDeNivel)
      
      val gyarados = new Pokemon(Envenenado, List(llama), Dragon, Bicho ,
      5, 0, Macho, 400, 500, 9, 80, 60, UsarUnaPiedraLunar)
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
  
  test("pokemon Bueno realiza Actividad Usar Antidoto y no Hace nada"){
    
    val pikachuCurado = fixture.pikachu.realizarActividad(UsarAntidoto)
    
    assert(fixture.pikachu.estado == Bueno)
    assert(pikachuCurado.estado == Bueno)
  }
  
  test("pokemon de cualquier estado menos KO usa Ether y se pone normal"){
    
    val charmander = fixture.charmander.realizarActividad(UsarEther)
    val pikachu = fixture.pikachu.realizarActividad(UsarEther)
    
    assert(charmander.estado == Bueno)
    assert(pikachu.estado == Bueno)
  }
  
  test("pokemon dormido ignora 3 actividades y desp se recupera"){
        
    var scuartul = fixture.scuartul.realizarActividad(UsarPocion)//esta dormido
    
    assert(scuartul.energia == 500)
           
    scuartul = scuartul.realizarActividad(UsarPocion)//esta dormido
    
    assert(scuartul.energia == 500)
    
    scuartul = scuartul.realizarActividad(UsarPocion)//esta dormido
    
    assert(scuartul.energia == 500)
    
    scuartul = scuartul.realizarActividad(UsarPocion)//esta despierto
    
    assert(scuartul.energia == 550)
    assert(scuartul.estado == Bueno)
       
  }
  
  test("pokemon de estado KO hace cualquier actividad y tira error"){
    
    var tiroError = false
    
    try {val bulvasor = fixture.bulvasor.realizarActividad(UsarEther)}//esta ko
    
    catch{case _ : EstaKo => tiroError = true}
    assert(tiroError)
  }
  
  test("pokemon come hierro y aumenta en 5 su fuerza"){
    
    val pokemon = fixture.pikachu
    val pokemonConHierro = pokemon.realizarActividad(ComerHierro)
    
    assert(pokemon.fuerza == 90)
    assert(pokemonConHierro.fuerza == 95)
  }
  
  test("pokemon come calcio y aumenta en 5 su velocidad"){
    
    val pokemon = fixture.pikachu
    val pokemonConCalcio = pokemon.realizarActividad(ComerCalcio)
    
    assert(pokemon.velocidad == 20)
    assert(pokemonConCalcio.velocidad == 25)
  }
  
  test("pokemon al descansar recupera el maximo de PA de todos sus ataques"){
    
    val charmander = fixture.charmander
    
    val embestida = charmander.listaAtaques.find { ataque => ataque.nombre == "Embestida"}
    embestida match {
      case Some(embestida) => assert(embestida.puntosAtaque == 7)
    }
    
    val charmanderDescansado = charmander.realizarActividad(Descansar)
        
    val embestida2 = charmander.listaAtaques.find { ataque => ataque.nombre == "Embestida"}
    embestida2 match {
      case Some(embestida2) => assert(embestida2.puntosAtaque == 10)
    }
  }
  
  test("pokemon si no tiene estado Bueno y tiene menos de la mitad de la vida al realiza la actividad descansar se duerme por 3 turnos"){
    val pikachu = fixture.pikachu
    
    val pikachuDescansado = pikachu.realizarActividad(Descansar)
    
    assert(pikachuDescansado.estado == Dormido(3))
    
    val embestida = pikachuDescansado.listaAtaques.find { ataque => ataque.nombre == "Embestida"}
    embestida match {
      case Some(embestida) => assert(embestida.puntosAtaque == 10)
    }
  }
  
  test("pokemon realiza un ataque de su tipo principal se baja en 1 el pa del ataque y gana 50 de exp"){
    val pikachu = fixture.pikachu
    
    val actividad = new RealizarUnAtaque(fixture.impactrueno)
    
    val pikachu2 = pikachu.realizarActividad(actividad)
    
    assert(pikachu.experiencia == 0)
    assert(pikachu2.experiencia == 50)
    
    val impactrueno = pikachu2.listaAtaques.find { ataque => ataque.nombre == "Impactrueno"}
    impactrueno match {
      case Some(impactrueno) => assert(impactrueno.puntosAtaque == 0)
    }
  }
  
  test("pokemon realiza ataque tipo secundario y es macho gana 20 puntos de experiencia"){
    val pikachu = fixture.pikachu
    
    val ataque = new RealizarUnAtaque(fixture.embestida)
    
    val pikachu2 = pikachu.realizarActividad(ataque)
    
    assert(pikachu2.experiencia == 20)
  }
  
  test("pokemon realiza ataque tipo secundario y es Hembra gana 40 puntos de experiencia y aparte se aplican los efectos secundarios"){
    val charmander = fixture.charmander
    
    val ataque = new RealizarUnAtaque(fixture.embestida)
    
    val charmander2 = charmander.realizarActividad(ataque)
    
    assert(charmander2.experiencia == 40)
    assert(charmander2.estado == Dormido(3))
  }
  
  test("pokemon realiza ataque tipo dragon y gana 80 puntos"){
    val gyarados = fixture.gyarados
    
    val ataque = new RealizarUnAtaque(fixture.llama)
    
    val gyarados2 = gyarados.realizarActividad(ataque)
    
    assert(gyarados2.experiencia == 80) 
  }
  
  test("pokemon no tiene Pa suficiente entonces tira error"){
    val pikachu = fixture.pikachu
    
    val actividad = new RealizarUnAtaque(fixture.impactrueno)
    
    val pikachu2 = pikachu.realizarActividad(actividad)
    
    var tiroError = false
    
    try{pikachu2.realizarActividad(actividad)}
    
    catch{case _: PokemonNoConoceMovONoTienePA => tiroError = true}
    
    assert(tiroError)
  }
  
  test("pokemon no conoce ataque entonces tira error"){
    val pikachu = fixture.pikachu
    
    val actividad = new RealizarUnAtaque(fixture.llama)
    
    var tiroError = false
    
    try{pikachu.realizarActividad(actividad)}
    
    catch{case _: PokemonNoConoceMovONoTienePA => tiroError = true}
    
    assert(tiroError)
  }
  
  test("pokemon realiza nadar y pierde 1 pt de energia por cada min,y por cada minuto gana 200 de exp, ademas los de agua ganan 1 de vel por cada min"){
    val actividad = Nadar(2)
    
    val lapras = fixture.lapras.realizarActividad(actividad)
    val gyarados = fixture.gyarados.realizarActividad(actividad)
    
    assert(gyarados.experiencia == 400)
    assert(lapras.experiencia == 480)
    
  }
  
  test("pokemon realiza nada y tipo principal o secundario pierden contra agua, entonces queda Ko y no gana experiencia"){
    val actividad = Nadar(2)
    
    val charmander = fixture.charmander.realizarActividad(actividad)
    
    assert(charmander.estado == Ko)
    assert(charmander.experiencia == 0)
  }
}