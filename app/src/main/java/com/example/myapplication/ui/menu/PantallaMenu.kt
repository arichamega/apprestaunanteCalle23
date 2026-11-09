package com.example.myapplication.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Icecream
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WineBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.data.model.Plato
import com.example.myapplication.ui.cart.VistaBolsaFlotante
import com.example.myapplication.ui.cart.ViewModelBolsa
import com.example.myapplication.ui.cart.ViewModelCarrito
import com.example.myapplication.ui.components.LogoCalle28
import kotlinx.coroutines.launch
import java.util.Locale

// 🚩 [PANTALLA MENU]: Catálogo interactivo de platillos por categorías
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMenu(
    categoriaInicial: String = "Platos",
    viewModelCarrito: ViewModelCarrito = viewModel(),
    viewModelBolsa: ViewModelBolsa = viewModel(),
    viewModelMenu: ViewModelMenu = viewModel(),
    alAgregarAlCarrito: (Plato, Int, String) -> Unit,
    alNavegarAlCarrito: () -> Unit = {},
    alCerrarSesion: () -> Unit = {}
) {
    val platos by viewModelMenu.platos.collectAsState()
    val elementosBolsa by viewModelBolsa.items.collectAsState()
    val cantidadElementosBolsa = elementosBolsa.sumOf { it.cantidad }

    var categoriaSeleccionada by remember(categoriaInicial) { mutableStateOf(categoriaInicial) }
    var consultaBusqueda by remember { mutableStateOf("") }
    var platoSeleccionadoDetalle by remember { mutableStateOf<Plato?>(null) }
    var mostrarHojaBolsa by remember { mutableStateOf(false) }

    val estadoSnackbar = remember { SnackbarHostState() }
    val alcanceCorrutina = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(estadoSnackbar) },
        topBar = {
            TopAppBar(
                title = { 
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LogoCalle28(size = 35)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Calle 28", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    // inicio boton: abrir bolsa flotante
                    IconButton(onClick = { mostrarHojaBolsa = true }) {
                        if (cantidadElementosBolsa > 0) {
                            BadgedBox(
                                badge = { Badge { Text("$cantidadElementosBolsa") } }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = "Bolsa de Compras",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = "Bolsa de Compras",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
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
            OutlinedTextField(
                value = consultaBusqueda,
                onValueChange = { consultaBusqueda = it },
                placeholder = { Text("¿Qué se te antoja hoy?") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White, 
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val datosCategorias = listOf(
                    Triple("Platos", Icons.Default.Restaurant, "Platos"),
                    Triple("Entradas", Icons.Default.Fastfood, "Entradas"),
                    Triple("Bebidas", Icons.Default.WineBar, "Bebidas"),
                    Triple("Postres", Icons.Default.Icecream, "Postres"),
                    Triple("Favoritos", Icons.Default.Star, "Favoritos")
                )
                items(datosCategorias) { (etiqueta, icono, categoria) ->
                    val estaSeleccionado = categoria == categoriaSeleccionada
                    // inicio boton: seleccionar categoria
                    Surface(
                        onClick = { categoriaSeleccionada = categoria },
                        color = if (estaSeleccionado) MaterialTheme.colorScheme.primary else Color.White,
                        shape = RoundedCornerShape(20.dp),
                        tonalElevation = 2.dp,
                        modifier = Modifier.height(40.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = icono, 
                                contentDescription = null, 
                                modifier = Modifier.size(18.dp),
                                tint = if (estaSeleccionado) Color.White else Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = etiqueta, 
                                color = if (estaSeleccionado) Color.White else Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = if (consultaBusqueda.isNotEmpty()) "Resultados de búsqueda" else if (categoriaSeleccionada == "Favoritos") "Mis Favoritos Guardados" else "Populares",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            val platosFiltrados = platos.filter { plato ->
                val coincideBusqueda = plato.nombre.contains(consultaBusqueda, ignoreCase = true) || 
                                       plato.descripcion.contains(consultaBusqueda, ignoreCase = true)
                
                if (consultaBusqueda.isNotEmpty()) {
                    coincideBusqueda
                } else {
                    if (categoriaSeleccionada == "Favoritos") {
                        plato.esFavorito
                    } else {
                        plato.categoria == categoriaSeleccionada
                    }
                }
            }

            if (platosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (categoriaSeleccionada == "Favoritos") Icons.Default.FavoriteBorder else Icons.Default.Restaurant,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (categoriaSeleccionada == "Favoritos") "Aún no tienes platillos guardados en Favoritos" else "No se encontraron platillos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (categoriaSeleccionada == "Favoritos") "Toca el ícono del corazón ❤️ en cualquier plato para guardarlo aquí." else "Intenta buscar con otra palabra.",
                            color = Color.Gray,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(platosFiltrados) { plato ->
                        ElementoPlato(
                            plato = plato, 
                            alHacerClic = { platoSeleccionadoDetalle = plato },
                            alAgregarAlCarrito = {
                                alAgregarAlCarrito(plato, 1, "")
                                alcanceCorrutina.launch {
                                    estadoSnackbar.showSnackbar("¡${plato.nombre} agregado al Carrito!")
                                }
                            },
                            alAgregarABolsa = {
                                viewModelBolsa.agregarABolsa(plato, 1, "")
                                alcanceCorrutina.launch {
                                    estadoSnackbar.showSnackbar("¡${plato.nombre} agregado a la Bolsa!")
                                }
                            },
                            alAlternarFavorito = { viewModelMenu.alternarFavorito(plato.id) }
                        )
                    }
                }
            }
        }

        platoSeleccionadoDetalle?.let { plato ->
            // RF12 / RF13: Apertura de hoja de detalle para personalizar notas y elegir cantidad
            HojaDetallePlato(
                plato = plato,
                alCerrar = { platoSeleccionadoDetalle = null },
                alAgregarAlCarrito = { p, cantidad, notas ->
                    alAgregarAlCarrito(p, cantidad, notas)
                    alcanceCorrutina.launch {
                        val mensaje = if (cantidad > 1) "¡${cantidad}× ${p.nombre} agregados al Carrito!" else "¡${p.nombre} agregado al Carrito!"
                        estadoSnackbar.showSnackbar(mensaje)
                    }
                },
                alAgregarABolsa = { p, cantidad, notas ->
                    viewModelBolsa.agregarABolsa(p, cantidad, notas)
                    alcanceCorrutina.launch {
                        val mensaje = if (cantidad > 1) "¡${cantidad}× ${p.nombre} agregados a la Bolsa!" else "¡${p.nombre} agregado a la Bolsa!"
                        estadoSnackbar.showSnackbar(mensaje)
                    }
                }
            )
        }

        if (mostrarHojaBolsa) {
            // inicio navegacion: vista bolsa flotante
            VistaBolsaFlotante(
                viewModelBolsa = viewModelBolsa,
                viewModelCarrito = viewModelCarrito,
                alCerrar = { mostrarHojaBolsa = false },
                alNavegarAlCarrito = alNavegarAlCarrito
            )
        }
    }
}

@Composable
fun ElementoPlato(
    plato: Plato,
    alHacerClic: () -> Unit,
    alAgregarAlCarrito: (Plato) -> Unit,
    alAgregarABolsa: (Plato) -> Unit,
    alAlternarFavorito: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = alHacerClic),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.LightGray)
            ) {
                val contexto = LocalContext.current
                val modeloImagen: Any = remember(plato.urlImagen) {
                    val url = plato.urlImagen.trim()
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        ImageRequest.Builder(contexto)
                            .data(url)
                            .addHeader("User-Agent", "Mozilla/5.0")
                            .crossfade(true)
                            .build()
                    } else if (url.isNotEmpty()) {
                        val nombreLimpio = url.substringBeforeLast(".").lowercase()
                        val resId = contexto.resources.getIdentifier(nombreLimpio, "drawable", contexto.packageName)
                        if (resId != 0) resId else "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800"
                    } else {
                        "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800"
                    }
                }
                AsyncImage(
                    model = modeloImagen,
                    contentDescription = plato.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // inicio boton: alternar favorito
                IconButton(
                    onClick = alAlternarFavorito,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                        .size(36.dp)
                ) {
                    Icon(
                        imageVector = if (plato.esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (plato.esFavorito) Color.Red else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                val textoEtiqueta = when {
                    plato.esPopular -> "Favorito"
                    plato.esNuevo -> "Nuevo"
                    else -> null
                }
                
                if (textoEtiqueta != null) {
                    Surface(
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFFF8A65).copy(alpha = 0.9f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (plato.esPopular) Icons.Default.Favorite else Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(textoEtiqueta, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = plato.nombre, 
                    style = MaterialTheme.typography.titleLarge, 
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "S/ ${plato.precio.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = plato.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 2,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // inicio boton: a carrito directo
                OutlinedButton(
                    onClick = { alAgregarAlCarrito(plato) },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("A Carrito", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                // inicio boton: a la bolsa directo
                Button(
                    onClick = { alAgregarABolsa(plato) },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("A la Bolsa", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// 🚩 [RF12 / RF13]: HOJA MODAL DE DETALLE PARA PERSONALIZACIÓN Y SELECCIÓN DE CANTIDAD
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HojaDetallePlato(
    plato: Plato,
    alCerrar: () -> Unit,
    alAgregarAlCarrito: (Plato, Int, String) -> Unit,
    alAgregarABolsa: (Plato, Int, String) -> Unit
) {
    // RF13: Estado reactivo de cantidad seleccionada
    var cantidad by remember { mutableIntStateOf(1) }
    // RF12: Estado reactivo de notas e indicaciones de personalización
    var notas by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = alCerrar,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.LightGray)
            ) {
                val contexto = LocalContext.current
                val modeloImagen: Any = remember(plato.urlImagen) {
                    val url = plato.urlImagen.trim()
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        ImageRequest.Builder(contexto)
                            .data(url)
                            .addHeader("User-Agent", "Mozilla/5.0")
                            .crossfade(true)
                            .build()
                    } else if (url.isNotEmpty()) {
                        val nombreLimpio = url.substringBeforeLast(".").lowercase()
                        val resId = contexto.resources.getIdentifier(nombreLimpio, "drawable", contexto.packageName)
                        if (resId != 0) resId else "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800"
                    } else {
                        "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800"
                    }
                }
                AsyncImage(
                    model = modeloImagen,
                    contentDescription = plato.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "⏱ ${plato.tiempoEstimado} min",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = plato.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "S/ ${String.format(Locale.US, "%.2f", plato.precio)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = plato.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🚩 [RF12 - PERSONALIZACIÓN DE PLATO]: Campo para escribir indicaciones personalizadas (sin cebolla, extra salsa, etc.)
            OutlinedTextField(
                value = notas,
                onValueChange = { notas = it },
                label = { Text("Notas / Indicaciones especiales") },
                placeholder = { Text("Ej. Sin cebolla, extra picante, término medio, etc.") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🚩 [RF13 - SELECCIÓN DE CANTIDAD DEL PRODUCTO]: Botones (-) y (+) para ajustar la cantidad antes de agregar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cantidad",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // inicio boton: disminuir cantidad
                    IconButton(
                        onClick = { if (cantidad > 1) cantidad-- },
                        enabled = cantidad > 1
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Disminuir")
                    }

                    Text(
                        text = "$cantidad",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    // inicio boton: aumentar cantidad
                    IconButton(onClick = { cantidad++ }) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val totalPlato = plato.precio * cantidad

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // inicio boton: a carrito con cantidad y notas guardadas
                OutlinedButton(
                    onClick = {
                        alAgregarAlCarrito(plato, cantidad, notas)
                        alCerrar()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("A Carrito", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                // inicio boton: a la bolsa con cantidad y notas guardadas
                Button(
                    onClick = {
                        alAgregarABolsa(plato, cantidad, notas)
                        alCerrar()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("A la Bolsa • S/ ${String.format(Locale.US, "%.0f", totalPlato)}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
