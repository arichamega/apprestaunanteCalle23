package com.example.myapplication.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Login : Screen("login", "Acceso")
    object Menu : Screen("menu", "Menú", Icons.Default.RestaurantMenu)
    object Cart : Screen("cart", "Carrito", Icons.Default.ShoppingCart)
    object Orders : Screen("orders", "Pedidos", Icons.Default.History)
    object Admin : Screen("admin", "Admin", Icons.Default.Settings)
    object ManageMenu : Screen("manage_menu", "Gestión", Icons.Default.Edit)
}
