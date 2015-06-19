package ar.wololo.pokemon.dominio

import scala.util.Try

case class Pokemon(
  val estado: EstadoPokemon,
  val listaAtaques: List[(Ataque,Int,Int)],//Ataque, puntos de ataque actual, puntos de ataque maximos
  val nivel: Int,
  val experiencia: Long,
  val genero: Genero,
  val energia: Int,
  val energiaMax: Int,
  val peso: Int,
  val fuerza: Int,
  val velocidad: Int,
  val especie: Especie) {

  val velocidadMax = 100 //constante de enunciado
  val fuerzaMax = 100 //constante de enunciado
  val tipoPrincipal: Tipo = especie.tipoPrincipal
  val tipoSecundario: Tipo = especie.tipoSecundario
  val pesoMaximoSaludable: Int = especie.pesoMaximoSaludable
  val condicionEvolutiva: CondicionEvolutiva = especie.condicionEvolutiva

  def aumentaExperiencia(cantidad: Long): Pokemon = especie.aumentaExperienciaDe(this, cantidad)
  def evolucionar: Pokemon = especie.evolucionarA(this)
  def realizarRutina(rutina: Rutina): Try[Pokemon] = rutina.esHechaPor(this)
  private def sufriEfectosSecundarios(ataque: Ataque): Pokemon = ataque.efecto(this)

  def aumentaExpEnBaseAGenero():Pokemon = genero.aumentaExperiencia(this)
  def fingiIntercambio():Pokemon = genero.fingiIntercambio(this)
  
  def modificaPeso(cantidad :Int):Pokemon = this.copy(peso = this.peso + cantidad).verificarParams()
  def modificaVelocidad(cantidad :Int):Pokemon = this.copy(velocidad = Math.min(this.velocidad + cantidad,this.velocidadMax)).verificarParams()
  def modificaEnergia(cantidad :Int):Pokemon = this.copy(energia = Math.min(this.energia + cantidad, this.energiaMax)).verificarParams()
  def modificaFuerza(cantidad :Int):Pokemon = this.copy(fuerza = Math.min(this.fuerza + cantidad, this.fuerzaMax)).verificarParams()
  def modificaListaAtaque(listaNueva : List[(Ataque,Int,Int)]):Pokemon = this.copy(listaAtaques = listaNueva)
  
  def cambiaAEstado(nuevoEstado :EstadoPokemon):Pokemon =  this.copy(estado = nuevoEstado)

  def evaluarEfectos(piedra : Piedra):Pokemon = condicionEvolutiva.evaluarEfectosPiedra(this, piedra)

  def verificarParams(): Pokemon = {
    if (energia < 0)
      throw NoPuedeEnergiaMenorACero(this)
    if (peso < 0 || peso > pesoMaximoSaludable)
      throw NoPuedePesoInvalido(this)
    if (fuerza < 0)
      throw NoPuedeFuerzaInvalida(this)
    if (velocidad < 0)
      throw NoPuedeVelocidadInvalida(this)
    this
  }

  private def descansar(): Pokemon = {
    val nuevaListaAtaque = this.listaAtaques.map{case (ataque : Ataque,pa :Int,paMax :Int) => (ataque,paMax,paMax)}
    
    energia match {
      case energia if energia < energiaMax * 0.5 => this.cambiaAEstado(Dormido(3)).modificaListaAtaque(nuevaListaAtaque)
      case _ => this.modificaListaAtaque(nuevaListaAtaque)
    }
  }

  private def aumentaPAMaximo(cant: Int): Pokemon = {
    val nuevaListaAtaque = this.listaAtaques.map{case (ataque : Ataque,pa :Int,paMax :Int) => (ataque,pa,paMax + cant)}
    this.modificaListaAtaque(nuevaListaAtaque)
  }
  
  def levantaSiPodes(kg:Int):Pokemon ={
    if(this.podesLevantar(kg)){
      (this.tipoPrincipal, this.tipoSecundario) match {
         case (Pelea, _) | (_, Pelea) => this.aumentaExperiencia(kg * 2)
         case (Fantasma, _) | (_, Fantasma) => throw FantasmaNoPuedeLevantarPesas(this)
         case _ => this.aumentaExperiencia(kg)
      }
    }
    else{
      this.cambiaAEstado(Paralizado)
    }                 
  }
  
  def nada(minutos :Int):Pokemon ={
    (this.tipoPrincipal, this.tipoSecundario) match {
        case (Agua, _)|(_,Agua) => this.modificaEnergia(minutos * -1).modificaVelocidad(minutos).aumentaExperiencia(minutos * 200)
        case (Fuego, _) | (_, Fuego) | (Tierra, _) | (_, Tierra) | (Roca, _) | (_, Roca) => this.cambiaAEstado(Ko)
        case _ => this.modificaEnergia(minutos * -1).aumentaExperiencia(minutos * 200)
    }
  }  
  
  def podesLevantar(kg: Int):Boolean = kg < (10 * this.fuerza + 1)
  
  def aprendeAtaqueSiPodes(ataqueAAprender:Ataque):Pokemon =ataqueAAprender.tipo match {
          case Normal | this.tipoPrincipal | this.tipoSecundario => this.modificaListaAtaque((ataqueAAprender,ataqueAAprender.puntosAtaqueInciales,ataqueAAprender.puntosAtaqueInciales) :: this.listaAtaques)
          case _ => this.cambiaAEstado(Ko)
  }
  
  def realizaAtaqueSiPodes(ataqueARealizar :Ataque):Pokemon = {
          val resultadoAtaque = this.listaAtaques.find{case (ataque,pa,_) => ataque == ataqueARealizar && pa > 1}
          resultadoAtaque match {
            case None => throw PokemonNoConoceMovONoTienePA(this)
            case Some(resultadoAtaque) => this.realizaAtaque(ataqueARealizar)
          }
  }
  
  def realizaAtaque(ataqueARealizar :Ataque):Pokemon = {
         val pokemonAfectado = this.utilizaAtaque(ataqueARealizar)//this.sufriEfectosSecundarios(ataqueARealizar)
         ataqueARealizar.tipo match {
                case Dragon => pokemonAfectado.aumentaExperiencia(80)
                case pokemonAfectado.tipoPrincipal => pokemonAfectado.aumentaExperiencia(50)
                case pokemonAfectado.tipoSecundario => pokemonAfectado.aumentaExpEnBaseAGenero()
         }       
  }
          
  def utilizaAtaque(ataque: Ataque):Pokemon = {
    val nuevaListaAtaques = this.reducirPa(ataque)
    this.modificaListaAtaque(nuevaListaAtaques).sufriEfectosSecundarios(ataque)
  } 
  
  //reduce el pa del ataque que se realiza
  //ES HORRIBLE SE PUEDE HACER MEJOR LA LOGICA FIXME
  def reducirPa(ataque:Ataque):List[(Ataque,Int,Int)] ={   
    this.listaAtaques.map{case (a :Ataque,pa:Int,paMax:Int) => 
      if(a == ataque)
        (a,pa-1,paMax)
      else
        (a,pa,paMax)       
    }
  }
  
  def realizarActividad(actividad: Actividad): Pokemon = {
    val futuroPokemon = this.estado match {
      case Ko => throw EstaKo(this)
      case e: Dormido if e.turnos > 0 => this.cambiaAEstado(Dormido(e.turnos - 1))
      case e: Dormido => this.cambiaAEstado(Bueno).realizarActividad(actividad)
      case _: EstadoPokemon => actividad match {
        case UsarPocion => this.modificaEnergia(50)
        case UsarEther => this.cambiaAEstado(Bueno)
        case ComerHierro => this.modificaFuerza(5)
        case ComerCalcio => this.modificaVelocidad(5)
        case ComerZinc => this.aumentaPAMaximo(2)
        case Descansar => this.descansar
        case UsarAntidoto => this.estado match {
          case Envenenado => this.cambiaAEstado(Bueno)
          case _: EstadoPokemon => this
        }
        case FingirIntercambio => this.condicionEvolutiva match {
          case Intercambiar => this.evolucionar //Como me intercambiaron, evoluciono porque mi condicion evolutiva es Intercambiar. [Requerimiento-TP]
          case _: CondicionEvolutiva => this.fingiIntercambio()
        }
        case actividad: UsarPiedra => this.evaluarEfectos(actividad.piedra)
        case actividad: LevantarPesas => this.estado match {
          case Paralizado => this.cambiaAEstado(Ko)
          case _: EstadoPokemon => this.levantaSiPodes(actividad.kg)
        }
        case actividad: Nadar => this.nada(actividad.minutos)
        case actividad: RealizarUnAtaque => this.realizaAtaqueSiPodes(actividad.ataqueARealizar)
        case actividad: AprenderAtaque => this.aprendeAtaqueSiPodes(actividad.ataqueAAprender)
      }
    }
    futuroPokemon // retorno el posible pokemon habiendo realizado la actividad
  }

  override def equals(unPokemon: Any): Boolean = {
    // XXX Revisar
    var that: Pokemon = unPokemon.asInstanceOf[Pokemon]
    return that.estado == this.estado &&
      that.listaAtaques == this.listaAtaques &&
      that.tipoPrincipal == this.tipoPrincipal &&
      that.tipoSecundario == this.tipoSecundario &&
      that.nivel == this.nivel &&
      that.experiencia == this.experiencia &&
      that.genero == this.genero &&
      that.energia == this.energia &&
      that.energiaMax == this.energiaMax &&
      that.peso == this.peso &&
      that.fuerza == this.fuerza &&
      that.velocidad == this.velocidad &&
      that.condicionEvolutiva == this.condicionEvolutiva &&
      that.especie.resistenciaEvolutiva == especie.resistenciaEvolutiva
  }
}
