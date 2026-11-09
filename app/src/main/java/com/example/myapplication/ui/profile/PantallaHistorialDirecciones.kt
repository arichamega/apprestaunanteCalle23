package com.example.myapplication.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.TableRestaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Direccion(
    val id: String,
    val titulo: String,
    val detalle: String,
    val esMesa: Boolean = false
)

// 🚩 [PANTALLA HISTORIAL DIRECCIONES]: Gestión de direcciones de entrega y mesas guardadas del cliente
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHistorialDirecciones(
    alVolverAlPerfil: () -> Unit
) {
    var listaDirecciones by remember {
        mutableStateOf(
            listOf(
                Direccion("1", "Mesa Preferida", "Mesa 5 - Salón Principal", esMesa = true),
                Direccion("2", "Domicilio Principal", "Av. 28 de Julio 123, Miraflores, Lima"),
                Direccion("3", "Oficina", "Av. Javier Prado Este 4560, Surco, Lima")
            )
        )
    }

    var mostrarDialogo by remember { mutableStateOf(false) }
    var nuevoTitulo by remember { mutableStateOf("") }
    var nuevoDetalle by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Direcciones", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    // inicio boton: volver al perfil
                    IconButton(onClick = alVolverAlPerfil) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver al Perfil")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        floatingActionButton = {
            // inicio boton: agregar nueva direccion
            FloatingActionButton(onClick = { mostrarDialogo = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Dirección")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listaDirecciones) { direccion ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(
                                        imageVector = if (direccion.esMesa) Icons.Default.TableRestaurant else Icons.Default.LocationOn,
                                        contentDescription = null,
                                        modifier = Modifier.padding(10.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = direccion.titulo,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = direccion.detalle,
                                        fontSize = 13.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            // inicio boton: eliminar direccion
                            IconButton(onClick = {
                                listaDirecciones = listaDirecciones.filterNot { it.id == direccion.id }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                title = { Text("Agregar Nueva Dirección") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = nuevoTitulo,
                            onValueChange = { nuevoTitulo = it },
                            label = { Text("Etiqueta (Ej. Casa, Trabajo)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = nuevoDetalle,
                            onValueChange = { nuevoDetalle = it },
                            label = { Text("Dirección / Mesa") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    // inicio boton: guardar direccion
                    Button(
                        onClick = {
                            if (nuevoTitulo.isNotEmpty() && nuevoDetalle.isNotEmpty()) {
                                val idNuevo = (listaDirecciones.size + 1).toString()
                                listaDirecciones = listaDirecciones + Direccion(idNuevo, nuevoTitulo, nuevoDetalle)
                                nuevoTitulo = ""
                                nuevoDetalle = ""
                                mostrarDialogo = false
                            }
                        }
                    ) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    // inicio boton: cancelar agregar direccion
                    TextButton(onClick = { mostrarDialogo = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
