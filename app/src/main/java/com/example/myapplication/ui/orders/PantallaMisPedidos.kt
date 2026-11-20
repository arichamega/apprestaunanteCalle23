package com.example.myapplication.ui.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.TableBar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.model.EstadoPedido
import com.example.myapplication.data.model.Pedido
import com.example.myapplication.ui.admin.ViewModelAdmin
import java.util.Locale

// 🚩 [PANTALLA MIS PEDIDOS]: Historial personal de pedidos del cliente ("Mis Pedidos") con opción de editar cantidad/eliminar platos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMisPedidos(
    viewModel: ViewModelAdmin = viewModel(),
    alCerrarSesion: () -> Unit = {}
) {
    val pedidos by viewModel.pedidos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            if (pedidos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Aún no tienes pedidos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tus compras y comandas guardadas aparecerán aquí.",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(pedidos) { pedido ->
                        TarjetaPedidoCliente(
                            pedido = pedido,
                            alActualizarCantidad = { idPlato, notas, nuevaCant ->
                                viewModel.actualizarCantidadItemPedido(pedido.id, idPlato, notas, nuevaCant)
                            },
                            alEliminarItem = { idPlato, notas ->
                                viewModel.eliminarItemDePedido(pedido.id, idPlato, notas)
                            },
                            alCancelarPedido = {
                                viewModel.cancelarPedido(pedido.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaPedidoCliente(
    pedido: Pedido,
    alActualizarCantidad: (idPlato: String, notas: String, nuevaCantidad: Int) -> Unit = { _, _, _ -> },
    alEliminarItem: (idPlato: String, notas: String) -> Unit = { _, _ -> },
    alCancelarPedido: () -> Unit = {}
) {
    val sePuedeEditar = pedido.estado == EstadoPedido.RECIBIDO || pedido.estado == EstadoPedido.EN_COCINA

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #C28-${pedido.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                val colorEstado = when (pedido.estado) {
                    EstadoPedido.SERVIDO -> Color(0xFF2E7D32)
                    EstadoPedido.EN_COCINA -> MaterialTheme.colorScheme.primary
                    EstadoPedido.CANCELADO -> MaterialTheme.colorScheme.error
                    else -> Color(0xFFE65100)
                }

                Surface(
                    color = colorEstado.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = pedido.estado.nombreMostrado,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = colorEstado,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            pedido.numeroMesa?.let { numeroMesa ->
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TableBar,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Mesa Destino: #$numeroMesa",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Productos Agregados:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            pedido.items.forEach { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.nombrePlato,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            if (item.notas.isNotEmpty()) {
                                Text(
                                    text = "Nota: ${item.notas}",
                                    fontSize = 12.sp,
                                    color = Color(0xFFE65100)
                                )
                            }
                        }

                        Text(
                            text = "S/ ${String.format(Locale.US, "%.2f", item.precio * item.cantidad)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (sePuedeEditar) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { alActualizarCantidad(item.idPlato, item.notas, item.cantidad - 1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Disminuir", modifier = Modifier.size(16.dp))
                                }

                                Text(
                                    text = "${item.cantidad}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )

                                IconButton(
                                    onClick = { alActualizarCantidad(item.idPlato, item.notas, item.cantidad + 1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Aumentar", modifier = Modifier.size(16.dp))
                                }
                            }

                            IconButton(
                                onClick = { alEliminarItem(item.idPlato, item.notas) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Total Calculado", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "S/ ${String.format(Locale.US, "%.2f", pedido.precioTotal)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (sePuedeEditar) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = alCancelarPedido,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancelar Pedido")
                }
            }
        }
    }
}
