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
    var ataques: List[(Ataque, Int, Int)] = List(),
    var experiencia: Long = 0,
    var genero: Genero = null,
    var energia: Int = 0,
    var especie: Especie = null) {

  def getFuerza: Int = especie.incrementoFuerza
  def getPeso: Int = Math.min(especie.incrementoPeso, especie.pesoMaximoSaludable)
  def getEnergiaMax: Int = especie.incrementoEnergiaMax
  def getVelocidad: Int = especie.incrementoVelocidad

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
      if (unaEspecie.condicionEvolutiva.isEmpty)
        copy(especie = unaEspecie.copy(condicionEvolutiva = Some(NoEvoluciona)))
      else
        copy(especie = unaEspecie)
    else
      throw new EspecieBuilderException("Especie con parámetros inválidos")
  }

  def setExperiencia(ptsDeExperiencia: Long): PokemonBuilder = {
    ptsDeExperiencia match {
      case exp if exp < 0 => throw new ExperienciaBuilderException("Experiencia negativa => " + ptsDeExperiencia)
      case exp if exp >= 0 => copy(experiencia = ptsDeExperiencia)
    }
  }

  private def ataqueAprendible(ataque: Ataque) = List(Normal, especie.tipoPrincipal, especie.tipoSecundario).contains(ataque.tipo)

  def setAtaques(unosAtaques: List[(Ataque, Int, Int)]): PokemonBuilder = {
    if (unosAtaques.forall { tuplaAtaque => ataqueAprendible(tuplaAtaque._1) })
      copy(ataques = unosAtaques)
    else
      throw new AtaqueBuilderException("Hay ataques que no puede aprender el pokemón")
  }

  def build: Pokemon = {
    if (!(estado == null) &&
      !genero.eq(null) &&
      energia > 0 &&
      !(especie == null) &&
      (ataques.isEmpty ||
        ataques.forall { tuplaAtaque => ataqueAprendible(tuplaAtaque._1) }))

      new Pokemon(estado,
        ataques,
        experiencia,
        genero,
        energia,
        energiaMaxBase = 0,
        pesoBase = 0,
        fuerzaBase = 0,
        velocidadBase = 0,
        especie)
    else
      throw new BuilderException("Faltan parámetros para la creación del pokemón")
  }

}
