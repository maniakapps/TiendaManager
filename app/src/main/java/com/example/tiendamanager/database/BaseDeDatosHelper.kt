package com.example.tienda.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tienda.models.Producto

class BaseDeDatosHelper(contexto: Context) :
    SQLiteOpenHelper(contexto, NOMBRE_BASE_DE_DATOS, null, VERSION_BASE_DE_DATOS) {

    override fun onCreate(db: SQLiteDatabase) {
        // Crear la tabla de productos
        db.execSQL(
            "CREATE TABLE $NOMBRE_TABLA_PRODUCTOS (" +
                    "id INTEGER PRIMARY KEY, " +
                    "nombre TEXT, " +
                    "descripcion TEXT, " +
                    "precio REAL, " +
                    "cantidad INTEGER" +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Borrar y recrear la tabla de productos en caso de actualización de versión
        db.execSQL("DROP TABLE IF EXISTS $NOMBRE_TABLA_PRODUCTOS")
        onCreate(db)
    }

    fun agregarProducto(producto: Producto) {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", producto.nombre)
            put("descripcion", producto.descripcion)
            put("precio", producto.precio)
            put("cantidad", producto.cantidad)
        }
        db.insert(NOMBRE_TABLA_PRODUCTOS, null, valores)
        db.close()
    }

    fun obtenerProductos(): MutableList<Producto> {
        val productos = mutableListOf<Producto>()
        val query = "SELECT * FROM $NOMBRE_TABLA_PRODUCTOS ORDER BY id ASC"
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex("id")
                val nombreIndex = cursor.getColumnIndex("nombre")
                val descripcionIndex = cursor.getColumnIndex("descripcion")
                val precioIndex = cursor.getColumnIndex("precio")
                val cantidadIndex = cursor.getColumnIndex("cantidad")

                val id = if (idIndex >= 0) cursor.getInt(idIndex) else 0
                val nombre = if (nombreIndex >= 0) cursor.getString(nombreIndex) else ""
                val descripcion = if (descripcionIndex >= 0) cursor.getString(descripcionIndex) else ""
                val precio = if (precioIndex >= 0) cursor.getDouble(precioIndex) else 0.0
                val cantidad = if (cantidadIndex >= 0) cursor.getInt(cantidadIndex) else 0

                productos.add(Producto(id, nombre, descripcion, precio, cantidad))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return productos
    }


    fun obtenerProducto(id: Int): Producto? {
        val db = readableDatabase
        val query = "SELECT * FROM $NOMBRE_TABLA_PRODUCTOS WHERE id = $id"
        val cursor = db.rawQuery(query, null)

        var producto: Producto? = null
        if (cursor.moveToFirst()) {
            val nombreIndex = cursor.getColumnIndex("nombre")
            val descripcionIndex = cursor.getColumnIndex("descripcion")
            val precioIndex = cursor.getColumnIndex("precio")
            val cantidadIndex = cursor.getColumnIndex("cantidad")

            val nombre = if (nombreIndex >= 0) cursor.getString(nombreIndex) else ""
            val descripcion = if (descripcionIndex >= 0) cursor.getString(descripcionIndex) else ""
            val precio = if (precioIndex >= 0) cursor.getDouble(precioIndex) else 0.0
            val cantidad = if (cantidadIndex >= 0) cursor.getInt(cantidadIndex) else 0

            producto = Producto(id, nombre, descripcion, precio, cantidad)
        }

        cursor.close()
        db.close()

        return producto
    }


    fun actualizarProducto(producto: Producto) {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", producto.nombre)
            put("descripcion", producto.descripcion)
            put("precio", producto.precio)
            put("cantidad", producto.cantidad)
        }
        db.update(NOMBRE_TABLA_PRODUCTOS, valores, "id = ${producto.id}", null)
        db.close()
    }

    fun eliminarProducto(id: Int) {
        val db = writableDatabase
        db.delete(NOMBRE_TABLA_PRODUCTOS, "id = $id", null)
        db.close()
    }

    companion object {
        private const val NOMBRE_BASE_DE_DATOS = "MiTienda"
        private const val VERSION_BASE_DE_DATOS = 1
        private const val NOMBRE_TABLA_PRODUCTOS = "Productos"
    }
}
