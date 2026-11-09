package com.example.myapplication.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.Restaurant
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
import java.util.Locale

// 🚩 [PANTALLA ADMINISTRADOR]: Comandas activas e Historial para Administrador
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAdministrador(
    mostrarSoloHistorial: Boolean = false,
    viewModel: ViewModelAdmin = viewModel(),
    alCerrarSesion: () -> Unit
) {
    val pedidos by viewModel.pedidos.collectAsState()

    val pedidosFiltrados = if (mostrarSoloHistorial) {
        pedidos.filter { it.estado == EstadoPedido.SERVIDO || it.estado == EstadoPedido.CANCELADO }
    } else {
        pedidos.filter { it.estado == EstadoPedido.RECIBIDO || it.estado == EstadoPedido.EN_COCINA }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = if (mostrarSoloHistorial) "Historial de Comandas" else "Comandas en Cocina", 
                        fontWeight = FontWeight.Bold
                    ) 
                },
                actions = {
                    // inicio boton: cerrar sesion admin
                    IconButton(onClick = alCerrarSesion) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                },
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
            if (pedidosFiltrados.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (mostrarSoloHistorial) "No hay pedidos en el historial." else "No hay comandas activas en cocina.",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(pedidosFiltrados) { pedido ->
                        TarjetaPedidoAdmin(
                            pedido = pedido,
                            alCambiarEstado = { nuevoEstado ->
                                viewModel.actualizarEstadoPedido(pedido.id, nuevoEstado)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaPedidoAdmin(pedido: Pedido, alCambiarEstado: (EstadoPedido) -> Unit) {
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

                Surface(
                    color = Color(0xFFFBE9E7),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TableBar,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Mesa ${pedido.numeroMesa ?: "N/A"}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Cliente: ${pedido.idUsuario.ifEmpty { "Cliente" }}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Platos solicitados:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            pedido.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${item.cantidad}x ${item.nombrePlato}",
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
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Estado: ", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    val colorEstado = when (pedido.estado) {
                        EstadoPedido.SERVIDO -> Color(0xFF2E7D32)
                        EstadoPedido.EN_COCINA -> MaterialTheme.colorScheme.primary
                        EstadoPedido.CANCELADO -> MaterialTheme.colorScheme.error
                        else -> Color(0xFFE65100)
                    }
                    Text(
                        text = pedido.estado.nombreMostrado,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorEstado
                    )
                }

                Text(
                    text = "Total: S/ ${String.format(Locale.US, "%.2f", pedido.precioTotal)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (pedido.estado == EstadoPedido.CANCELADO) {
                Surface(
                    color = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "❌ Pedido Cancelado",
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // inicio boton: cambiar estado a en cocina
                    Button(
                        onClick = { alCambiarEstado(EstadoPedido.EN_COCINA) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (pedido.estado == EstadoPedido.EN_COCINA) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("En Cocina", fontSize = 12.sp)
                        }
                    }

                    // inicio boton: cambiar estado a servido
                    Button(
                        onClick = { alCambiarEstado(EstadoPedido.SERVIDO) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (pedido.estado == EstadoPedido.SERVIDO) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DinnerDining, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Servido", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
