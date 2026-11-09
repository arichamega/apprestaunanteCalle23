package com.example.myapplication.ui.menu

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.Plato
import com.example.myapplication.data.repository.RepositorioMenu
import kotlinx.coroutines.flow.StateFlow

class ViewModelMenu : ViewModel() {
    val platos: StateFlow<List<Plato>> = RepositorioMenu.platos

    fun agregarPlato(nombre: String, descripcion: String, precio: Double, categoria: String, urlImagen: String = "") {
        val plato = Plato(
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            categoria = categoria,
            urlImagen = urlImagen
        )
        RepositorioMenu.agregarPlato(plato)
    }

    fun actualizarPlato(plato: Plato) {
        RepositorioMenu.actualizarPlato(plato)
    }

    fun eliminarPlato(idPlato: String) {
        RepositorioMenu.eliminarPlato(idPlato)
    }

    fun alternarFavorito(idPlato: String) {
        RepositorioMenu.alternarFavorito(idPlato)
    }
}
