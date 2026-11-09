# Proyecto: App de Gestión y Pedidos de Restaurante

Este documento describe la arquitectura, requerimientos y estructura técnica de la aplicación desarrollada en Android Studio.

## 📌 Visión General
Una aplicación nativa para Android diseñada para optimizar el ciclo de pedidos en un restaurante, permitiendo a los clientes realizar compras y al personal administrativo gestionar el menú y las comandas en tiempo real.

---

## 🚀 Requerimientos Funcionales

### 1. Módulo de Cliente
- **Menú Interactivo:** Visualización de platillos organizados por categorías (Entradas, Fondos, Bebidas, Postres) con imágenes, descripciones y precios en Soles (S/).
- **Gestión de Carrito:** Capacidad de agregar productos, modificar cantidades y visualizar el total de la compra antes de confirmar.
- **Confirmación de Pedidos:** Proceso de "Checkout" que genera una orden con número secuencial (iniciando en #101).
- **Seguimiento en Tiempo Real:** Pantalla de "Mis Pedidos" donde el cliente puede ver el estado actual de su orden (Recibido → En preparación → Listo).

### 2. Módulo de Administración
- **Gestión de Menú (CRUD):** Interfaz para agregar nuevos platos, editar la información de los existentes (nombre, precio, categoría) o eliminarlos del sistema.
- **Panel de Cocina / Comandas:** Control total sobre los pedidos entrantes con capacidad de actualizar el estado de cada orden para informar al cliente.
- **Gestión de Identificadores:** Generación automática y simplificada de IDs de pedido para facilitar la comunicación entre salón y cocina.

---

## 🛠️ Especificaciones Técnicas

### Arquitectura de Software
- **Patrón MVVM (Model-View-ViewModel):** Separación estricta entre la lógica de negocio y la interfaz de usuario.
- **Patrón Repository:** Uso de repositorios centralizados (`OrderRepository`, `MenuRepository`) como fuentes únicas de verdad para los datos.
- **Reactividad:** Implementación de `StateFlow` para asegurar que los cambios en los datos (como un nuevo pedido) se reflejen instantáneamente en todas las pantallas.

### Stack Tecnológico
- **Lenguaje:** Kotlin 2.2.10
- **UI:** Jetpack Compose (Declarativa)
- **Diseño:** Material Design 3
- **Navegación:** Compose Navigation (múltiples destinos con barra inferior).
- **Carga de Imágenes:** Librería Coil para procesamiento eficiente.
- **Compatibilidad Mínima:** Android 8.0 (API nivel 26).

---

## 📂 Estructura del Proyecto
- `data/model/`: Definición de las entidades (Dish, Order, OrderItem).
- `data/repository/`: Lógica de persistencia y manejo de datos en memoria.
- `ui/menu/`: Pantalla principal de ventas y lógica del menú.
- `ui/cart/`: Lógica de cálculo de totales y procesamiento de pagos.
- `ui/orders/`: Historial de pedidos para el cliente.
- `ui/admin/`: Panel de control de cocina y edición del catálogo.
- `ui/navigation/`: Configuración de rutas y navegación entre pantallas.

---

## 📈 Próximos Pasos (Roadmap)
1. **Persistencia Real:** Integración con Firebase Firestore para almacenamiento en la nube.
2. **Autenticación:** Implementación de Firebase Auth para perfiles de usuario reales.
3. **Notificaciones:** Avisos push cuando el estado del pedido cambie a "Listo".
4. **Mesa/QR:** Asignación automática de pedidos mediante escaneo de códigos QR en mesas.
