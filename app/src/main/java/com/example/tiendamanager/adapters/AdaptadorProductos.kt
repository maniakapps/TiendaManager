package com.example.tiendamanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tienda.models.Producto
import com.example.tiendamanager.R

class AdaptadorProductos(
    private val context: Context,
    private val productos: MutableList<Producto>,
    private val listener: Any
) : RecyclerView.Adapter<AdaptadorProductos.ViewHolder>(), View.OnClickListener {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.nombreTextView)
        val descripcionTextView: TextView = view.findViewById(R.id.descripcionTextView)
        val precioTextView: TextView = view.findViewById(R.id.precioTextView)
        val cantidadTextView: TextView = view.findViewById(R.id.cantidadTextView)

        init {
            view.setOnClickListener(this@AdaptadorProductos)
        }

        fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val producto = productos[position]
                listener(producto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = productos[position]
        holder.nombreTextView.text = producto.nombre
        holder.descripcionTextView.text = producto.descripcion
        holder.precioTextView.text = context.getString(R.string.precio_format, producto.precio)
        holder.cantidadTextView.text = context.getString(R.string.cantidad_format, producto.cantidad)
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    override fun onClick(view: View) {
        val position = recyclerView.getChildAdapterPosition(view)
        if (position != RecyclerView.NO_POSITION) {
            val producto = productos[position]
            listener(producto)
        }
    }

}
