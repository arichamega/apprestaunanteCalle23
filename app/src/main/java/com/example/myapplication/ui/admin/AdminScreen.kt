package com.example.myapplication.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.model.Order
import com.example.myapplication.data.model.OrderStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: AdminViewModel = viewModel(),
    onLogout: () -> Unit
) {
    val orders by viewModel.orders.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administración") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(orders) { order ->
                OrderAdminItem(order) { newStatus ->
                    viewModel.updateOrderStatus(order.id, newStatus)
                }
            }
        }
    }
}

@Composable
fun OrderAdminItem(order: Order, onStatusChange: (OrderStatus) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Pedido #${order.id}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Mesa: ${order.tableNumber ?: "N/A"}")
            Text(text = "Total: S/ ${order.totalPrice}")
            Text(text = "Estado: ${order.status.displayName}")
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onStatusChange(OrderStatus.PREPARING) }) {
                    Text("Preparar")
                }
                Button(onClick = { onStatusChange(OrderStatus.READY) }) {
                    Text("Listo")
                }
            }
        }
    }
}
