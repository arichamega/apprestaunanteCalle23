# 📋 Especificación Técnica de Habilidades y Requerimientos (RF01 - RF25)

Este documento resume las capacidades técnicas, requerimientos funcionales implementados y la estructura de la **Plataforma Gastronómica Calle 28**, desarrollada bajo los estándares modernos de Android Studio.

---

## ⚙️ Cobertura Completa de Requerimientos Funcionales (RF01 - RF25)

### 🔑 Autenticación, Seguridad y Perfil (RF01, RF02, RF04, RF05, RF06, RF07, RF08, RF21, RF23, RF24)
- **RF01 - Splash Screen y Verificación de Sesión:** Verificación automática de sesión al inicio.
- **RF02 - Pantalla de Bienvenida:** Presentación institucional con accesos a la app.
- **RF04 - Formulario de Crear Cuenta:** Registro de nuevos clientes.
- **RF05 - Validación de Campos en Registro:** Validación en tiempo real de correo `@gmail.com`, teléfono (9 dígitos) y contraseña.
- **RF06 - Verificación de Correo Duplicado:** Comprobación en base de datos impidiendo el registro si el correo ya existe.
- **RF07 - Inicio de Sesión:** Autenticación por correo y contraseña con asignación de rol (`CLIENTE` / `ADMIN`).
- **RF08 - Control de Error en Iniciar Sesión:** Mensaje claro de error (*"Correo o contraseña incorrectos"*) manteniendo al usuario en la vista.
- **RF21 - Visualización de Datos Personales:** Muestra del nombre, correo, Puntos Calle 28 y cupones en el perfil.
- **RF23 - Cierre de Sesión:** Salida segura y limpieza de SharedPreferences.
- **RF24 - Persistencia de Sesión Activa:** Reconocimiento de sesión en `SharedPreferences` para ingresar directamente sin autenticarse de nuevo.

### 🍽️ Catálogo, Personalización e Invitados (RF03, RF09, RF10, RF11, RF12, RF13)
- **RF03 - Restricción de Acción para Invitados:** Bloqueo de adición al carrito para usuarios no autenticados desplegando un `AlertDialog` emergente para Iniciar Sesión o Crear Cuenta.
- **RF09 - Barra de Navegación Inferior (Bottom Nav):** Menú reactivo adaptado a cada rol (`CLIENTE` o `ADMIN`).
- **RF10 - Visualización de Menú por Categorías:** Filtros por categorías (*Platos, Entradas, Bebidas, Postres, Favoritos*) e insignias *"Popular"* / *"Nuevo"*.
- **RF11 - Vista de Detalle del Plato:** Hoja de detalle (`HojaDetallePlato`) con foto HD, descripción y tiempo estimado.
- **RF12 - Personalización de Plato:** Campo de notas e indicaciones especiales (*"Sin cebolla", "Extra picante"*).
- **RF13 - Selección de Cantidad del Producto:** Controles incrementales/decrementales (`-` / `+`).

### 🛒 Carrito, Checkout y Pedidos (RF14, RF15, RF16, RF17, RF18, RF19, RF20, RF22, RF25)
- **RF14 - Acción "Agregar a la Bolsa":** Integración inmediata a la bolsa de compras.
- **RF15 - Revisión de Platos Elegidos en la Bolsa:** Vista de resumen de la bolsa con cálculo de Total en Soles (S/).
- **RF16 - Modificación de Ítems en el Carrito:** Ajuste de cantidades, eliminación de productos y recalculado automático del total.
- **RF17 - Selección/Ingreso del Número de Mesa:** Campo para registrar la mesa de entrega (ej. Mesa #5).
- **RF18 - Confirmación de Pedido:** Procesamiento con métodos de pago (*Yape / Plin, Tarjeta, Efectivo*) e ID secuencial (#C28-101) guardado en Firebase Firestore.
- **RF19 - Visualización de la Barra de Progreso en Tiempo Real:** Seguimiento gráfico (*Recibido → En Cocina → Servido*).
- **RF20 - Actualización Automática del Estado ("Servido"):** Sincronización en tiempo real entre la cocina y el cliente.
- **RF22 - Consulta del Historial de Consumo:** Pestaña "Mis Pedidos" con opción de modificar cantidades o cancelar comandas en preparación.
- **RF25 - Panel de Pedidos de Personal:** Control de comandas para personal administrativo/cocina y CRUD completo del catálogo del menú.

---

## 🛠️ Stack Tecnológico
- **Lenguaje:** Kotlin 2.2.10
- **UI:** Jetpack Compose con Material Design 3
- **Persistencia Remota:** Firebase Firestore / Firebase Auth
- **Persistencia Local:** `SharedPreferences` (`user_session_prefs`)
- **Navegación:** Compose Navigation
- **Imágenes:** Librería Coil
- **Compatibilidad:** Android 8.0+ (API 26+)
