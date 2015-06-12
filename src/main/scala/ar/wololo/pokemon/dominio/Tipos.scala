package ar.wololo.pokemon.dominio

trait Tipo { def leGanaA(): List[Tipo] }
object Fuego extends Tipo { def leGanaA(): List[Tipo] = List(Planta, Hielo, Bicho) }
object Agua extends Tipo { def leGanaA(): List[Tipo] = List(Fuego, Tierra, Roca) }
object Planta extends Tipo { def leGanaA(): List[Tipo] = List(Agua, Tierra, Roca) }
object Tierra extends Tipo { def leGanaA(): List[Tipo] = List(Fuego, Electrico, Veneno, Roca) }
object Hielo extends Tipo { def leGanaA(): List[Tipo] = List(Planta, Tierra, Volador, Dragon) }
object Roca extends Tipo { def leGanaA(): List[Tipo] = List(Fuego, Hielo, Volador, Bicho) }
object Electrico extends Tipo { def leGanaA(): List[Tipo] = List(Agua, Volador) }
object Psiquico extends Tipo { def leGanaA(): List[Tipo] = List(Pelea, Veneno) }
object Pelea extends Tipo { def leGanaA(): List[Tipo] = List(Normal, Hielo, Roca) }
object Fantasma extends Tipo { def leGanaA(): List[Tipo] = List(Psiquico, Fantasma) }
object Volador extends Tipo { def leGanaA(): List[Tipo] = List(Planta, Pelea, Bicho) }
object Bicho extends Tipo { def leGanaA(): List[Tipo] = List(Planta, Psiquico) }
object Veneno extends Tipo { def leGanaA(): List[Tipo] = List(Planta) }
object Dragon extends Tipo { def leGanaA(): List[Tipo] = List(Dragon) }
object Normal extends Tipo { def leGanaA(): List[Tipo] = List() }