package com.example.myapplication.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.EstadoPedido
import com.example.myapplication.data.model.Pedido
import com.example.myapplication.data.repository.RepositorioPedidos
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelAdmin : ViewModel() {
    val pedidos: StateFlow<List<Pedido>> = RepositorioPedidos.pedidos

    fun actualizarEstadoPedido(idPedido: String, nuevoEstado: EstadoPedido) {
        viewModelScope.launch {
            RepositorioPedidos.actualizarEstadoPedido(idPedido, nuevoEstado)
        }
    }

    fun marcarComoPagado(idPedido: String) {
        viewModelScope.launch {
            RepositorioPedidos.marcarComoPagado(idPedido)
        }
    }

    fun actualizarCantidadItemPedido(idPedido: String, idPlato: String, notas: String, nuevaCantidad: Int) {
        viewModelScope.launch {
            RepositorioPedidos.actualizarCantidadItemPedido(idPedido, idPlato, notas, nuevaCantidad)
        }
    }

    fun eliminarItemDePedido(idPedido: String, idPlato: String, notas: String) {
        viewModelScope.launch {
            RepositorioPedidos.eliminarItemDePedido(idPedido, idPlato, notas)
        }
    }

    fun cancelarPedido(idPedido: String) {
        viewModelScope.launch {
            RepositorioPedidos.cancelarPedido(idPedido)
        }
    }
}
