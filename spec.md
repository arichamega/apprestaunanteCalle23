# 📐 Especificación Técnica del Código (Spec) - Calle 28

Este documento detalla la estructura exacta de clases, modelos, repositorios, requerimientos funcionales (RF01 - RF25) y pantallas implementadas en la **Plataforma Gastronómica Calle 28**.

---

## 📂 1. Modelos de Datos (`data/model/`)

### `Plato.kt` (Entidad de Platillo)
Representa un producto dentro del catálogo del menú, incluyendo insignias de popularidad, novedad, favoritos y tiempos estimados.
```kotlin
data class Plato(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val urlImagen: String = "",
    val categoria: String = "Platos",
    val esPopular: Boolean = false,
    val esNuevo: Boolean = false,
    val esFavorito: Boolean = false,
    val tiempoEstimado: Int = 15
)
```

### `Pedido.kt` (Entidad de Comanda)
Contiene la información de la comanda, lista de ítems con notas de personalización, mesa destino, cálculo de total y estados del flujo culinario.
```kotlin
data class Pedido(
    val id: String = "",
    val idUsuario: String = "",
    val items: List<ItemPedido> = emptyList(),
    val precioTotal: Double = 0.0,
    val estado: EstadoPedido = EstadoPedido.RECIBIDO,
    val estaPagado: Boolean = false,
    val marcaTiempo: Long = System.currentTimeMillis(),
    val numeroMesa: Int? = null
)

data class ItemPedido(
    val idPlato: String = "",
    val nombrePlato: String = "",
    val cantidad: Int = 1,
    val precio: Double = 0.0,
    val notas: String = ""
)

enum class EstadoPedido(val nombreMostrado: String) {
    RECIBIDO("Recibido"),
    EN_COCINA("En Cocina"),
    SERVIDO("Servido"),
    CANCELADO("Cancelado")
}
```

### `CuentaUsuario` (Entidad de Autenticación)
```kotlin
data class CuentaUsuario(
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val telefono: String = "",
    val rol: String = "CLIENTE"
)
```

---

## 🗄️ 2. Mapeo de Requerimientos Funcionales (RF01 - RF25)

| Código | Nombre del Requerimiento | Componente / Ubicación en Código |
| :--- | :--- | :--- |
| **RF01** | Splash Screen y Verificación de Sesión | `MainActivity.kt` / `RepositorioUsuarios.init()` |
| **RF02** | Pantalla de Bienvenida (Primera Vez) | `PantallaAutenticacion.kt` (`PantallaBienvenida`) |
| **RF03** | Restricción de Acción para Invitados | `PantallaMenu.kt` (`mostrarDialogoInvitado` - `AlertDialog`) |
| **RF04** | Formulario de Crear Cuenta | `PantallaAutenticacion.kt` (`PantallaRegistroCuenta`) |
| **RF05** | Validación de Campos en Registro | `PantallaAutenticacion.kt` (Formatos @gmail.com, 9 dígitos, min 8 caracteres) |
| **RF06** | Verificación de Correo Duplicado | `RepositorioUsuarios.kt` (`registrar()`) |
| **RF07** | Inicio de Sesión con Correo y Contraseña | `PantallaAutenticacion.kt` (`PantallaIniciarSesion`) |
| **RF08** | Control de Error en Iniciar Sesión | `PantallaAutenticacion.kt` / `RepositorioUsuarios.kt` |
| **RF09** | Barra de Navegación Inferior (Bottom Nav) | `MainScreen.kt` (`NavigationBar`) |
| **RF10** | Visualización de Menú por Categorías | `PantallaMenu.kt` (`platosFiltrados`) |
| **RF11** | Vista de Detalle del Plato | `PantallaMenu.kt` (`HojaDetallePlato`) |
| **RF12** | Personalización de Plato | `PantallaMenu.kt` (`OutlinedTextField` de notas) |
| **RF13** | Selección de Cantidad del Producto | `PantallaMenu.kt` (Controes `-` / `+`) |
| **RF14** | Acción "Agregar a la Bolsa" | `ViewModelBolsa.kt` / `ViewModelCarrito.kt` |
| **RF15** | Revisión de Platos Elegidos en la Bolsa | `PantallaCarrito.kt` / `PantallaBolsa.kt` |
| **RF16** | Modificación de Ítems en el Carrito | `PantallaCarrito.kt` / `PantallaMisPedidos.kt` |
| **RF17** | Selección/Ingreso del Número de Mesa | `PantallaCarrito.kt` (`textoNumeroMesa`) |
| **RF18** | Confirmación de Pedido | `RepositorioPedidos.kt` (`realizarPedido()`) |
| **RF19** | Visualización de la Barra de Progreso en Tiempo Real | `PantallaEstadoPedido.kt` |
| **RF20** | Actualización Automática del Estado ("Servido") | `RepositorioPedidos.kt` / `PantallaAdministrador.kt` |
| **RF21** | Visualización de Datos Personales | `PantallaPerfil.kt` |
| **RF22** | Consulta del Historial de Consumo | `PantallaMisPedidos.kt` |
| **RF23** | Cierre de Sesión | `RepositorioUsuarios.kt` (`cerrarSesion()`) |
| **RF24** | Persistencia de Sesión Activa | `RepositorioUsuarios.kt` (`user_session_prefs`) |
| **RF25** | Panel de pedidos de personal | `PantallaAdministrador.kt` & `PantallaGestionMenu.kt` |

---

## 🗄️ 3. Arquitectura de Pantallas y Flujos de Usuario (`ui/`)

1. **`PantallaBienvenida` (RF02)**: Pantalla de bienvenida con el logotipo corporativo de "Calle 28" y opciones de entrada.
2. **`PantallaIniciarSesion` (RF07, RF08)**: Control de login con notificación de credenciales incorrectas.
3. **`PantallaRegistroCuenta` (RF04, RF05, RF06)**: Formulario con validación de expresiones y detección de correos duplicados.
4. **`PantallaMenu` (RF03, RF10, RF11, RF12, RF13, RF14)**: Catálogo categorizado con hoja de detalle, notas de personalización, control de cantidad y bloqueo con `AlertDialog` para invitados.
5. **`PantallaCarrito` (RF15, RF16, RF17, RF18)**: Revisión de bolsa, ingreso de mesa destino, selección de pago y confirmación con recalculado automático.
6. **`PantallaEstadoPedido` (RF19, RF20)**: Indicador de progreso de comanda en tiempo real.
7. **`PantallaMisPedidos` (RF16, RF22)**: Historial completo del cliente con ajuste de cantidades, eliminación de productos y cancelación de orden.
8. **`PantallaPerfil` (RF21, RF23, RF24)**: Datos personales, puntos acumulados, direcciones guardadas y cierre de sesión.
9. **`PantallaAdministrador` & `PantallaGestionMenu` (RF25)**: Control de cocina de personal y CRUD de platillos.

---
*Nota: La aplicación utiliza Material Design 3 en Kotlin 2.2.10 y Jetpack Compose.*
