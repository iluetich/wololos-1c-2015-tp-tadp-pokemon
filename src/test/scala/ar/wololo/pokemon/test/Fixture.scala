package ar.wololo.pokemon.test
import ar.wololo.pokemon.dominio._

object Fixt{
      val impactrueno = new Ataque("Impactrueno",(Pokemon => Pokemon),Electrico,1,20)
      val embestida = new Ataque("Embestida",(Pokemon => Pokemon.copy(estado = Dormido(3))),Normal,7,10)
      val llama = new Ataque("Llama",(Pokemon => Pokemon),Dragon,15,30)
      
      val especiePikachu = new Especie(Electrico,Normal,2,3,5,80,40,200,SubirDeNivel(100),null)
      val especieCharmander = new Especie(Fuego,Normal,4,1,6,80,30,250,UsarUnaPiedra,null)
      val especieSquirtle = new Especie(Agua,Normal,1,2,4,80,45,230,SubirDeNivel(100),null)
      val especieLapras = new Especie(Agua,Dragon,10,9,8,80,7,350,Intercambiar,null)
      val especieBulbasaur = new Especie(Planta,Bicho,6,1,3,80,80,140,SubirDeNivel(100),null)
      val especieGyarados = new Especie(Dragon,Bicho,5,8,9,80,75,280,UsarUnaPiedraLunar,null) //LOCO GYARADOS ESTABA BIEN ESCRITO
      val especieHitmonchan = new Especie(Pelea,Normal,5,3,10,80,95,450,Intercambiar,null)
      val especieHunter = new Especie(Fantasma,Normal,2,15,12,80,65,550,SubirDeNivel(100),null)
      val especieVoltorb = new Especie(Electrico,Tierra,5,6,7,80,15,83,SubirDeNivel(100),null)
      
      //implementar especies evolucion
      
      
      val pikachu = new Pokemon(Bueno, List(impactrueno , embestida),1, 0, Macho, 30, 1000, 5, 90, 20,
          especiePikachu)
      
      val charmander = new Pokemon(Bueno, List(embestida),1, 0, Hembra, 990, 1000, 5, 80, 50,
          especieCharmander)
      
      val scuartul = new Pokemon(Dormido(3), List[Ataque](),7, 80, Hembra, 500, 800, 8, 90, 40,
          especieSquirtle)
      
      val lapras = new Pokemon(Bueno, List[Ataque](),7, 0, Macho, 500, 800, 8, 20, 50,
          especieLapras)
      
      val bulvasor = new Pokemon(Ko, List[Ataque](),6, 0, Macho, 400, 1200, 9, 30, 20,
          especieBulbasaur)
      
      val gyarados = new Pokemon(Envenenado, List(llama),5, 0, Macho, 400, 500, 9, 80, 60,
          especieGyarados)
      
      val hitmonchan = new Pokemon(Bueno, List[Ataque](),1, 0, Macho, 450, 600, 9, 20, 80,
          especieHitmonchan)
      
      val hunter = new Pokemon(Bueno, List[Ataque](),1, 0, Hembra, 450, 600, 15, 20, 80,
          especieHunter)
      
      val voltod = new Pokemon(Bueno, List(),1, 0, Hembra, 60, 800, 5, 95, 40,
          especieVoltorb)
}