package com.example.myapplication.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.admin.AdminScreen
import com.example.myapplication.ui.admin.EditMenuScreen
import com.example.myapplication.ui.cart.CartScreen
import com.example.myapplication.ui.cart.CartViewModel
import com.example.myapplication.ui.menu.MenuScreen
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.orders.OrdersScreen

@Composable
fun MainScreen(
    cartViewModel: CartViewModel = viewModel()
) {
    val navController = rememberNavController()
    // Todas las pantallas disponibles directamente
    val screens = listOf(
        Screen.Menu, 
        Screen.Cart, 
        Screen.Orders, 
        Screen.Admin, 
        Screen.ManageMenu
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { screen.icon?.let { Icon(it, contentDescription = null) } },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Menu.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Menu.route) { 
                MenuScreen(
                    onAddToCart = { dish -> cartViewModel.addToCart(dish) },
                    onLogout = {} // Ya no hace nada
                ) 
            }
            composable(Screen.Cart.route) { 
                CartScreen(
                    viewModel = cartViewModel,
                    onLogout = {} 
                ) 
            }
            composable(Screen.Orders.route) { 
                OrdersScreen(onLogout = {}) 
            }
            composable(Screen.Admin.route) { 
                AdminScreen(onLogout = {}) 
            }
            composable(Screen.ManageMenu.route) { 
                EditMenuScreen(onLogout = {})
            }
        }
    }
}
