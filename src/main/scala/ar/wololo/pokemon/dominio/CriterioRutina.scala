package ar.wololo.pokemon.dominio

import scala.util.Try

object Tipos {
  type Criterio = Seq[(Rutina, Pokemon)] => Rutina
  type TernaAtaque = (Ataque, Int, Int)
  type Actividad = Pokemon => Pokemon
}