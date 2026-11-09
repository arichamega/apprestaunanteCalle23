package com.example.myapplication.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.myapplication.data.repository.RepositorioUsuarios
import com.example.myapplication.ui.admin.PantallaAdministrador
import com.example.myapplication.ui.admin.PantallaGestionMenu
import com.example.myapplication.ui.auth.PantallaBienvenida
import com.example.myapplication.ui.auth.PantallaIniciarSesion
import com.example.myapplication.ui.auth.PantallaRecuperarCuenta
import com.example.myapplication.ui.auth.PantallaRegistroCuenta
import com.example.myapplication.ui.cart.PantallaCarrito
import com.example.myapplication.ui.cart.ViewModelCarrito
import com.example.myapplication.ui.menu.PantallaMenu
import com.example.myapplication.ui.navigation.Pantalla
import com.example.myapplication.ui.orders.PantallaEstadoPedido
import com.example.myapplication.ui.orders.PantallaMisPedidos
import com.example.myapplication.ui.profile.PantallaConfirmacion
import com.example.myapplication.ui.profile.PantallaHistorialDirecciones
import com.example.myapplication.ui.profile.PantallaHistorialPedidos
import com.example.myapplication.ui.profile.PantallaNueva
import com.example.myapplication.ui.profile.PantallaPerfil
import com.example.myapplication.ui.profile.PantallaPromociones

// inicio navegacion: contenedor principal navhost
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModelCarrito: ViewModelCarrito = viewModel()
) {
    val controladorNavegacion = rememberNavController()
    val nombreUsuarioActual by RepositorioUsuarios.nombreUsuarioActual.collectAsState()
    val sesionIniciada = nombreUsuarioActual.isNotEmpty()

    var pasoActual by remember { 
        mutableStateOf(
            if (sesionIniciada) "app" else "splash"
        ) 
    }

    var rolUsuario by remember { 
        mutableStateOf<String?>(
            if (sesionIniciada) {
                if (RepositorioUsuarios.correoUsuarioActual?.contains("admin") == true) "ADMIN" else "CLIENTE"
            } else null
        ) 
    }

    LaunchedEffect(sesionIniciada) {
        if (!sesionIniciada) {
            rolUsuario = null
        }
    }

    val elementosCarrito by viewModelCarrito.items.collectAsState()
    val cantidadElementosCarrito = elementosCarrito.sumOf { it.cantidad }

    var categoriaSeleccionadaMenu by remember { mutableStateOf("Platos") }

    // inicio accion: cerrar sesion usuario
    val manejarCerrarSesion = {
        RepositorioUsuarios.cerrarSesion()
        rolUsuario = null
        pasoActual = "splash"
    }

    if (pasoActual == "splash") {
        // inicio navegacion: pantalla bienvenida
        PantallaBienvenida(
            alExplorarMenu = { pasoActual = "app" },
            alIniciarSesion = { pasoActual = "login" }
        )
    } else if (pasoActual == "login") {
        // inicio navegacion: pantalla iniciar sesion
        PantallaIniciarSesion(
            alIniciarSesionExitoso = { rol ->
                rolUsuario = rol
                pasoActual = "app"
            },
            alHacerClicEnRegistrarse = { pasoActual = "register" },
            alHacerClicEnRecuperarContrasena = { pasoActual = "forgot_password" }
        )
    } else if (pasoActual == "register") {
        // inicio navegacion: pantalla registro
        PantallaRegistroCuenta(
            alRegistrarExitoso = { rol ->
                rolUsuario = rol
                pasoActual = "app"
            },
            alIniciarSesionClick = { pasoActual = "login" }
        )
    } else if (pasoActual == "forgot_password") {
        // inicio navegacion: pantalla recuperar cuenta
        PantallaRecuperarCuenta(
            alVolverAlLogin = { pasoActual = "login" }
        )
    } else {
        val pantallas = if (rolUsuario == "ADMIN") {
            listOf(Pantalla.Administrador, Pantalla.HistorialAdmin, Pantalla.GestionMenu)
        } else {
            listOf(Pantalla.Menu, Pantalla.Carrito, Pantalla.EstadoPedido, Pantalla.MisPedidos, Pantalla.Perfil)
        }

        Scaffold(
            bottomBar = {
                // inicio navegacion: barra de navegacion inferior
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    val entradaPilaNavegacion by controladorNavegacion.currentBackStackEntryAsState()
                    val destinoActual = entradaPilaNavegacion?.destination
                    pantallas.forEach { pantalla ->
                        // inicio boton: item navegacion
                        NavigationBarItem(
                            icon = { 
                                if (pantalla == Pantalla.Carrito && cantidadElementosCarrito > 0) {
                                    BadgedBox(
                                        badge = { Badge { Text("$cantidadElementosCarrito") } }
                                    ) {
                                        pantalla.icono?.let { Icon(it, contentDescription = null) }
                                    }
                                } else {
                                    pantalla.icono?.let { Icon(it, contentDescription = null) }
                                }
                            },
                            label = { Text(pantalla.titulo) },
                            selected = destinoActual?.hierarchy?.any { it.route == pantalla.ruta } == true,
                            onClick = {
                                if (pantalla == Pantalla.Menu) {
                                    categoriaSeleccionadaMenu = "Platos"
                                }
                                if ((pantalla == Pantalla.Carrito || pantalla == Pantalla.Perfil || pantalla == Pantalla.EstadoPedido) && !sesionIniciada) {
                                    pasoActual = "login"
                                } else {
                                    // navegacion: cambiar de pantalla
                                    controladorNavegacion.navigate(pantalla.ruta) {
                                        popUpTo(controladorNavegacion.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            // inicio navegacion: declaracion de rutas navhost
            NavHost(
                navController = controladorNavegacion,
                startDestination = if (rolUsuario == "ADMIN") Pantalla.Administrador.ruta else Pantalla.Menu.ruta,
                modifier = Modifier.padding(innerPadding)
            ) {
                // inicio navegacion: ruta promociones
                composable(Pantalla.Promociones.ruta) {
                    PantallaPromociones(
                        alVolverAPerfil = {
                            controladorNavegacion.navigate(Pantalla.Perfil.ruta)
                        },
                        alVolverAlMenu = {
                            controladorNavegacion.navigate(Pantalla.Menu.ruta)
                        }
                    )
                }
                // inicio navegacion: ruta menu
                composable(Pantalla.Menu.ruta) { 
                    PantallaMenu(
                        categoriaInicial = categoriaSeleccionadaMenu,
                        viewModelCarrito = viewModelCarrito,
                        alAgregarAlCarrito = { plato, cantidad, notas -> 
                            if (sesionIniciada) {
                                viewModelCarrito.agregarAlCarrito(plato, cantidad, notas)
                            } else {
                                pasoActual = "login"
                            }
                        },
                        alNavegarAlCarrito = {
                            if (sesionIniciada) {
                                // navegacion: ir a carrito
                                controladorNavegacion.navigate(Pantalla.Carrito.ruta)
                            } else {
                                pasoActual = "login"
                            }
                        },
                        alCerrarSesion = manejarCerrarSesion
                    ) 
                }
                // inicio navegacion: ruta carrito
                composable(Pantalla.Carrito.ruta) { 
                    PantallaCarrito(
                        titulo = "Mi Carrito",
                        viewModel = viewModelCarrito,
                        alRealizarPedido = {
                            // navegacion: ir a estado pedido
                            controladorNavegacion.navigate(Pantalla.EstadoPedido.ruta) {
                                popUpTo(Pantalla.Menu.ruta)
                            }
                        },
                        alVolverAlMenu = { 
                            // navegacion: volver a menu
                            controladorNavegacion.navigate(Pantalla.Menu.ruta)
                        },
                        alNavegarAPromociones = {
                            // navegacion: ir a promociones
                            controladorNavegacion.navigate(Pantalla.Promociones.ruta)
                        }
                    ) 
                }
                // inicio navegacion: ruta estado pedido
                composable(Pantalla.EstadoPedido.ruta) { 
                    PantallaEstadoPedido(
                        alVolverAlMenu = {
                            // navegacion: volver a menu desde estado
                            controladorNavegacion.navigate(Pantalla.Menu.ruta) {
                                popUpTo(Pantalla.Menu.ruta)
                            }
                        }
                    )
                }
                // inicio navegacion: ruta mis pedidos
                composable(Pantalla.MisPedidos.ruta) { 
                    PantallaMisPedidos(alCerrarSesion = manejarCerrarSesion)
                }
                // inicio navegacion: ruta perfil
                composable(Pantalla.Perfil.ruta) { 
                    PantallaPerfil(
                        alCerrarSesion = manejarCerrarSesion,
                        alNavegarAFavoritos = {
                            categoriaSeleccionadaMenu = "Favoritos"
                            // navegacion: ir a favoritos en menu
                            controladorNavegacion.navigate(Pantalla.Menu.ruta)
                        },
                        alNavegarAPedidos = {
                            // navegacion: ir a mis pedidos
                            controladorNavegacion.navigate(Pantalla.MisPedidos.ruta)
                        },
                        alNavegarAHistorialPedidos = {
                            // navegacion: ir a historial de pedidos
                            controladorNavegacion.navigate(Pantalla.HistorialPedidosCliente.ruta)
                        },
                        alNavegarAHistorialDirecciones = {
                            // navegacion: ir a historial de direcciones
                            controladorNavegacion.navigate(Pantalla.HistorialDirecciones.ruta)
                        },
                        alNavegarAPromociones = {
                            // navegacion: ir a promociones
                            controladorNavegacion.navigate(Pantalla.Promociones.ruta)
                        },
                        alNavegarANuevaPantalla = {
                            // navegacion: ir a nueva pantalla
                            controladorNavegacion.navigate(Pantalla.NuevaPantalla.ruta)
                        },
                        alNavegarAConfirmacion = {
                            // navegacion: ir a pantalla confirmaciones
                            controladorNavegacion.navigate(Pantalla.PantallaConfirmaciones.ruta)
                        }
                    )
                }
                // inicio navegacion: ruta pantalla confirmaciones
                composable(Pantalla.PantallaConfirmaciones.ruta) {
                    PantallaConfirmacion(
                        alVolverAPerfil = {
                            controladorNavegacion.navigate(Pantalla.Perfil.ruta)
                        }
                    )
                }
                // inicio navegacion: ruta nueva pantalla
                composable(Pantalla.NuevaPantalla.ruta) {
                    PantallaNueva(
                        alVolverAPerfil = {
                            controladorNavegacion.navigate(Pantalla.Perfil.ruta)
                        }
                    )
                }
                // inicio navegacion: ruta historial de pedidos cliente
                composable(Pantalla.HistorialPedidosCliente.ruta) {
                    PantallaHistorialPedidos(
                        alVolverAlPerfil = {
                            // navegacion: volver a perfil
                            controladorNavegacion.navigate(Pantalla.Perfil.ruta) {
                                popUpTo(Pantalla.Perfil.ruta) { inclusive = true }
                            }
                        }
                    )
                }
                // inicio navegacion: ruta historial de direcciones
                composable(Pantalla.HistorialDirecciones.ruta) {
                    PantallaHistorialDirecciones(
                        alVolverAlPerfil = {
                            // navegacion: volver a perfil
                            controladorNavegacion.navigate(Pantalla.Perfil.ruta) {
                                popUpTo(Pantalla.Perfil.ruta) { inclusive = true }
                            }
                        }
                    )
                }
                // inicio navegacion: ruta admin comandas
                composable(Pantalla.Administrador.ruta) { 
                    PantallaAdministrador(mostrarSoloHistorial = false, alCerrarSesion = manejarCerrarSesion) 
                }
                // inicio navegacion: ruta admin historial
                composable(Pantalla.HistorialAdmin.ruta) { 
                    PantallaAdministrador(mostrarSoloHistorial = true, alCerrarSesion = manejarCerrarSesion)
                }
                // inicio navegacion: ruta admin gestion menu
                composable(Pantalla.GestionMenu.ruta) { 
                    PantallaGestionMenu(alCerrarSesion = manejarCerrarSesion)
                }
            }
        }
    }
}
