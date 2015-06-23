package ar.wololo.pokemon.dominio

/**
 * @author ivan
 */

case class PesoFactoryException(mensaje: String) extends Exception(mensaje)
case class VelocidadFactoryException(mensaje: String) extends Exception(mensaje)
case class FuerzaFactoryException(mensaje: String) extends Exception(mensaje)
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
    var experiencia: Int = 0,
    var genero: Genero = null,
    var energia: Int = 0,
    var energiaMax: Int = 0,
    var peso: Int = 0,
    var fuerza: Int = 0,
    var velocidad: Int = 0,
    var especie: Especie = null) {

  def setEnergiaMax(joulesMax: Int): PokemonFactory = {
    if (joulesMax > 0)
      copy(energiaMax = joulesMax)
    else
      throw new EnergiaFactoryException("Energía máxima igual o menor a 0")
  }

  def setEnergia(joules: Int): PokemonFactory = {
    if (energiaMax > 0)
      if (joules > 0 && joules <= energiaMax)
        copy(energia = joules)
      else
        throw new EnergiaFactoryException("Energía no válida")
    else
      throw new EnergiaFactoryException("Primero debe definirse la energía máxima.")
  }

  def setPeso(kilos: Int): PokemonFactory = {
    if (especie.pesoMaximoSaludable > 0)
      if (kilos > 0 && kilos <= especie.pesoMaximoSaludable)
        copy(peso = kilos)
      else {
        throw new PesoFactoryException("Peso no válido")
      }
    else
      throw new PesoFactoryException("Primero debe definirse el peso máximo en la especie")
  }

  def setVelocidad(velocity: Int): PokemonFactory = {
    if (velocity >= 0 && velocity <= 100)
      copy(velocidad = velocity)
    else
      throw new VelocidadFactoryException("Velocidad no válida")
  }

  def setFuerza(newtons: Int): PokemonFactory = {
    if (newtons >= 0 && newtons <= 100)
      copy(fuerza = newtons)
    else
      throw new FuerzaFactoryException("Fuerza no válida")
  }

  def setGenero(identidad: Genero): PokemonFactory = copy(genero = identidad)
  def setEstado(unEstado: EstadoPokemon): PokemonFactory = copy(estado = unEstado)

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
    if (unNivel > 0)
      copy(nivel = unNivel)
    else
      throw new NivelFactoryException("Nivel menor o igual a 0")
  }

  def setExperiencia(ptsDeExperiencia: Int): PokemonFactory = {
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
    if (!estado.eq(null) &&
      nivel > 0 &&
      experiencia >= especie.experienciaParaNivel(nivel) &&
      experiencia < especie.experienciaParaNivel(nivel + 1) &&
      !genero.eq(null) &&
      energia > 0 &&
      energiaMax > 0 &&
      peso > 0 &&
      fuerza > 0 &&
      velocidad > 0 &&
      !especie.eq(null) &&
      (ataques.isEmpty ||
        ataques.forall { atk => atk.tipo == especie.tipoPrincipal || atk.tipo == especie.tipoSecundario || atk.tipo == Normal }))
      new Pokemon(estado, ataques, nivel, experiencia, genero, energia, energiaMax, peso, fuerza, velocidad, especie)
    else
      throw new BuildFactoryException("Faltan parámetros para la creación del pokemón")
  }

}