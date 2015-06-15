package ar.wololo.pokemon.test
import ar.wololo.pokemon.dominio._

object Fixt {

  val fabricaDePokes = new PokemonFactory

  // Ataque(nombre, efecto, tipo, puntosDeAtaque, puntosDeAtaqueMax)
  val impactrueno = new Ataque("Impactrueno", (Pokemon => Pokemon), Electrico, 1, 20)
  val embestida = new Ataque("Embestida", (Pokemon => Pokemon.copy(estado = Dormido(3))), Normal, 7, 10)
  val llama = new Ataque("Llama", (Pokemon => Pokemon), Dragon, 15, 30)

  // Especie(tipoPrincipal, tipoSecundario, incFuerza, incVelocidad, incPeso, incEnergiaMax, pesoMaxSaludable, resistEvol, condicionEvol, especieEvol)

  val especieRaychu = new Especie(Electrico, Normal, 3, 4, 6, 100, 70, 300)
  val especieCharizard = new Especie(Fuego, Volador, 10, 5, 9, 200, 90, 600)
  val especieCharmeleon = new Especie(Fuego, Normal, 5, 2, 7, 100, 50, 300, Intercambiar, especieCharizard)
  val especiePikachu = new Especie(Electrico, Normal, 2, 3, 5, 80, 40, 200, SubirDeNivel(100), especieRaychu)
  val especieCharmander = new Especie(Fuego, Normal, 4, 1, 6, 80, 30, 250, UsarUnaPiedra, especieCharmeleon)
  val especieSquirtle = new Especie(Agua, Normal, 1, 2, 4, 80, 45, 230)
  val especieLapras = new Especie(Agua, Dragon, 10, 9, 8, 80, 7, 350)
  val especieBulbasaur = new Especie(Planta, Bicho, 6, 1, 3, 80, 80, 140)
  val especieGyarados = new Especie(Dragon, Bicho, 5, 8, 9, 80, 75, 280) //LOCO GYARADOS ESTABA BIEN ESCRITO
  val especieHitmonchan = new Especie(Pelea, Normal, 5, 3, 10, 80, 95, 450)
  val especieHunter = new Especie(Fantasma, Normal, 2, 15, 12, 80, 65, 550)
  val especieVoltorb = new Especie(Electrico, Tierra, 5, 6, 7, 80, 15, 83)

  /*
   * Pokemones
   */
  val pikachu = fabricaDePokes.setEstado(Bueno)
    .setEspecie(especiePikachu)
    .setAtaques(List(impactrueno, embestida))
    .setNivel(1)
    .setExperiencia(0)
    .setGenero(Macho)
    .setEnergiaMax(1000)
    .setEnergia(30)
    .setPeso(5)
    .setFuerza(90)
    .setVelocidad(20)
    .build

  val charmander = fabricaDePokes.setEstado(Bueno)
    .setEspecie(especieCharmander)
    .setAtaques(List(embestida))
    .setNivel(1)
    .setExperiencia(0)
    .setGenero(Hembra)
    .setEnergiaMax(1000)
    .setEnergia(990)
    .setPeso(5)
    .setFuerza(80)
    .setVelocidad(50)
    .build

//  val pikachu = new Pokemon(Bueno, List(impactrueno, embestida), 1, 0, Macho, 30, 1000, 5, 90, 20,
//    especiePikachu)
//
//  val charmander = new Pokemon(Bueno, List(embestida), 1, 0, Hembra, 990, 1000, 5, 80, 50,
//    especieCharmander)

  val scuartul = new Pokemon(Dormido(3), List[Ataque](), 7, 80, Hembra, 500, 800, 8, 90, 40,
    especieSquirtle)

  val lapras = new Pokemon(Bueno, List[Ataque](), 7, 0, Macho, 500, 800, 8, 20, 50,
    especieLapras)

  val bulvasor = new Pokemon(Ko, List[Ataque](), 6, 0, Macho, 400, 1200, 9, 30, 20,
    especieBulbasaur)

  val gyarados = new Pokemon(Envenenado, List(llama), 5, 0, Macho, 400, 500, 9, 80, 60,
    especieGyarados)

  val hitmonchan = new Pokemon(Bueno, List[Ataque](), 1, 0, Macho, 450, 600, 9, 20, 80,
    especieHitmonchan)

  val hunter = new Pokemon(Bueno, List[Ataque](), 1, 0, Hembra, 450, 600, 15, 20, 80,
    especieHunter)

  val voltod = new Pokemon(Bueno, List(), 1, 0, Hembra, 60, 800, 5, 95, 40,
    especieVoltorb)

  /*
   * Algunas rutinas
   * Rutina(actividades)
   */

  val rutinaNado = new Rutina(List[Actividad](Nadar(1), Nadar(1), Nadar(1), Nadar(1)))
  val rutinaPhelps = new Rutina(List[Actividad](Nadar(100), Nadar(100), Nadar(100), Nadar(100)))
  val rutinaPocionado = new Rutina(List[Actividad](UsarPocion, UsarPocion, UsarPocion, UsarPocion))
  val rutinaIntercambio = new Rutina(List[Actividad](FingirIntercambio, FingirIntercambio, FingirIntercambio))
}