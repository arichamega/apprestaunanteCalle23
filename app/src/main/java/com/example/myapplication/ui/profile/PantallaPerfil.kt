package com.example.myapplication.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.repository.RepositorioUsuarios

// [PANTALLA PERFIL]: Perfil del cliente, puntos acumulados, historial de pedidos, direcciones y cerrar sesión
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(
    alCerrarSesion: () -> Unit = {},
    alNavegarAFavoritos: () -> Unit = {},
    alNavegarAPedidos: () -> Unit = {},
    alNavegarAHistorialPedidos: () -> Unit = {},
    alNavegarAHistorialDirecciones: () -> Unit = {},
    alNavegarAPromociones: () -> Unit = {},
    alNavegarANuevaPantalla: () -> Unit = {},
    alNavegarAConfirmacion: () -> Unit = {}
) {
    val nombreUsuarioActual by RepositorioUsuarios.nombreUsuarioActual.collectAsState()
    val correo = RepositorioUsuarios.correoUsuarioActual ?: "cliente@calle28.pe"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape,
                modifier = Modifier.size(90.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = nombreUsuarioActual.ifEmpty { "Ana Martínez" },
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = correo,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("120 pts", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Puntos Calle 28", fontSize = 12.sp, color = Color.Gray)
                    }

                    VerticalDivider(modifier = Modifier.height(36.dp), color = Color.LightGray.copy(alpha = 0.4f))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("2 Cupones", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Disponibles", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text("promociones", fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text("Revisa todas las promociones") },
                        leadingContent = {
                            Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { alNavegarAPromociones() }
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))
                    // inicio navegacion: ir a historial de pedidos desde perfil
                    ListItem(
                        headlineContent = { Text("Historial de Pedidos", fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text("Revisa todas tus compras pasadas") },
                        leadingContent = {
                            Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { alNavegarAHistorialPedidos() }
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))

                    // inicio navegacion: ir a historial de direcciones desde perfil
                    ListItem(
                        headlineContent = { Text("Historial de Direcciones", fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text("Administra tus direcciones y mesas guardadas") },
                        leadingContent = {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { alNavegarAHistorialDirecciones() }
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))

                    // inicio navegacion: ir a mis pedidos activos
                    ListItem(
                        headlineContent = { Text("Pedidos Activos", fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text("Sigue el estado de tu pedido actual") },
                        leadingContent = {
                            Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { alNavegarAPedidos() }
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))

                    // inicio navegacion: ir a favoritos desde perfil
                    ListItem(
                        headlineContent = { Text("Mis Favoritos", fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text("Tus platillos guardados") },
                        leadingContent = {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { alNavegarAFavoritos() }
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))

                    // inicio navegacion: ir a nueva pantalla desde perfil
                    ListItem(
                        headlineContent = { Text("Nueva Pantalla", fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text("Escribe y registra un mensaje") },
                        leadingContent = {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { alNavegarANuevaPantalla() }
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))

                    // inicio navegacion: ir a pantalla confirmacion desde perfil
                    ListItem(
                        headlineContent = { Text("Pantalla Confirmación", fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text("Registra y confirma tu dirección") },
                        leadingContent = {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { alNavegarAConfirmacion() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // inicio boton: cerrar sesion visible
            Button(
                onClick = alCerrarSesion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar Sesión", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}
