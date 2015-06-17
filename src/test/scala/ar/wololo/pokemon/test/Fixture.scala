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
  val especieBlastoise = new Especie(Agua, Agua, 10, 10, 10, 300, 500, 550)
  val especiePikachu = new Especie(Electrico, Normal, 2, 3, 5, 80, 40, 200, SubirDeNivel(100), especieRaychu)
  val especieCharmeleon = new Especie(Fuego, Normal, 5, 2, 7, 100, 50, 300, Intercambiar, especieCharizard)
  val especieCharmander = new Especie(Fuego, Normal, 4, 1, 6, 80, 30, 250, UsarUnaPiedra, especieCharmeleon)
  val especieWarturtle = new Especie(Agua, Agua, 6, 5, 4, 80, 100, 400, SubirDeNivel(30), especieBlastoise)
  val especieSquirtle = new Especie(Agua, Normal, 1, 2, 4, 80, 45, 230,SubirDeNivel(200))
  val especieLapras = new Especie(Agua, Dragon, 10, 9, 8, 80, 100, 350,UsarUnaPiedraLunar)//necesito testear que un pok utilice una piedra
  val especieBulbasaur = new Especie(Planta, Bicho, 6, 1, 3, 80, 80, 140,SubirDeNivel(150))
  val especieGyarados = new Especie(Dragon, Bicho, 5, 8, 9, 80, 75, 280,UsarUnaPiedra) //LOCO GYARADOS ESTABA BIEN ESCRITO
  val especieHitmonchan = new Especie(Pelea, Normal, 5, 3, 10, 80, 95, 450,Intercambiar)
  val especieHunter = new Especie(Fantasma, Normal, 2, 15, 12, 80, 65, 550)
  val especieVoltorb = new Especie(Electrico, Tierra, 5, 6, 7, 80, 15, 83,UsarUnaPiedra)

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

  val squirtle = fabricaDePokes.setEstado(Dormido(3))
    .setEspecie(especieSquirtle)
    .setAtaques(List())
    .setNivel(7)
    .setExperiencia(25000)
    .setGenero(Hembra)
    .setEnergiaMax(800)
    .setEnergia(500)
    .setPeso(8)
    .setFuerza(90)
    .setVelocidad(40)
    .build

  val lapras = fabricaDePokes.setEstado(Bueno)
    .setEspecie(especieLapras)
    .setAtaques(List())
    .setNivel(1)
    .setExperiencia(0)
    .setGenero(Macho)
    .setEnergiaMax(800)
    .setEnergia(500)
    .setPeso(8)
    .setFuerza(20)
    .setVelocidad(50)
    .build

  val bulbasaur = fabricaDePokes.setEstado(Ko)
    .setEspecie(especieBulbasaur)
    .setAtaques(List())
    .setNivel(6)
    .setExperiencia(8000)
    .setGenero(Macho)
    .setEnergiaMax(1200)
    .setEnergia(400)
    .setPeso(9)
    .setFuerza(30)
    .setVelocidad(20)
    .build

  val gyarados = fabricaDePokes.setEstado(Envenenado)
    .setEspecie(especieGyarados)
    .setAtaques(List(llama))
    .setNivel(1)
    .setExperiencia(0)
    .setGenero(Macho)
    .setEnergiaMax(500)
    .setEnergia(400)
    .setPeso(9)
    .setFuerza(80)
    .setVelocidad(60)
    .build

  val hitmonchan = fabricaDePokes.setEstado(Bueno)
    .setEspecie(especieHitmonchan)
    .setAtaques(List())
    .setNivel(1)
    .setExperiencia(0)
    .setGenero(Macho)
    .setEnergiaMax(600)
    .setEnergia(450)
    .setPeso(9)
    .setFuerza(20)
    .setVelocidad(80)
    .build

  val hunter = fabricaDePokes.setEstado(Bueno)
    .setEspecie(especieHunter)
    .setAtaques(List())
    .setNivel(1)
    .setExperiencia(0)
    .setGenero(Hembra)
    .setEnergiaMax(600)
    .setEnergia(450)
    .setPeso(15)
    .setFuerza(20)
    .setVelocidad(80)
    .build

  val voltorb = fabricaDePokes.setEstado(Bueno)
    .setEspecie(especieVoltorb)
    .setAtaques(List())
    .setNivel(1)
    .setExperiencia(50)
    .setGenero(Hembra)
    .setEnergiaMax(800)
    .setEnergia(60)
    .setPeso(5)
    .setFuerza(95)
    .setVelocidad(40)
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