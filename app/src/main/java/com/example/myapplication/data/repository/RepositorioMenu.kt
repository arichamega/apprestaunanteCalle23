package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Plato
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

object RepositorioMenu {
    private val _platos = MutableStateFlow(listOf(
        Plato("1", "Lomo Saltado Clásico", "Trozos de lomo fino salteados al wok con cebolla, tomate y ají amarillo. Acompañado de papas fritas y arroz.", 38.00, "lomo_saltado", "Platos", esPopular = true, tiempoEstimado = 15),
        Plato("2", "Ceviche Carretillero", "Pesca del día en fresca leche de tigre, cebolla roja, camote glaseado, choclo y su chicharrón.", 42.00, "https://images.unsplash.com/photo-1535399831218-d5bd36d1a6b3?w=800", "Platos", esPopular = false, esNuevo = true, tiempoEstimado = 12),
        Plato("3", "Ají de Gallina", "Cremoso guiso de pechuga deshilachada con base de ají amarillo y pecanas. Servido con arroz y papa.", 32.00, "aji_de_gallina", "Platos", esPopular = false, tiempoEstimado = 15),
        Plato("4", "Papas Fritas", "Crujientes papas fritas cortadas a mano con sal de mar.", 12.00, "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=800", "Entradas", esPopular = false, tiempoEstimado = 8),
        Plato("5", "Chicha Morada", "Refrescante bebida tradicional peruana hecha de maíz morado con piña, manzana y limón.", 8.00, "chicha_morada", "Bebidas", esPopular = true, tiempoEstimado = 3),
        Plato("6", "Causa Rellena", "Masa de papa amarilla sazonada con ají amarillo y limón, rellena de pollo con mayonesa y palta.", 22.00, "causa_rellena", "Entradas", esPopular = false, tiempoEstimado = 10),
        Plato("7", "Helado de Vainilla", "crema sabor a vainilla.", 18.00, "https://images.unsplash.com/photo-1570197788417-0e82375c9371?w=800", "Postres", esPopular = false, tiempoEstimado = 8),
        Plato("8", "Ensalada de gelatina", "Gelatina de diferentes sabores.", 10.00, "ensalada_gelatina", "Postres", esPopular = false, tiempoEstimado = 8)
    ))
    val platos: StateFlow<List<Plato>> = _platos.asStateFlow()

    fun agregarPlato(plato: Plato) {
        val nuevoPlato = plato.copy(id = UUID.randomUUID().toString().take(6))
        _platos.update { it + nuevoPlato }
    }

    fun actualizarPlato(platoActualizado: Plato) {
        _platos.update { listaActual ->
            listaActual.map { if (it.id == platoActualizado.id) platoActualizado else it }
        }
    }

    fun eliminarPlato(idPlato: String) {
        _platos.update { lista -> lista.filterNot { plato -> plato.id == idPlato } }
    }

    fun alternarFavorito(idPlato: String) {
        _platos.update { listaActual ->
            listaActual.map { 
                if (it.id == idPlato) it.copy(esFavorito = !it.esFavorito) else it 
            }
        }
    }
}
