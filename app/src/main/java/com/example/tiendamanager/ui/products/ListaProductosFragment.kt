package com.example.tienda.ui.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tienda.database.BaseDeDatosHelper
import com.example.tienda.models.Producto
import com.example.tiendamanager.R
import com.example.tiendamanager.adapters.AdaptadorProductos


class ListaProductosFragment : Fragment() {

    // Variables para la lista de productos y el adaptador
    private lateinit var listaProductos: MutableList<Producto>
    private lateinit var adaptador: AdaptadorProductos
    private lateinit var helper: BaseDeDatosHelper

    // Vista del fragment
    private lateinit var vista: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflar el layout del fragment
        vista = inflater.inflate(R.layout.fragment_lista_productos, container, false)

        // Configurar el listener para el botón Agregar Producto
        vista.findViewById<Button>(R.id.agregarProductoButton).setOnClickListener {
            val ventanaProducto = VentanaProductoFragment.newInstance(0)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, ventanaProducto)
                .addToBackStack(null)
                .commit()
        }

        // Configurar el adaptador para la lista de productos
        listaProductos = helper.obtenerProductos()
        adaptador = AdaptadorProductos(requireContext(), listaProductos, object : AdaptadorProductos.OnItemClickListener {
            override fun onItemClick(producto: Producto) {
                val ventanaProducto = VentanaProductoFragment.newInstance(producto.id)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.contenedorFragment, ventanaProducto)
                    .addToBackStack(null)
                    .commit()
            }
        })

        vista.findViewById<RecyclerView>(R.id.listaProductosRecyclerView).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adaptador
        }

        return vista
    }

    fun actualizarListaProductos() {
        // Actualizar la lista de productos desde la base de datos
        listaProductos.clear()
        listaProductos.addAll(helper.obtenerProductos())
        adaptador.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): ListaProductosFragment {
            return ListaProductosFragment()
        }
    }
}
