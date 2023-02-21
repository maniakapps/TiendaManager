package com.example.tienda.ui.productos

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.tienda.MainActivity
import com.example.tienda.R
import com.example.tienda.database.BaseDeDatosHelper
import com.example.tienda.models.Producto

class VentanaProductoFragment : Fragment() {

    // ID del producto que se está editando o 0 si se está agregando un producto nuevo
    private var productoId: Int = 0

    // Vista del fragment
    private lateinit var vista: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflar el layout del fragment
        vista = inflater.inflate(R.layout.fragment_ventana_producto, container, false)

        // Obtener el ID del producto de los argumentos del fragment
        productoId = arguments?.getInt(ARG_PRODUCTO_ID) ?: 0

        // Configurar el listener para el botón Guardar
        vista.findViewById<Button>(R.id.guardarButton).setOnClickListener {
            guardarProducto()
        }

        // Cargar los detalles del producto si se está editando uno existente
        if (productoId != 0) {
            cargarProducto()
        }

        return vista
    }

    private fun cargarProducto() {
        // Cargar los detalles del producto desde la base de datos
        val producto = obtenerProductoDesdeBaseDeDatos(productoId)

        // Mostrar los detalles del producto en la vista
        if (producto != null) {
            vista.findViewById<EditText>(R.id.nombreEditText).setText(producto.nombre)
        }
        if (producto != null) {
            vista.findViewById<EditText>(R.id.descripcionEditText).setText(producto.descripcion)
        }
        if (producto != null) {
            vista.findViewById<EditText>(R.id.precioEditText).setText(producto.precio.toString())
        }
        if (producto != null) {
            vista.findViewById<EditText>(R.id.cantidadEditText).setText(producto.cantidad.toString())
        }
    }

    fun obtenerProductoDesdeBaseDeDatos(productoId: Int): Producto? {
        val db = context?.let { BaseDeDatosHelper(it).readableDatabase }
        val cursor = db?.rawQuery("SELECT * FROM $NOMBRE_TABLA_PRODUCTOS WHERE id = ?", arrayOf(productoId.toString()))

        var producto: Producto? = null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
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

                producto = Producto(id, nombre, descripcion, precio, cantidad)
            }
        }

        if (cursor != null) {
            cursor.close()
        }
        db?.close()

        return producto
    }



    private fun guardarProducto() {
        // Obtener los detalles del producto desde la vista
        val nombre = vista.findViewById<EditText>(R.id.nombreEditText).text.toString()
        val descripcion = vista.findViewById<EditText>(R.id.descripcionEditText).text.toString()
        val precio = vista.findViewById<EditText>(R.id.precioEditText).text.toString().toDouble()
        val cantidad = vista.findViewById<EditText>(R.id.cantidadEditText).text.toString().toInt()

        // Guardar los detalles del producto en la base de datos
        val producto = Producto(productoId, nombre, descripcion, precio, cantidad)
        guardarProductoEnBaseDeDatos(producto)

        // Actualizar la lista de productos en la ventana principal
        (requireActivity() as MainActivity).actualizarListaProductos()

        // Volver a la ventana principal
        requireActivity().supportFragmentManager.popBackStack()
    }
    fun guardarProductoEnBaseDeDatos(producto: Producto) {
        val nombreEditText = view?.findViewById<EditText>(R.id.nombreEditText)
        val precioEditText = view?.findViewById<EditText>(R.id.precioEditText)
        val cantidadEditText = view?.findViewById<EditText>(R.id.cantidadEditText)

        if (producto.nombre.isEmpty()) {
            if (nombreEditText != null) {
                nombreEditText.error = "El nombre es obligatorio"
            }
            return
        }

        if (producto.precio < 0) {
            if (precioEditText != null) {
                precioEditText.error = "El precio debe ser mayor o igual a 0"
            }
            return
        }

        if (producto.cantidad < 0) {
            if (cantidadEditText != null) {
                cantidadEditText.error = "La cantidad debe ser mayor o igual a 0"
            }
            return
        }

        val db = BaseDeDatosHelper(requireContext()).writableDatabase

        if (producto.id == 0) {
            // Nuevo producto: agregar a la base de datos
            val valores = ContentValues().apply {
                put("nombre", producto.nombre)
                put("descripcion", producto.descripcion)
                put("precio", producto.precio)
                put("cantidad", producto.cantidad)
            }
            db.insert(NOMBRE_TABLA_PRODUCTOS, null, valores)
        } else {
            // Producto existente: actualizar en la base de datos
            val valores = ContentValues().apply {
                put("nombre", producto.nombre)
                put("descripcion", producto.descripcion)
                put("precio", producto.precio)
                put("cantidad", producto.cantidad)
            }
            db.update(NOMBRE_TABLA_PRODUCTOS, valores, "id = ${producto.id}", null)
        }

        db.close()

        // Volver a la ventana de lista de productos
        val listaProductosFragment = ListaProductosFragment.newInstance()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragment, listaProductosFragment)
            .commit()
    }

    companion object {
        const val ARG_PRODUCTO_ID = "productoId"
        private const val NOMBRE_TABLA_PRODUCTOS = "Productos"

        fun newInstance(productoId: Int): VentanaProductoFragment {
            val fragment = VentanaProductoFragment()
            val args = Bundle()
            args.putInt(ARG_PRODUCTO_ID, productoId)
            fragment.arguments = args
            return fragment
        }
    }
}

