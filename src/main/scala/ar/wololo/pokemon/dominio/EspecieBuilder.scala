package ar.wololo.pokemon.dominio

import ar.wololo.pokemon.dominio._

/**
 * @author ivan
 */

case class IncrementosPorNivelBuilderException(msg: String) extends Exception(msg)
case class TipoDeEspecieBuilderException(msg: String) extends Exception(msg)
case class PesoMaxSaludableBuilderException(msg: String) extends Exception(msg)
case class ResistenciaEvolutivaBuilderException(msg: String) extends Exception(msg)
case class CondicionEvolutivaBuilderException(msg: String) extends Exception(msg)
case class EspecieEvolucionBuilderException(msg: String) extends Exception(msg)
case class EspecieCreacionBuilderException(msg: String) extends Exception(msg)

case class EspecieBuilder(var tipoPrincipal: Tipo = null,
    var tipoSecundario: Option[Tipo] = None,
    var incrementoPeso: Int = 0,
    var incrementoFuerza: Int = 0,
    var incrementoEnergiaMax: Int = 0,
    var incrementoVelocidad: Int = 0,
    var pesoMaximoSaludable: Int = 0,
    var resistenciaEvolutiva: Int = 0,
    var condicionEvolutiva: Option[CondicionEvolutiva] = None,
    var especieEvolucion: Option[Especie] = None) {

  def setTipos(principal: Tipo, secundario: Tipo = null): EspecieBuilder = {
    if (principal.equals(secundario))
      throw new TipoDeEspecieBuilderException("El tipo principal es el mismo que el secundario.")
    else
      copy(tipoPrincipal = principal, tipoSecundario = Some(secundario))
  }

  def setIncrementos(incPeso: Int, incFuerza: Int, incEnergiaMax: Int, incVelocidad: Int): EspecieBuilder = {
    if (incPeso > 0 && incFuerza > 0 && incEnergiaMax > 0 && incVelocidad > 0)
      copy(incrementoPeso = incPeso, incrementoFuerza = incFuerza, incrementoEnergiaMax = incEnergiaMax, incrementoVelocidad = incVelocidad)
    else
      throw new IncrementosPorNivelBuilderException("Algunos incrementos son iguales o menores a 0.")
  }

  def setPesoMaximoSaludable(pesoMax: Int): EspecieBuilder = {
    if (pesoMax > 0)
      copy(pesoMaximoSaludable = pesoMax)
    else
      throw new PesoMaxSaludableBuilderException("El peso máximo saludable debe ser mayor a 0")
  }

  def setResistenciaEvolutiva(resEvol: Int): EspecieBuilder = {
    if (resEvol > 0)
      copy(resistenciaEvolutiva = resEvol)
    else
      throw new ResistenciaEvolutivaBuilderException("La resistencia evolutiva debe ser mayor a 0")
  }

  def setCondicionEvolutiva(condicion: CondicionEvolutiva): EspecieBuilder = {
    condicion match {
      case c: SubirDeNivel => {
        if (c.nivelParaEvolucionar > 0)
          copy(condicionEvolutiva = Some(condicion))
        else
          throw new CondicionEvolutivaBuilderException("Subir de nivel debe tener un nivel mayor a 0.")
      }
      case _ => copy(condicionEvolutiva = Some(condicion))
    }
  }

  def setEspecieEvolucion(especie: Especie): EspecieBuilder = {
    if (condicionEvolutiva.isDefined) copy(especieEvolucion = Some(especie))
    else
      throw new EspecieEvolucionBuilderException("Es necesario asignar una condicion de evolucion antes de especificar la especie de evolucion")
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
      throw new EspecieCreacionBuilderException("Parámetros de creación de especie inválidos")

  }

}