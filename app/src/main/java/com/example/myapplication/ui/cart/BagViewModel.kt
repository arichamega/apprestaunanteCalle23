package com.example.myapplication.ui.cart

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.EstadoPedido
import com.example.myapplication.data.model.ItemPedido
import com.example.myapplication.data.model.Pedido
import com.example.myapplication.data.model.Plato
import com.example.myapplication.data.repository.RepositorioPedidos
import com.example.myapplication.data.repository.RepositorioUsuarios
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ViewModelBolsa : ViewModel() {
    private val _items = MutableStateFlow<List<ItemPedido>>(emptyList())
    val items: StateFlow<List<ItemPedido>> = _items.asStateFlow()

    val cantidadTotalElementos: Int
        get() = _items.value.sumOf { it.cantidad }

    val precioTotal: Double
        get() = _items.value.sumOf { it.precio * it.cantidad }

    fun agregarABolsa(plato: Plato, cantidad: Int = 1, notas: String = "") {
        _items.update { elementosActuales ->
            val indiceExistente = elementosActuales.indexOfFirst { it.idPlato == plato.id && it.notas == notas }
            if (indiceExistente >= 0) {
                elementosActuales.mapIndexed { indice, elemento ->
                    if (indice == indiceExistente) {
                        elemento.copy(cantidad = elemento.cantidad + cantidad)
                    } else {
                        elemento
                    }
                }
            } else {
                elementosActuales + ItemPedido(
                    idPlato = plato.id,
                    nombrePlato = plato.nombre,
                    cantidad = cantidad,
                    precio = plato.precio,
                    notas = notas
                )
            }
        }
    }

    fun actualizarCantidad(idPlato: String, notas: String, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            eliminarDeBolsa(idPlato, notas)
        } else {
            _items.update { elementosActuales ->
                elementosActuales.map {
                    if (it.idPlato == idPlato && it.notas == notas) {
                        it.copy(cantidad = nuevaCantidad)
                    } else {
                        it
                    }
                }
            }
        }
    }

    fun eliminarDeBolsa(idPlato: String, notas: String = "") {
        _items.update { elementosActuales ->
            elementosActuales.filterNot { it.idPlato == idPlato && it.notas == notas }
        }
    }

    fun confirmarPedido(numeroMesa: Int) {
        if (_items.value.isEmpty()) return
        
        val nuevoPedido = Pedido(
            idUsuario = RepositorioUsuarios.nombreUsuarioActual.value.ifEmpty { "Cliente" },
            items = _items.value,
            precioTotal = precioTotal,
            numeroMesa = numeroMesa,
            estado = EstadoPedido.RECIBIDO
        )
        RepositorioPedidos.realizarPedido(nuevoPedido)
        limpiarBolsa()
    }

    fun limpiarBolsa() {
        _items.value = emptyList()
    }
}
