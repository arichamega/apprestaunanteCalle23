package com.example.myapplication.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

//  [DEFINICIÓN DE RUTAS DE NAVEGACIÓN]: Asigna la clave única ("ruta"), título e ícono de cada pantalla
sealed class Pantalla(val ruta: String, val titulo: String, val icono: ImageVector? = null) {
    object Inicio : Pantalla("inicio", "Inicio")
    object IniciarSesion : Pantalla("login", "Iniciar Sesión")
    object Registro : Pantalla("registro", "Crear Cuenta")
    object Menu : Pantalla("menu", "Menú", Icons.Default.RestaurantMenu)
    object Carrito : Pantalla("carrito", "Carrito", Icons.Default.ShoppingCart)
    object EstadoPedido : Pantalla("estado_pedido", "Estado", Icons.Default.BorderColor)
    object MisPedidos : Pantalla("mis_pedidos", "Pedidos", Icons.Default.History)
    object Perfil : Pantalla("perfil", "Perfil", Icons.Default.Person)
    object HistorialPedidosCliente : Pantalla("historial_pedidos_cliente", "Historial de Pedidos", Icons.Default.History)
    object HistorialDirecciones : Pantalla("historial_direcciones", "Mis Direcciones", Icons.Default.LocationOn)
    object Promociones : Pantalla("promociones", "Promociones", Icons.Default.LocalOffer)
    object NuevaPantalla : Pantalla("nueva_pantalla", "Nueva Pantalla", Icons.Default.Edit)

    object PantallaConfirmaciones : Pantalla("confirmaciones", "Confirmación", Icons.Default.Edit)
    object Administrador : Pantalla("admin", "Pedidos", Icons.AutoMirrored.Filled.ReceiptLong)
    object HistorialAdmin : Pantalla("historial_admin", "Historial", Icons.Default.History)
    object GestionMenu : Pantalla("gestion_menu", "Gestión Menú", Icons.Default.RestaurantMenu)
}
