package ar.wololo.pokemon.test
import ar.wololo.pokemon.dominio._
import sun.awt.AppContext.GetAppContextLock

object Fixt {

  val fabricaDePokes = new PokemonFactory
  val fabricaDeEspecies = new EspecieFactory

  // Ataque(nombre, efecto, tipo, puntosDeAtaque, puntosDeAtaqueMax)
  val impactrueno = new Ataque("Impactrueno", (Pokemon => Pokemon), Electrico, 1, 20)
  val embestida = new Ataque("Embestida", (Pokemon => Pokemon.copy(estado = Dormido(3))), Normal, 7, 10)
  val llama = new Ataque("Llama", (Pokemon => Pokemon), Dragon, 15, 30)

  // Especies
  print(1)
  val especieRaychu = fabricaDeEspecies.setTipos(principal = Electrico, secundario = Normal)
    .setIncrementos(incPeso = 6, incFuerza = 3, incEnergiaMax = 100, incVelocidad = 4)
    .setPesoMaximoSaludable(70)
    .setResistenciaEvolutiva(300)
    .build
  print(2)
  val especieCharizard = fabricaDeEspecies.setTipos(principal = Fuego, secundario = Volador)
    .setIncrementos(incPeso = 9, incFuerza = 10, incEnergiaMax = 200, incVelocidad = 5)
    .setPesoMaximoSaludable(90)
    .setResistenciaEvolutiva(600)
    .build
  print(3)
  val especieBlastoise = fabricaDeEspecies.setTipos(principal = Agua)
    .setIncrementos(incPeso = 10, incFuerza = 10, incEnergiaMax = 300, incVelocidad = 10)
    .setPesoMaximoSaludable(500)
    .setResistenciaEvolutiva(550)
    .build
  print(4)
  val especiePikachu = fabricaDeEspecies.setTipos(principal = Electrico, secundario = Normal)
    .setIncrementos(incPeso = 5, incFuerza = 2, incEnergiaMax = 80, incVelocidad = 9)
    .setPesoMaximoSaludable(40)
    .setResistenciaEvolutiva(200)
    .setCondicionEvolutiva(SubirDeNivel(100))
    .setEspecieEvolucion(especieRaychu)
    .build
  print(5)
  val especieCharmeleon = fabricaDeEspecies.setTipos(principal = Fuego, secundario = Normal)
    .setIncrementos(incPeso = 7, incFuerza = 5, incEnergiaMax = 100, incVelocidad = 2)
    .setPesoMaximoSaludable(50)
    .setResistenciaEvolutiva(300)
    .setCondicionEvolutiva(Intercambiar)
    .setEspecieEvolucion(especieCharizard)
    .build
  print(6)
  val especieCharmander = fabricaDeEspecies.setTipos(principal = Fuego, secundario = Normal)
    .setIncrementos(incPeso = 6, incFuerza = 4, incEnergiaMax = 80, incVelocidad = 1)
    .setPesoMaximoSaludable(30)
    .setResistenciaEvolutiva(300)
    .setCondicionEvolutiva(UsarUnaPiedra)
    .setEspecieEvolucion(especieCharmeleon)
    .build

  print(7)
  val especieWarturtle = fabricaDeEspecies.setTipos(principal = Agua)
    .setIncrementos(incPeso = 4, incFuerza = 6, incEnergiaMax = 80, incVelocidad = 5)
    .setPesoMaximoSaludable(100)
    .setResistenciaEvolutiva(400)
    .setCondicionEvolutiva(SubirDeNivel(30))
    .setEspecieEvolucion(especieBlastoise)
    .build
  print(8)
  val especieSquirtle = fabricaDeEspecies.setTipos(principal = Agua, secundario = Normal)
    .setIncrementos(incPeso = 4, incFuerza = 1, incEnergiaMax = 80, incVelocidad = 2)
    .setPesoMaximoSaludable(45)
    .setResistenciaEvolutiva(230)
    .build

  print(9)
  val especieLapras = fabricaDeEspecies.setTipos(principal = Agua, secundario = Dragon)
    .setIncrementos(incPeso = 8, incFuerza = 10, incEnergiaMax = 80, incVelocidad = 9)
    .setPesoMaximoSaludable(100)
    .setResistenciaEvolutiva(350)
    .build
  print(10)
  val especieBulbasaur = fabricaDeEspecies.setTipos(principal = Planta, secundario = Bicho)
    .setIncrementos(incPeso = 3, incFuerza = 6, incEnergiaMax = 80, incVelocidad = 1)
    .setPesoMaximoSaludable(80)
    .setResistenciaEvolutiva(140)
    .build
  print(11)
  val especieGyarados = fabricaDeEspecies.setTipos(principal = Dragon, secundario = Bicho)
    .setIncrementos(incPeso = 9, incFuerza = 5, incEnergiaMax = 80, incVelocidad = 8)
    .setPesoMaximoSaludable(75)
    .setResistenciaEvolutiva(280)
    .build

  print(12)
  val especieHitmonchan = fabricaDeEspecies.setTipos(principal = Pelea, secundario = Normal)
    .setIncrementos(incPeso = 10, incFuerza = 5, incEnergiaMax = 80, incVelocidad = 3)
    .setPesoMaximoSaludable(95)
    .setResistenciaEvolutiva(450)
    .build
  print(13)
  val especieHunter = fabricaDeEspecies.setTipos(principal = Fantasma, secundario = Normal)
    .setIncrementos(incPeso = 12, incFuerza = 2, incEnergiaMax = 80, incVelocidad = 15)
    .setPesoMaximoSaludable(65)
    .setResistenciaEvolutiva(550)
    .build
  print(14)
  val especieVoltorb = fabricaDeEspecies.setTipos(principal = Electrico)
    .setIncrementos(incPeso = 7, incFuerza = 5, incEnergiaMax = 80, incVelocidad = 6)
    .setPesoMaximoSaludable(15)
    .setResistenciaEvolutiva(83)
    .build

  /*
   * Pokemones
   */
  print("pikachu")
  print(especiePikachu.incrementoEnergiaMax)
  val pikachu = fabricaDePokes.setEstado(Bueno)
    .setEspecie(especiePikachu)
    .setAtaques(List(impactrueno, embestida))
    .setNivel(1)
    .setExperiencia(100)
    .setGenero(Macho)
    .setEnergia(30)
    .build
    print(pikachu.peso)
  print("charmander")
  val charmander = fabricaDePokes.setEstado(Bueno)
    .setEspecie(especieCharmander)
    .setAtaques(List(embestida))
    .setNivel(1)
    .setExperiencia(150)
    .setGenero(Hembra)
    .setEnergia(75)
    .build
  print("squirtle")
  val squirtle = fabricaDePokes.setEstado(Dormido(3))
    .setEspecie(especieSquirtle)
    .setAtaques(List())
    .setNivel(7)
    .setExperiencia(25000)
    .setGenero(Hembra)
    .setEnergia(500)
    .build
  print("lapras")
  val lapras = fabricaDePokes.setEstado(Bueno)
    .setEspecie(especieLapras)
    .setAtaques(List())
    .setNivel(7)
    .setExperiencia(40000)
    .setGenero(Macho)
    .setEnergia(500)
    .build
  print("bulbasaur")
  val bulbasaur = fabricaDePokes.setEstado(Ko)
    .setEspecie(especieBulbasaur)
    .setAtaques(List())
    .setNivel(6)
    .setExperiencia(8000)
    .setGenero(Macho)
    .setEnergia(400)
    .build
  print("gyarados")
  val gyarados = fabricaDePokes.setEstado(Envenenado)
    .setEspecie(especieGyarados)
    .setAtaques(List(llama))
    .setNivel(5)
    .setExperiencia(8000)
    .setGenero(Macho)
    .setEnergia(400)
    .build
  print("hitmonchan")
  val hitmonchan = fabricaDePokes.setEstado(Bueno)
    .setEspecie(especieHitmonchan)
    .setAtaques(List())
    .setNivel(1)
    .setExperiencia(200)
    .setGenero(Macho)
    .setEnergia(60)
    .build
  print("hunter")
  val hunter = fabricaDePokes.setEstado(Bueno)
    .setEspecie(especieHunter)
    .setAtaques(List())
    .setNivel(1)
    .setExperiencia(200)
    .setGenero(Hembra)
    .setEnergia(75)
    .build
  print("voltorb")
  val voltorb = fabricaDePokes.setEstado(Bueno)
    .setEspecie(especieVoltorb)
    .setAtaques(List())
    .setNivel(1)
    .setExperiencia(50)
    .setGenero(Hembra)
    .setEnergia(75)
    .build

  /*
   * Algunas rutinas
   * Rutina(nombre, actividades)
   */

  val rutinaNado = new Rutina("natación_tranca", List[Actividad](Nadar(1), Nadar(1), Nadar(1), Nadar(1)))
  val rutinaPhelps = new Rutina("natación_pro", List[Actividad](Nadar(100), Nadar(100), Nadar(100), Nadar(100)))
  val rutinaPocionado = new Rutina("pocionado", List[Actividad](UsarPocion, UsarPocion, UsarPocion, UsarPocion))
  val rutinaIntercambio = new Rutina("intercambiado", List[Actividad](FingirIntercambio, FingirIntercambio, FingirIntercambio))
}