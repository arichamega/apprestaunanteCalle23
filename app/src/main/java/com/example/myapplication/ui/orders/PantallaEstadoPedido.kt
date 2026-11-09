package com.example.myapplication.ui.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
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
import com.example.myapplication.ui.admin.ViewModelAdmin
import java.util.Locale

// 🚩 [PANTALLA ESTADO PEDIDO]: Seguimiento en tiempo real del pedido en cocina
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEstadoPedido(
    viewModel: ViewModelAdmin = viewModel(),
    alVolverAlMenu: () -> Unit = {}
) {
    val pedidos by viewModel.pedidos.collectAsState()
    val pedidoActivo = pedidos.lastOrNull { it.estado != EstadoPedido.CANCELADO }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estado del Pedido", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    // inicio boton: volver al menu desde estado
                    IconButton(onClick = alVolverAlMenu) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver al menú")
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
                .padding(16.dp)
        ) {
            if (pedidoActivo == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No tienes pedidos activos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // inicio boton: ir a la carta
                        Button(
                            onClick = alVolverAlMenu,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Ir a la carta")
                        }
                    }
                }
            } else {
                val textoEstado = when (pedidoActivo.estado) {
                    EstadoPedido.RECIBIDO -> "¡Pedido Recibido!"
                    EstadoPedido.EN_COCINA -> "¡En Cocina!"
                    EstadoPedido.SERVIDO -> "¡Servido en Tu Mesa!"
                    EstadoPedido.CANCELADO -> "Pedido Cancelado"
                }

                val subtextoEstado = when (pedidoActivo.estado) {
                    EstadoPedido.RECIBIDO -> "Tu pedido ingresó al sistema y está en cola."
                    EstadoPedido.EN_COCINA -> "Los chefs están preparando tu comida con dedicación."
                    EstadoPedido.SERVIDO -> "¡Buen provecho! Tu comida está lista."
                    EstadoPedido.CANCELADO -> "Tu comanda ha sido cancelada."
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(
                                imageVector = if (pedidoActivo.estado == EstadoPedido.SERVIDO) Icons.Default.CheckCircle else Icons.Default.Restaurant,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = textoEstado,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = subtextoEstado,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PasoEstado("Recibido", estaCompletado = pedidoActivo.estado != EstadoPedido.CANCELADO)
                            PasoEstado("En Cocina", estaCompletado = pedidoActivo.estado == EstadoPedido.EN_COCINA || pedidoActivo.estado == EstadoPedido.SERVIDO)
                            PasoEstado("Servido", estaCompletado = pedidoActivo.estado == EstadoPedido.SERVIDO)
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Tiempo Estimado:", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            }
                            Text("15 - 20 min", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }

                        pedidoActivo.numeroMesa?.let { mesa ->
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.DinnerDining, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Mesa Asignada:", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                }
                                Text("#$mesa", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Detalle del Pedido #C28-${pedidoActivo.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(pedidoActivo.items) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${item.cantidad}x ${item.nombrePlato}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    if (item.notas.isNotEmpty()) {
                                        Text("Nota: ${item.notas}", fontSize = 12.sp, color = Color(0xFFE65100))
                                    }
                                }
                                Text("S/ ${String.format(Locale.US, "%.2f", item.precio * item.cantidad)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // inicio boton: volver al menu
                Button(
                    onClick = alVolverAlMenu,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Volver al Menú", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun PasoEstado(etiqueta: String, estaCompletado: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            color = if (estaCompletado) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f),
            shape = RoundedCornerShape(50)
        ) {
            Box(
                modifier = Modifier.size(28.dp),
                contentAlignment = Alignment.Center
            ) {
                if (estaCompletado) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = etiqueta,
            fontSize = 11.sp,
            fontWeight = if (estaCompletado) FontWeight.Bold else FontWeight.Normal,
            color = if (estaCompletado) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}
