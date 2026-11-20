# Proyecto: App de Gestión y Pedidos de Restaurante - Calle 28

Este documento describe la arquitectura, requerimientos completos y estructura técnica de la **Plataforma Gastronómica Calle 28** desarrollada en Android Studio.

## 📌 Visión General
Aplicación Android nativa para optimizar el ciclo completo de pedidos en el restaurante **Calle 28**. Permite a los clientes explorar el menú, personalizar platillos, gestionar carritos/bolsa, realizar compras asociando mesas, y seguir comandas en tiempo real. También brinda al personal administrativo un panel de cocina para la gestión de comandas y un sistema CRUD de administración de menú con persistencia remota en la nube.

---

## 🚀 Lista Completa de Requerimientos Funcionales Implementados (RF01 - RF25)

### 🔑 Módulo de Autenticación, Sesión y Perfil
- **RF01 - Splash Screen y Verificación de Sesión:** Pantalla inicial de carga que verifica automáticamente si existe una sesión activa almacenada previamente.
- **RF02 - Pantalla de Bienvenida (Primera Vez):** Vista inicial con el logotipo oficial de "Calle 28", eslogan corporativo (*"Sabor vibrante, experiencia única."*) y accesos directos para explorar el menú o autenticarse.
- **RF03 - Restricción de Acción para Invitados:** Permite explorar libremente el menú a usuarios sin sesión. Si el invitado presiona *"Agregar a la bolsa"* o *"Agregar al Carrito"*, la acción se bloquea y se despliega un cuadro de diálogo emergente (`AlertDialog`) solicitando iniciar sesión o registrarse.
- **RF04 - Formulario de Crear Cuenta:** Interfaz para el registro de nuevos clientes solicitando Nombre Completo, Correo Electrónico, Teléfono (9 dígitos) y Contraseña.
- **RF05 - Validación de Campos en Registro:** Verificación estricta de formato en tiempo real (correo `@gmail.com`, teléfono de 9 dígitos iniciando en `9`, contraseña de mínimo 8 caracteres) con notificaciones e indicación visual de errores.
- **RF06 - Verificación de Correo Duplicado:** Consulta en el repositorio/base de datos para validar que el correo no se encuentre registrado previamente. Si ya existe, bloquea el registro e indica un mensaje claro de error (*"El correo electrónico ya está registrado"*).
- **RF07 - Inicio de Sesión con Correo y Contraseña:** Formulario de autenticación mediante credenciales con asignación automática de rol (`CLIENTE` o `ADMIN`).
- **RF08 - Control de Error en Iniciar Sesión:** Detección de credenciales inválidas. Muestra la alerta de error *"Correo o contraseña incorrectos"* sin cerrar la vista de login, permitiendo al usuario reintentar.
- **RF21 - Visualización de Datos Personales:** Sección en el Perfil del cliente que muestra el nombre completo, correo electrónico, Puntos Calle 28 acumulados y cupones disponibles.
- **RF23 - Cierre de Sesión:** Opción segura para cerrar la sesión actual, limpiando credenciales en memoria y SharedPreferences, redirigiendo al usuario a la pantalla de bienvenida.
- **RF24 - Persistencia de Sesión Activa:** Almacenamiento seguro del estado de sesión en `SharedPreferences` (`user_session_prefs`). Al reabrir la app, el sistema reconoce la sesión y redirige directamente a la pantalla principal sin solicitar credenciales nuevamente.

### 🍽️ Módulo de Menú y Catálogo
- **RF09 - Barra de Navegación Inferior (Bottom Nav):** Barra reactiva inferior para clientes (*Menú, Carrito, Estado, Pedidos, Perfil*) y administradores (*Pedidos, Historial, Gestión Menú*) adaptada dinámicamente según el rol.
- **RF10 - Visualización de Menú por Categorías:** Catálogo organizado en pestañas deslizantes (*Platos, Entradas, Bebidas, Postres, Favoritos*) con buscador en tiempo real e insignias de *"Favorito"* y *"Nuevo"*.
- **RF11 - Vista de Detalle del Plato:** Modal interactivo (`HojaDetallePlato`) que despliega la imagen en alta definición, descripción completa, precio en Soles (S/) y tiempo estimado de preparación.
- **RF12 - Personalización de Plato:** Campo de entrada de texto dentro del detalle del producto para ingresar indicaciones especiales (ej. *"Sin cebolla", "Extra picante", "Término medio"*).
- **RF13 - Selección de Cantidad del Producto:** Controles incrementales/decrementales (`-` / `+`) para seleccionar el número de unidades antes de añadir al pedido.

### 🛒 Módulo de Carrito, Bolsa y Compras
- **RF14 - Acción "Agregar a la Bolsa":** Incorporación inmediata del platillo seleccionado con su cantidad y notas personalizadas a la bolsa flotante/carrito.
- **RF15 - Revisión de Platos Elegidos en la Bolsa:** Pantalla de revisión que detalla los productos seleccionados, subtotales por ítem y el **Total Calculado** en Soles (S/), mostrando pantalla vacía con botón de retorno si no hay elementos.
- **RF16 - Modificación de Ítems en el Carrito:** Ajuste de cantidades (`-` / `+`), eliminación de productos individuales y recalculado automático e instantáneo del monto total.
- **RF17 - Selección/Ingreso del Número de Mesa:** Campo obligatorio para especificar el número de mesa de destino (ej. Mesa #5) antes de procesar la comanda.
- **RF18 - Confirmación de Pedido:** Procesamiento de compra asociando método de pago (*Yape / Plin, Tarjeta de Crédito/Débito, Efectivo*), asignación de ID secuencial (#C28-101) y guardado remoto en Firebase Firestore.

### 🚴 Módulo de Estado, Historial y Administración
- **RF19 - Visualización de la Barra de Progreso en Tiempo Real:** Indicador visual e interactivo del estado del pedido (*Recibido → En Cocina → Servido*) con tiempo estimado restante.
- **RF20 - Actualización Automática del Estado ("Servido"):** Sincronización automática de cambios de estado entre la cocina y la vista del cliente.
- **RF22 - Consulta del Historial de Consumo:** Vista en "Mis Pedidos" que lista todas las comandas activas y pasadas del cliente con opción de editar cantidades o cancelar pedidos en preparación.
- **RF25 - Panel de Pedidos de Personal:** Interfaz exclusiva de administración (`PantallaAdministrador` & `PantallaGestionMenu`) para que el personal de cocina gestione estados de comandas, marque pagos registrados y administre el catálogo del menú (CRUD de platos).

---

## 🛠️ Especificaciones Técnicas

### Arquitectura de Software
- **Patrón MVVM (Model-View-ViewModel):** Separación estricta entre la interfaz declarativa y la lógica de negocio.
- **Patrón Repository:** Repositorios centralizados (`RepositorioMenu`, `RepositorioPedidos`, `RepositorioUsuarios`) como fuentes únicas de verdad.
- **Reactividad:** Implementación de `StateFlow` y `MutableStateFlow` para sincronización en tiempo real.

### Stack Tecnológico
- **Lenguaje:** Kotlin 2.2.10
- **UI:** Jetpack Compose (Declarativa) con Material Design 3.
- **Persistencia Nube:** Firebase Firestore / Firebase Auth
- **Persistencia Local:** `SharedPreferences` (`user_session_prefs`)
- **Navegación:** Compose Navigation
- **Carga de Imágenes:** Librería Coil
- **Compatibilidad Mínima:** Android 8.0 (API nivel 26).

---

## 📂 Estructura del Proyecto
- `data/model/`: Entidades (`Plato.kt`, `Pedido.kt`, `CuentaUsuario`).
- `data/repository/`: Repositorios (`RepositorioMenu.kt`, `RepositorioPedidos.kt`, `RepositorioUsuarios.kt`).
- `ui/menu/`: Catálogo y detalle del plato (`PantallaMenu.kt`, `ViewModelMenu.kt`).
- `ui/cart/`: Carrito y checkout (`PantallaCarrito.kt`, `ViewModelCarrito.kt`, `ViewModelBolsa.kt`).
- `ui/orders/`: Estado y seguimiento de pedidos (`PantallaMisPedidos.kt`, `PantallaEstadoPedido.kt`).
- `ui/profile/`: Perfil e historiales (`PantallaPerfil.kt`, `PantallaHistorialPedidos.kt`, `PantallaHistorialDirecciones.kt`).
- `ui/auth/`: Bienvenida, Login, Registro y Recuperación (`PantallaAutenticacion.kt`).
- `ui/admin/`: Control de cocina y gestión del menú (`PantallaAdministrador.kt`, `PantallaGestionMenu.kt`, `ViewModelAdmin.kt`).
- `ui/navigation/`: Configuración de rutas y barra inferior (`Pantalla.kt`, `MainScreen.kt`).
