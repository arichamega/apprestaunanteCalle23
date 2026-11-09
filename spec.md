# 📐 Especificación Técnica del Código (Spec) - Calle 28

Este documento detalla la estructura exacta de clases, modelos y pantallas implementadas en la **Plataforma Gastronómica Calle 28** de acuerdo con el modelo visual del prototipo.

---

## 📂 1. Modelos de Datos (`data/model/`)

### `Dish.kt` (Entidad de Platillo)
Representa un producto dentro del catálogo del menú, incluyendo su popularidad y tiempos estimados.
```kotlin
data class Dish(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = "",
    val isAvailable: Boolean = true,
    val isPopular: Boolean = false,
    val estimatedTime: Int = 15
)
```

### `Order.kt` (Entidad de Pedido)
Contiene la información de la comanda, cálculos fiscales y estados del flujo culinario.
```kotlin
data class Order(
    val id: String = "",
    val userId: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val status: OrderStatus = OrderStatus.RECEIVED,
    val timestamp: Long = System.currentTimeMillis(),
    val tableNumber: Int? = null
)
```

---

## 🗄️ 2. Arquitectura de Pantallas y Flujos de Usuario (`ui/`)

El sistema implementa el flujo visual exacto del prototipo a través de las siguientes pantallas nativas:

1. **Inicio (`SplashScreen`)**: Pantalla de bienvenida limpia con el logo de "Calle 28", eslogan corporativo (*"Sabor vibrante, experiencia única."*) y botón de acción directa.
2. **Autenticación (`LoginScreen` & `RegisterScreen`)**: Formularios estilizados en tonos terracota y crema para el control de acceso y registro de datos del cliente.
3. **Menú Principal (`MenuScreen`)**: Buscador interactivo superior, pestañas deslizantes de categorías y listado de platos con insignias de *"★ Popular"*.
4. **Carrito de Compras (`CartScreen`) / RF15**: Revisión de platos elegidos en la pestaña "Mi Pedido" con lista de productos agregados, cantidades, notas especiales y el Total calculado directamente en Soles (S/), mostrando estado vacío cuando no hay ítems en la bolsa.
5. **Estado del Pedido (`OrderStatusScreen`)**: Pantalla interactiva que muestra el indicador de *"¡En Cocina!"*, tiempo estimado de preparación y barra de progreso.
6. **Perfil del Usuario (`ProfileScreen`)**: Vista que incluye el nombre del cliente (*Ana Martínez*), sistema de puntos, cupones acumulados e historial de comandas pasadas.

---
*Nota: La app utiliza la paleta de colores oficial (Terracota #C75A2E y Crema #FFF6F0) bajo Material Design 3 en la API 37.*
