package ar.wololo.pokemon.dominio

/**
 * @author ivan
 */

case class EnergiaFactoryException(mensaje: String) extends Exception(mensaje)
case class NivelFactoryException(mensaje: String) extends Exception(mensaje)
case class ExperienciaFactoryException(mensaje: String) extends Exception(mensaje)
case class AtaqueFactoryException(mensaje: String) extends Exception(mensaje)
case class EspecieFactoryException(mensaje: String) extends Exception(mensaje)
case class BuildFactoryException(mensaje: String) extends Exception(mensaje)

/*
 * Hice que los métodos retornen copias de PokemonFactory para
 * la concatenación de mensajes y que sea fácil armar pokemones
 */

case class PokemonFactory(var estado: EstadoPokemon = null,
    var ataques: List[Ataque] = List(),
    var nivel: Int = 0,
    var experiencia: Long = 0,
    var genero: Genero = null,
    var energia: Int = 0,
    var especie: Especie = null) {

  def getFuerza: Int = if (nivel > 0) nivel * especie.incrementoFuerza else throw NivelFactoryException("Definir nivel mayor a 0 antes de asignar fuerza.")
  def getPeso: Int = if (nivel > 0) Math.min(nivel * especie.incrementoPeso, especie.pesoMaximoSaludable) else throw NivelFactoryException("Definir nivel mayor a 0 antes de asignar peso.")
  def getEnergiaMax: Int = if (nivel > 0) nivel * especie.incrementoEnergiaMax else throw NivelFactoryException("Definir nivel mayor a 0 antes de asignar energia.")
  def getVelocidad: Int = if (nivel > 0) nivel * especie.incrementoVelocidad else throw NivelFactoryException("Definir nivel mayor a 0 antes de asignar velocidad.")

  def setGenero(identidad: Genero): PokemonFactory = copy(genero = identidad)
  def setEstado(unEstado: EstadoPokemon): PokemonFactory = copy(estado = unEstado)

  def setEnergia(joules: Int): PokemonFactory = {
    if (getEnergiaMax > 0)
      if (joules > 0 && joules <= getEnergiaMax)
        copy(energia = joules)
      else {
        print("Energia max es: " + getEnergiaMax)
        print("Incremento energia max es: " + especie.incrementoEnergiaMax)
        throw new EnergiaFactoryException("Energía no válida => " + joules + " . Debe estar entre 0 y " + getEnergiaMax)
      }
    else
      throw new EnergiaFactoryException("La energía máxima es igual o menor a 0. Revisar especie.")
  }

  def setEspecie(unaEspecie: Especie): PokemonFactory = {
    if (unaEspecie.pesoMaximoSaludable > 0 &&
      unaEspecie.incrementoEnergiaMax > 0 &&
      unaEspecie.incrementoFuerza > 0 &&
      unaEspecie.incrementoPeso > 0 &&
      unaEspecie.incrementoVelocidad > 0 &&
      unaEspecie.resistenciaEvolutiva > 0)
      if(unaEspecie.condicionEvolutiva == null)
        copy(especie = unaEspecie.copy(condicionEvolutiva = NoEvoluciona))
      else  
        copy(especie = unaEspecie)
    else
      throw new EspecieFactoryException("Especie con parámetros inválidos")
  }

  def setNivel(unNivel: Int): PokemonFactory = {
    especie.condicionEvolutiva match {
      case c: SubirDeNivel =>
        if (unNivel < c.nivelParaEvolucionar)
          copy(nivel = unNivel)
        else
          throw NivelFactoryException("El nivel debe ser menor al nivel especificado para evolucionar.")
      case _ =>
        if (unNivel > 0)
          copy(nivel = unNivel)
        else
          throw NivelFactoryException("El nivel debe ser mayor a 0")
    }

  }

  def setExperiencia(ptsDeExperiencia: Long): PokemonFactory = {
    nivel match {
      case n if n == 0 => throw new NivelFactoryException("Nivel aún no asignado")
      case n if n > 0 => {
        val expSgteNivel = especie.experienciaParaNivel(nivel + 1)
        val expNivelAct = especie.experienciaParaNivel(nivel)
        ptsDeExperiencia match {
          case exp if exp >= expSgteNivel => throw new ExperienciaFactoryException("Experiencia mayor a nivel asignado. Debe ser menor a " + expSgteNivel)
          case exp if exp >= expNivelAct => copy(experiencia = exp)
          case _ => throw new ExperienciaFactoryException("Experiencia menor a nivel asignado. Debe ser mayor a " + expNivelAct)
        }
      }
    }
  }

  def setAtaques(unosAtaques: List[Ataque]): PokemonFactory = {
    if (unosAtaques.forall { atk => atk.tipo == especie.tipoPrincipal || atk.tipo == especie.tipoSecundario || atk.tipo == Normal })
      copy(ataques = unosAtaques)
    else
      throw new AtaqueFactoryException("Hay ataques que no puede aprender el pokemón")
  }

  def build: Pokemon = {
    if (!(estado == null) &&
      nivel > 0 &&
      experiencia >= especie.experienciaParaNivel(nivel) &&
      experiencia < especie.experienciaParaNivel(nivel + 1) &&
      !genero.eq(null) &&
      energia > 0 &&
      !(especie == null) &&
      (ataques.isEmpty ||
        ataques.forall { atk => atk.tipo == especie.tipoPrincipal || atk.tipo == especie.tipoSecundario || atk.tipo == Normal }))

      new Pokemon(estado,
        ataques,
        nivel,
        experiencia,
        genero,
        energia,
        energiaMax = getEnergiaMax,
        peso = getPeso,
        fuerza = getFuerza,
        velocidad = getVelocidad,
        especie)
    else
      throw new BuildFactoryException("Faltan parámetros para la creación del pokemón")
  }

}
