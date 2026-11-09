package com.example.myapplication.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.model.Plato
import com.example.myapplication.ui.menu.ViewModelMenu

// 🚩 [PANTALLA GESTIÓN MENÚ]: Gestión de Menú (CRUD de Platillos) para Administrador
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaGestionMenu(
    viewModel: ViewModelMenu = viewModel(),
    alCerrarSesion: () -> Unit
) {
    val platos by viewModel.platos.collectAsState()
    var mostrarDialogo by remember { mutableStateOf(false) }
    var platoSeleccionado by remember { mutableStateOf<Plato?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Menú") },
                actions = {
                    // inicio boton: cerrar sesion gestion menu
                    IconButton(onClick = alCerrarSesion) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            // inicio boton: agregar nuevo plato
            FloatingActionButton(onClick = {
                platoSeleccionado = null
                mostrarDialogo = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Plato")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(platos) { plato ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = plato.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(text = "S/ ${plato.precio}")
                            Text(text = plato.categoria, style = MaterialTheme.typography.bodySmall)
                        }
                        Row {
                            // inicio boton: editar plato
                            IconButton(onClick = {
                                platoSeleccionado = plato
                                mostrarDialogo = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            // inicio boton: eliminar plato
                            IconButton(onClick = { viewModel.eliminarPlato(plato.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }

        if (mostrarDialogo) {
            DialogoPlato(
                plato = platoSeleccionado,
                alDescartar = { mostrarDialogo = false },
                alConfirmar = { nombre, desc, precio, cat, urlImg ->
                    if (platoSeleccionado == null) {
                        viewModel.agregarPlato(nombre, desc, precio, cat, urlImg)
                    } else {
                        viewModel.actualizarPlato(platoSeleccionado!!.copy(
                            nombre = nombre,
                            descripcion = desc,
                            precio = precio,
                            categoria = cat,
                            urlImagen = urlImg
                        ))
                    }
                    mostrarDialogo = false
                }
            )
        }
    }
}

@Composable
fun DialogoPlato(
    plato: Plato?,
    alDescartar: () -> Unit,
    alConfirmar: (String, String, Double, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf(plato?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(plato?.descripcion ?: "") }
    var precioTexto by remember { mutableStateOf(plato?.precio?.toString() ?: "") }
    var categoria by remember { mutableStateOf(plato?.categoria ?: "") }
    var urlImagen by remember { mutableStateOf(plato?.urlImagen ?: "") }

    AlertDialog(
        onDismissRequest = alDescartar,
        title = { Text(if (plato == null) "Agregar Plato" else "Editar Plato") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
                OutlinedTextField(
                    value = precioTexto,
                    onValueChange = { precioTexto = it },
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") })
                OutlinedTextField(value = urlImagen, onValueChange = { urlImagen = it }, label = { Text("URL de Imagen") })
            }
        },
        confirmButton = {
            // inicio boton: guardar plato en dialogo
            Button(onClick = {
                val precio = precioTexto.toDoubleOrNull() ?: 0.0
                alConfirmar(nombre, descripcion, precio, categoria, urlImagen)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            // inicio boton: cancelar dialogo plato
            TextButton(onClick = alDescartar) {
                Text("Cancelar")
            }
        }
    )
}
