package ar.wololo.pokemon.test
import ar.wololo.pokemon.dominio._

object Fixt {

  val fabricaDePokes = new PokemonBuilder
  val fabricaDeEspecies = new EspecieBuilder

  // tupla =(Ataque(nombre, efecto, tipo, puntosDeIniciales),puntosDeAtaque,puntosDeAtaqueMax)
  val impactrueno = (new Ataque("Impactrueno", Some(pokemon => pokemon), Electrico), 1, 20)
  val embestida = (new Ataque("Embestida", Some(pokemon => pokemon.copy(estado = Dormido(3))), Normal), 7, 10)
  val llama = (new Ataque("Llama", Some(pokemon => pokemon), Dragon), 15, 30)

  /*
   * Especies
   * Los seteos son en cualquier orden, excepto la especie evolucion, que debe ir despues
   * de asignar la condicion evolutiva.
   */

  val especieRaychu = fabricaDeEspecies
    .setTipos(principal = Electrico, secundario = Normal)
    .setIncrementos(incPeso = 6, incFuerza = 3, incEnergiaMax = 100, incVelocidad = 4)
    .setPesoMaximoSaludable(70)
    .setResistenciaEvolutiva(300)
    .build
  val especieCharizard = fabricaDeEspecies
    .setTipos(principal = Fuego, secundario = Volador)
    .setIncrementos(incPeso = 9, incFuerza = 10, incEnergiaMax = 200, incVelocidad = 5)
    .setPesoMaximoSaludable(90)
    .setResistenciaEvolutiva(600)
    .build
  val especieBlastoise = fabricaDeEspecies
    .setTipos(principal = Agua)
    .setIncrementos(incPeso = 10, incFuerza = 10, incEnergiaMax = 300, incVelocidad = 10)
    .setPesoMaximoSaludable(500)
    .setResistenciaEvolutiva(550)
    .build
  val especiePikachu = fabricaDeEspecies.setTipos(principal = Electrico, secundario = Normal)
    .setIncrementos(incPeso = 5, incFuerza = 2, incEnergiaMax = 80, incVelocidad = 9)
    .setPesoMaximoSaludable(40)
    .setResistenciaEvolutiva(200)
    .setCondicionEvolutiva(SubirDeNivel(100))
    .setEspecieEvolucion(especieRaychu)
    .build
  val especieCharmeleon = fabricaDeEspecies
    .setTipos(principal = Fuego, secundario = Normal)
    .setIncrementos(incPeso = 7, incFuerza = 5, incEnergiaMax = 100, incVelocidad = 2)
    .setPesoMaximoSaludable(50)
    .setResistenciaEvolutiva(300)
    .setCondicionEvolutiva(Intercambiar)
    .setEspecieEvolucion(especieCharizard)
    .build
  val especieCharmander = fabricaDeEspecies
    .setTipos(principal = Fuego, secundario = Normal)
    .setIncrementos(incPeso = 6, incFuerza = 4, incEnergiaMax = 80, incVelocidad = 1)
    .setPesoMaximoSaludable(30)
    .setResistenciaEvolutiva(300)
    .setCondicionEvolutiva(UsarUnaPiedra)
    .setEspecieEvolucion(especieCharmeleon)
    .build
  val especieElectrode = fabricaDeEspecies
    .setTipos(principal = Electrico)
    .setIncrementos(incPeso = 10, incFuerza = 12, incEnergiaMax = 40, incVelocidad = 5)
    .setPesoMaximoSaludable(20)
    .setResistenciaEvolutiva(250)
    .build
  val especieWarturtle = fabricaDeEspecies
    .setTipos(principal = Agua)
    .setIncrementos(incPeso = 4, incFuerza = 6, incEnergiaMax = 80, incVelocidad = 5)
    .setPesoMaximoSaludable(100)
    .setResistenciaEvolutiva(400)
    .setCondicionEvolutiva(SubirDeNivel(30))
    .setEspecieEvolucion(especieBlastoise)
    .build
  val especieSquirtle = fabricaDeEspecies
    .setTipos(principal = Agua, secundario = Normal)
    .setIncrementos(incPeso = 4, incFuerza = 1, incEnergiaMax = 130, incVelocidad = 2)
    .setPesoMaximoSaludable(45)
    .setResistenciaEvolutiva(230)
    .build
  val especieLapras = fabricaDeEspecies
    .setTipos(principal = Agua, secundario = Dragon)
    .setIncrementos(incPeso = 8, incFuerza = 10, incEnergiaMax = 80, incVelocidad = 9)
    .setPesoMaximoSaludable(100)
    .setResistenciaEvolutiva(350)
    .build
  val especieBulbasaur = fabricaDeEspecies
    .setTipos(principal = Planta, secundario = Bicho)
    .setIncrementos(incPeso = 3, incFuerza = 6, incEnergiaMax = 80, incVelocidad = 1)
    .setPesoMaximoSaludable(80)
    .setResistenciaEvolutiva(140)
    .build
  val especieGyarados = fabricaDeEspecies
    .setTipos(principal = Dragon, secundario = Bicho)
    .setIncrementos(incPeso = 9, incFuerza = 5, incEnergiaMax = 80, incVelocidad = 8)
    .setPesoMaximoSaludable(75)
    .setResistenciaEvolutiva(280)
    .build
  val especieHitmonchan = fabricaDeEspecies
    .setTipos(principal = Pelea, secundario = Normal)
    .setIncrementos(incPeso = 10, incFuerza = 5, incEnergiaMax = 80, incVelocidad = 3)
    .setPesoMaximoSaludable(95)
    .setResistenciaEvolutiva(450)
    .build
  val especieHunter = fabricaDeEspecies
    .setTipos(principal = Fantasma, secundario = Normal)
    .setIncrementos(incPeso = 12, incFuerza = 2, incEnergiaMax = 80, incVelocidad = 15)
    .setPesoMaximoSaludable(65)
    .setResistenciaEvolutiva(550)
    .build
  val especieVoltorb = fabricaDeEspecies
    .setTipos(principal = Electrico)
    .setIncrementos(incPeso = 7, incFuerza = 5, incEnergiaMax = 80, incVelocidad = 6)
    .setPesoMaximoSaludable(15)
    .setResistenciaEvolutiva(83)
    .setCondicionEvolutiva(UsarUnaPiedra)
    .setEspecieEvolucion(especieElectrode)
    .build
  /*
     * Especies para Tests de Pablo
     */
  val especiePokeMix = new Especie(tipoPrincipal = Hielo, tipoSecundario = Some(Roca),
    incrementoFuerza = 3, incrementoVelocidad = 2, incrementoPeso = 4,
    incrementoEnergiaMax = 1000, pesoMaximoSaludable = 200,
    resistenciaEvolutiva = 5, condicionEvolutiva = None,
    especieEvolucion = null)
  val especiePoke = new Especie(tipoPrincipal = Fuego, tipoSecundario = Some(Veneno),
    incrementoFuerza = 4, incrementoVelocidad = 1, incrementoPeso = 1,
    incrementoEnergiaMax = 100, pesoMaximoSaludable = 100,
    resistenciaEvolutiva = 3, condicionEvolutiva = Some(SubirDeNivel(5)),
    especieEvolucion = Some(especiePokeMix))
  /*
   * Pokemones[Los atributos ahora dependen del nivel y de la especie, para asegurarnos que sean válidos.]
   * fuerza = nivel * incrementoFuerza
   * energiaMax = nivel * incrementoEnergiaMax
   * velocidad = nivel * incrementoVelocidad
   * peso = min(nivel * incrementoPeso, especie.pesoMaximoSaludable)
   * 
   * Los seteos deben ser: estado => especie => cualquier otro (menos nivel y experiencia) => nivel => experiencia => cualquier otro 
   */

  val pikachu = fabricaDePokes
    .setEstado(Bueno)
    .setEspecie(especiePikachu)
    .setAtaques(List(impactrueno, embestida))
    .setExperiencia(0)
    .setGenero(Macho)
    .setEnergia(30)
    .build
  val charmander = fabricaDePokes
    .setEstado(Bueno)
    .setEspecie(especieCharmander)
    .setAtaques(List(embestida))
    .setExperiencia(0)
    .setGenero(Hembra)
    .setEnergia(30)
    .build
  val squirtle = fabricaDePokes
    .setEstado(Dormido(3))
    .setEspecie(especieSquirtle)
    .setAtaques(List())
    .setExperiencia(25000)
    .setGenero(Hembra)
    .setEnergia(80)
    .build
  val lapras = fabricaDePokes
    .setEstado(Bueno)
    .setEspecie(especieLapras)
    .setAtaques(List())
    .setExperiencia(0)
    .setGenero(Macho)
    .setEnergia(75)
    .build
  val bulbasaur = fabricaDePokes
    .setEstado(Ko)
    .setEspecie(especieBulbasaur)
    .setAtaques(List())
    .setExperiencia(8000)
    .setGenero(Macho)
    .setEnergia(75)
    .build
  val gyarados = fabricaDePokes
    .setEstado(Envenenado)
    .setEspecie(especieGyarados)
    .setAtaques(List(llama))
    .setExperiencia(0)
    .setGenero(Macho)
    .setEnergia(75)
    .build
  val hitmonchan = fabricaDePokes
    .setEstado(Bueno)
    .setEspecie(especieHitmonchan)
    .setAtaques(List())
    .setExperiencia(0)
    .setGenero(Macho)
    .setEnergia(60)
    .build
  val hunter = fabricaDePokes
    .setEstado(Bueno)
    .setEspecie(especieHunter)
    .setAtaques(List())
    .setExperiencia(0)
    .setGenero(Hembra)
    .setEnergia(75)
    .build
  val voltorb = fabricaDePokes
    .setEstado(Bueno)
    .setEspecie(especieVoltorb)
    .setAtaques(List())
    .setExperiencia(50)
    .setGenero(Hembra)
    .setEnergia(75)
    .build

  val charmeleon = fabricaDePokes
    .setEstado(Bueno)
    .setEspecie(especieCharmeleon)
    .setAtaques(List())
    .setExperiencia(9830100)
    .setGenero(Macho)
    .setEnergia(100)
    .build

  val pikaka = fabricaDePokes
    .setEspecie(especiePoke)
    .setEstado(Dormido(3))
    .setAtaques(List())
    .setExperiencia(0)
    .setGenero(Macho)
    .setEnergia(30)
    .build
  /*
   * Algunas rutinas
   * Rutina(nombre, actividades)
   */

  val rutinaNado = new Rutina("natación_tranca", List[Actividad](Nadar(1), Nadar(1), Nadar(1), Nadar(1)))
  val rutinaPhelps = new Rutina("natación_pro", List[Actividad](Nadar(1000), Nadar(1000), Nadar(1000), Nadar(1000)))
  val rutinaPocionado = new Rutina("pocionado", List[Actividad](UsarPocion, UsarPocion, UsarPocion, UsarPocion))
  val rutinaIntercambio = new Rutina("intercambiado", List[Actividad](FingirIntercambio, FingirIntercambio, FingirIntercambio))

}
