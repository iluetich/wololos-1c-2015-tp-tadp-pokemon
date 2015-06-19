package ar.wololo.pokemon.dominio

case class Ataque(val nombre: String,
             val efecto: Pokemon => Pokemon,
             val tipo: Tipo)
             