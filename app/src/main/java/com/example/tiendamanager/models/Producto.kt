package com.example.tienda.models

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    var cantidad: Int
)
