package com.example.tiendamanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tienda.ui.productos.ListaProductosFragment
import com.example.tiendamanager.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Agregar el fragmento ListaProductosFragment al contenedor de fragmentos
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragment, ListaProductosFragment.newInstance())
            .commit()
    }
}
