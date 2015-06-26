package ar.wololo.pokemon.dominio

/**
 * @author ivan
 */

case class EnergiaBuilderException(mensaje: String) extends Exception(mensaje)
case class NivelBuilderException(mensaje: String) extends Exception(mensaje)
case class ExperienciaBuilderException(mensaje: String) extends Exception(mensaje)
case class AtaqueBuilderException(mensaje: String) extends Exception(mensaje)
case class EspecieBuilderException(mensaje: String) extends Exception(mensaje)
case class BuilderException(mensaje: String) extends Exception(mensaje)

/*
 * Hice que los métodos retornen copias de PokemonFactory para
 * la concatenación de mensajes y que sea fácil armar pokemones
 */

case class PokemonBuilder(var estado: EstadoPokemon = null,
    var ataques: List[(Ataque,Int,Int)] = List(),
    var nivel: Int = 0,
    var experiencia: Long = 0,
    var genero: Genero = null,
    var energia: Int = 0,
    var especie: Especie = null) {

  def getFuerza: Int = if (nivel > 0) nivel * especie.incrementoFuerza else throw NivelBuilderException("Definir nivel mayor a 0 antes de asignar fuerza.")
  def getPeso: Int = if (nivel > 0) Math.min(nivel * especie.incrementoPeso, especie.pesoMaximoSaludable) else throw NivelBuilderException("Definir nivel mayor a 0 antes de asignar peso.")
  def getEnergiaMax: Int = if (nivel > 0) nivel * especie.incrementoEnergiaMax else throw NivelBuilderException("Definir nivel mayor a 0 antes de asignar energia.")
  def getVelocidad: Int = if (nivel > 0) nivel * especie.incrementoVelocidad else throw NivelBuilderException("Definir nivel mayor a 0 antes de asignar velocidad.")

  def setGenero(identidad: Genero): PokemonBuilder = copy(genero = identidad)
  def setEstado(unEstado: EstadoPokemon): PokemonBuilder = copy(estado = unEstado)

  def setEnergia(joules: Int): PokemonBuilder = {
    if (getEnergiaMax > 0)
      if (joules > 0 && joules <= getEnergiaMax)
        copy(energia = joules)
      else {
        print("Energia max es: " + getEnergiaMax)
        print("Incremento energia max es: " + especie.incrementoEnergiaMax)
        throw new EnergiaBuilderException("Energía no válida => " + joules + " . Debe estar entre 0 y " + getEnergiaMax)
      }
    else
      throw new EnergiaBuilderException("La energía máxima es igual o menor a 0. Revisar especie.")
  }

  def setEspecie(unaEspecie: Especie): PokemonBuilder = {
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
      throw new EspecieBuilderException("Especie con parámetros inválidos")
  }

  def setNivel(unNivel: Int): PokemonBuilder = {
    especie.condicionEvolutiva match {
      case c: SubirDeNivel =>
        if (unNivel < c.nivelParaEvolucionar)
          copy(nivel = unNivel)
        else
          throw NivelBuilderException("El nivel debe ser menor al nivel especificado para evolucionar.")
      case _ =>
        if (unNivel > 0)
          copy(nivel = unNivel)
        else
          throw NivelBuilderException("El nivel debe ser mayor a 0")
    }

  }

  def setExperiencia(ptsDeExperiencia: Long): PokemonBuilder = {
    nivel match {
      case n if n == 0 => throw new NivelBuilderException("Nivel aún no asignado")
      case n if n > 0 => {
        val expSgteNivel = especie.experienciaParaNivel(nivel + 1)
        val expNivelAct = especie.experienciaParaNivel(nivel)
        ptsDeExperiencia match {
          case exp if exp >= expSgteNivel => throw new ExperienciaBuilderException("Experiencia mayor a nivel asignado. Debe ser menor a " + expSgteNivel)
          case exp if exp >= expNivelAct => copy(experiencia = exp)
          case _ => throw new ExperienciaBuilderException("Experiencia menor a nivel asignado. Debe ser mayor a " + expNivelAct)
        }
      }
    }
  }

  def setAtaques(unosAtaques: List[(Ataque,Int,Int)]): PokemonBuilder = {
    if (unosAtaques.forall {case (atk :Ataque,_,_) => atk.tipo == especie.tipoPrincipal || atk.tipo == especie.tipoSecundario || atk.tipo == Normal })
      copy(ataques = unosAtaques)
    else
      throw new AtaqueBuilderException("Hay ataques que no puede aprender el pokemón")
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
        ataques.forall {case (atk : Ataque,_,_) => atk.tipo == especie.tipoPrincipal || atk.tipo == especie.tipoSecundario || atk.tipo == Normal }))

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
      throw new BuilderException("Faltan parámetros para la creación del pokemón")
  }

}
