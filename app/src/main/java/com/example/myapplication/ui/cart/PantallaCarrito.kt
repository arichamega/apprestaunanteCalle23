package com.example.myapplication.ui.cart

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

// 🚩 [PANTALLA CARRITO]: Pantalla de Carrito para revisión de productos, mesa, método de pago y confirmación
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCarrito(
    titulo: String = "Mi Bolsa - Calle 28",
    viewModel: ViewModelCarrito = viewModel(),
    alRealizarPedido: () -> Unit,
    alVolverAlMenu: () -> Unit
) {
    val listaItems by viewModel.items.collectAsState()
    val montoTotal = listaItems.sumOf { it.precio * it.cantidad }
    var textoNumeroMesa by rememberSaveable { mutableStateOf("") }
    var errorNumeroMesa by remember { mutableStateOf<String?>(null) }
    var metodoPagoSeleccionado by remember { mutableStateOf("Yape / Plin") }
    val contexto = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            if (listaItems.isNotEmpty()) {
                Surface(
                    tonalElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text(
                                text = "S/ ${String.format(Locale.US, "%.2f", montoTotal)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // inicio boton: pagar pedido
                        Button(
                            onClick = { 
                                val numeroMesa = textoNumeroMesa.trim().toIntOrNull()
                                if (numeroMesa == null || numeroMesa <= 0) {
                                    errorNumeroMesa = "Ingresa un número de mesa válido"
                                    return@Button
                                } else {
                                    errorNumeroMesa = null
                                }

                                viewModel.confirmarPedido(numeroMesa)
                                Toast.makeText(
                                    contexto, 
                                    "¡Pago de S/ ${String.format(Locale.US, "%.2f", montoTotal)} realizado con $metodoPagoSeleccionado!", 
                                    Toast.LENGTH_LONG
                                ).show()
                                // navegacion: ir a estado de pedido
                                alRealizarPedido()
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Pagar Pedido • S/ ${String.format(Locale.US, "%.2f", montoTotal)}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (listaItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Tu carrito está vacío", color = MaterialTheme.colorScheme.secondary, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Agrega tus platos favoritos para empezar tu pedido", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(20.dp))

                    // inicio boton: volver a la carta
                    Button(
                        onClick = { 
                            // navegacion: volver al menu
                            alVolverAlMenu() 
                        },
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Volver a la carta")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Número de Mesa",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = textoNumeroMesa,
                                onValueChange = { input ->
                                    if (input.all { it.isDigit() } && input.length <= 3) {
                                        textoNumeroMesa = input
                                        errorNumeroMesa = null
                                    }
                                },
                                label = { Text("Mesa destino *") },
                                placeholder = { Text("Ej. 5") },
                                isError = errorNumeroMesa != null,
                                supportingText = errorNumeroMesa?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                                leadingIcon = { Icon(Icons.Default.Tag, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Método de Pago",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            val metodos = listOf(
                                Triple("Yape / Plin", Icons.Default.QrCode, "Escanear QR o celular"),
                                Triple("Tarjeta de Crédito / Débito", Icons.Default.CreditCard, "POS o pago directo"),
                                Triple("Efectivo", Icons.Default.Money, "Pago directo en mesa")
                            )

                            metodos.forEach { (nombre, icono, desc) ->
                                val estaSeleccionado = metodoPagoSeleccionado == nombre
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { metodoPagoSeleccionado = nombre },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (estaSeleccionado) MaterialTheme.colorScheme.primaryContainer else Color(0xFFF8F8F8)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = estaSeleccionado,
                                            onClick = { metodoPagoSeleccionado = nombre }
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = icono,
                                            contentDescription = null,
                                            tint = if (estaSeleccionado) MaterialTheme.colorScheme.primary else Color.Gray
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = nombre,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = if (estaSeleccionado) MaterialTheme.colorScheme.primary else Color.Unspecified
                                            )
                                            Text(
                                                text = desc,
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Resumen de Platos",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(listaItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.nombrePlato,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "S/ ${String.format(Locale.US, "%.2f", item.precio)} c/u",
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontSize = 13.sp
                                    )
                                }

                                Text(
                                    text = "S/ ${String.format(Locale.US, "%.2f", item.precio * item.cantidad)}",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 16.sp
                                )
                            }

                            if (item.notas.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    color = Color(0xFFFFF3E0),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Nota: ${item.notas}",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontSize = 12.sp,
                                        color = Color(0xFFE65100)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // 🚩 [RF16 - MODIFICACIÓN DE ÍTEMS EN EL CARRITO]: Cambiar cantidades (- / +) o eliminar platos, recalculando el total automáticamente
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // inicio boton: disminuir cantidad carrito (RF16)
                                    IconButton(
                                        onClick = {
                                            viewModel.actualizarCantidad(item.idPlato, item.notas, item.cantidad - 1)
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Disminuir", modifier = Modifier.size(18.dp))
                                    }

                                    Text(
                                        text = "${item.cantidad}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )

                                    // inicio boton: aumentar cantidad carrito (RF16)
                                    IconButton(
                                        onClick = {
                                            viewModel.actualizarCantidad(item.idPlato, item.notas, item.cantidad + 1)
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Aumentar", modifier = Modifier.size(18.dp))
                                    }
                                }

                                // inicio boton: eliminar elemento del carrito (RF16)
                                IconButton(onClick = { viewModel.eliminarDelCarrito(item.idPlato, item.notas) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
