package com.example.tienda.models

import java.util.*

class MovimientoInventario(
    val id: Int,
    val producto: Producto,
    var cantidad: Int,
    val tipoMovimiento: TipoMovimiento,
    val fecha: Date
)
