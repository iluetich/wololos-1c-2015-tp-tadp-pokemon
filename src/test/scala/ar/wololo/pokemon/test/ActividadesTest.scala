
package ar.wololo.pokemon.test
import ar.wololo.pokemon.dominio._
import org.scalatest.FunSuite

class ActividadesTest extends FunSuite {
  
  def fixture = new {
	    val impactrueno = new Ataque("Impactrueno",(Pokemon => Pokemon),Electrico,3,20)
      val embestida = new Ataque("Embestida",(Pokemon => Pokemon),Normal,7,10)
      
      val pikachu = new Pokemon(Bueno, List(impactrueno , embestida), Electrico, Normal ,
      1, 0, Macho, 30, 1000, 5, 100, 20, SubirDeNivel)
      
      val charmander = new Pokemon(Envenenado, List(embestida), Fuego, Tierra ,
      1, 0, Hembra, 990, 1000, 5, 80, 50, SubirDeNivel)
      
      val scuartul = new Pokemon(Dormido(3), List[Ataque](), Agua, Normal ,
      7, 80, Hembra, 500, 800, 8, 90, 40, SubirDeNivel)
      
      val bulvasor = new Pokemon(Ko, List[Ataque](), Planta, Bicho ,
      6, 0, Macho, 400, 1200, 9, 30, 20, SubirDeNivel)
      
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
    
    assert(pokemon.fuerza == 100)
    assert(pokemonConHierro.fuerza == 105)
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
}