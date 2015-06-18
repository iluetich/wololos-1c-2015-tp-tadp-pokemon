package ar.wololo.pokemon.dominio

import ar.wololo.pokemon.dominio._


/**
 * @author ivan
 */


case class IncrementosPorNivelFactoryException(msg: String) extends Exception(msg)
case class TipoDeEspecieFactoryException(msg: String) extends Exception(msg)
case class PesoMaxSaludableFactoryException(msg: String) extends Exception(msg)
case class ResistenciaEvolutivaFactoryException(msg: String) extends Exception(msg)
case class CondicionEvolutivaFactoryException(msg: String) extends Exception(msg)
case class EspecieEvolucionFactoryException(msg: String) extends Exception(msg)
case class EspecieCreacionFactoryException(msg: String) extends Exception(msg)


case class EspecieFactory(var tipoPrincipal: Tipo = null,
    var tipoSecundario: Tipo = null,
    var incrementoPeso: Int = 0,
    var incrementoFuerza: Int = 0,
    var incrementoEnergiaMax: Int = 0,
    var incrementoVelocidad: Int = 0,
    var pesoMaximoSaludable: Int = 0,
    var resistenciaEvolutiva: Int = 0,
    var condicionEvolutiva: CondicionEvolutiva = null,
    var especieEvolucion: Especie = null) {

  def setTipos(principal: Tipo, secundario: Tipo = null): EspecieFactory = {
    if (principal.equals(secundario))
      throw new TipoDeEspecieFactoryException("El tipo principal es el mismo que el secundario.")
    else
      copy(tipoPrincipal = principal, tipoSecundario = secundario)
  }

  def setIncrementos(incPeso: Int, incFuerza: Int, incEnergiaMax: Int, incVelocidad: Int): EspecieFactory = {
    if (incPeso > 0 && incFuerza > 0 && incEnergiaMax > 0 && incVelocidad > 0)
      copy(incrementoPeso = incPeso, incrementoFuerza = incFuerza, incrementoEnergiaMax = incEnergiaMax, incrementoVelocidad = incVelocidad)
    else
      throw new IncrementosPorNivelFactoryException("Algunos incrementos son iguales o menores a 0.")
  }

  def setPesoMaximoSaludable(pesoMax: Int): EspecieFactory = {
    if (pesoMax > 0)
      copy(pesoMaximoSaludable = pesoMax)
    else
      throw new PesoMaxSaludableFactoryException("El peso máximo saludable debe ser mayor a 0")
  }

  def setResistenciaEvolutiva(resEvol: Int): EspecieFactory = {
    if (resEvol > 0)
      copy(resistenciaEvolutiva = resEvol)
    else
      throw new ResistenciaEvolutivaFactoryException("La resistencia evolutiva debe ser mayor a 0")
  }

  def setCondicionEvolutiva(condicion: CondicionEvolutiva): EspecieFactory = {
    condicion match {
      case c: SubirDeNivel => {
        if (c.nivelParaEvolucionar > 0)
          copy(condicionEvolutiva = condicion)
        else
          throw new CondicionEvolutivaFactoryException("Subir de nivel debe tener un nivel mayor a 0.")
      }
      case _ => copy(condicionEvolutiva = condicion)
    }
  }

  def setEspecieEvolucion(especie: Especie): EspecieFactory = {
    if (!(condicionEvolutiva == null))
      copy(especieEvolucion = especie)
    else
      throw new EspecieEvolucionFactoryException("Es necesario asignar una condicion de evolucion antes de especificar la especie de evolucion")
  }

  def build: Especie = {
    if (!(tipoPrincipal == null) &&
      incrementoPeso > 0 &&
      incrementoFuerza > 0 &&
      incrementoEnergiaMax > 0 &&
      incrementoVelocidad > 0 &&
      pesoMaximoSaludable > 0 &&
      resistenciaEvolutiva > 0)
      new Especie(tipoPrincipal,
        tipoSecundario,
        incrementoFuerza,
        incrementoVelocidad,
        incrementoPeso,
        incrementoEnergiaMax,
        pesoMaximoSaludable,
        resistenciaEvolutiva,
        condicionEvolutiva,
        especieEvolucion)
    else
      throw new EspecieCreacionFactoryException("Parámetros de creación de especie inválidos")

  }

}