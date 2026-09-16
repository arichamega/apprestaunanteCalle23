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
import com.example.myapplication.data.model.Dish
import com.example.myapplication.ui.menu.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMenuScreen(
    viewModel: MenuViewModel = viewModel(),
    onLogout: () -> Unit
) {
    val dishes by viewModel.dishes.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedDish by remember { mutableStateOf<Dish?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Menú") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedDish = null
                showDialog = true
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
            items(dishes) { dish ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = dish.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = "S/ ${dish.price}")
                            Text(text = dish.category, style = MaterialTheme.typography.bodySmall)
                        }
                        Row {
                            IconButton(onClick = {
                                selectedDish = dish
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = { viewModel.deleteDish(dish.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            DishDialog(
                dish = selectedDish,
                onDismiss = { showDialog = false },
                onConfirm = { name, desc, price, cat ->
                    if (selectedDish == null) {
                        viewModel.addDish(name, desc, price, cat)
                    } else {
                        viewModel.updateDish(selectedDish!!.copy(
                            name = name,
                            description = desc,
                            price = price,
                            category = cat
                        ))
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun DishDialog(
    dish: Dish?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Double, String) -> Unit
) {
    var name by remember { mutableStateOf(dish?.name ?: "") }
    var description by remember { mutableStateOf(dish?.description ?: "") }
    var price by remember { mutableStateOf(dish?.price?.toString() ?: "") }
    var category by remember { mutableStateOf(dish?.category ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (dish == null) "Agregar Plato" else "Editar Plato") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") })
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Categoría") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val p = price.toDoubleOrNull() ?: 0.0
                onConfirm(name, description, p, category)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
