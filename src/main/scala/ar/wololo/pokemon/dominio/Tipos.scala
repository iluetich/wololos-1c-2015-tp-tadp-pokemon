package ar.wololo.pokemon.dominio

trait Tipo {
  def aQuienesLeGanas(): List[Tipo]
  def leGanasA(tipo: Tipo) = aQuienesLeGanas.exists { t => t.equals(tipo) }
}
object Fuego extends Tipo { def aQuienesLeGanas: List[Tipo] = List(Planta, Hielo, Bicho) }
object Agua extends Tipo { def aQuienesLeGanas: List[Tipo] = List(Fuego, Tierra, Roca) }
object Planta extends Tipo { def aQuienesLeGanas: List[Tipo] = List(Agua, Tierra, Roca) }
object Tierra extends Tipo { def aQuienesLeGanas: List[Tipo] = List(Fuego, Electrico, Veneno, Roca) }
object Hielo extends Tipo { def aQuienesLeGanas: List[Tipo] = List(Planta, Tierra, Volador, Dragon) }
object Roca extends Tipo { def aQuienesLeGanas: List[Tipo] = List(Fuego, Hielo, Volador, Bicho) }
object Electrico extends Tipo { def aQuienesLeGanas: List[Tipo] = List(Agua, Volador) }
object Psiquico extends Tipo { def aQuienesLeGanas: List[Tipo] = List(Pelea, Veneno) }
object Pelea extends Tipo { def aQuienesLeGanas: List[Tipo] = List(Normal, Hielo, Roca) }
object Fantasma extends Tipo { def aQuienesLeGanas: List[Tipo] = List(Psiquico, Fantasma) }
object Volador extends Tipo { def aQuienesLeGanas: List[Tipo] = List(Planta, Pelea, Bicho) }
object Bicho extends Tipo { def aQuienesLeGanas: List[Tipo] = List(Planta, Psiquico) }
object Veneno extends Tipo { def aQuienesLeGanas: List[Tipo] = List(Planta) }
object Dragon extends Tipo { def aQuienesLeGanas: List[Tipo] = List(Dragon) }
object Normal extends Tipo { def aQuienesLeGanas: List[Tipo] = List() }