# 📋 Especificación Técnica del Proyecto

Este documento resume las capacidades técnicas y la estructura de la **App de Gestión de Restaurante**, desarrollada con los estándares actuales de Android.

## ⚙️ Funcionalidades Implementadas

### Módulo de Cliente
- **Catálogo Dinámico:** Menú interactivo con precios en Soles (S/) y categorización de platos.
- **Carrito de Compras:** Sistema para agregar productos, calcular totales automáticamente y vaciado tras confirmar el pedido.
- **Historial de Pedidos:** Seguimiento de órdenes con numeración secuencial profesional (iniciando en #101).

### Módulo de Administración
- **Panel de Control:** Gestión de pedidos en tiempo real con cambio de estados (Recibido, En preparación, Listo).
- **Gestión de Inventario:** Herramienta para agregar, editar y eliminar platos del menú directamente desde la app.

## 🏗️ Arquitectura y Diseño
Para asegurar que la app sea fácil de mantener y escalar, se han aplicado los siguientes pilares:

- **Patrón MVVM:** Organización del código en tres capas (Datos, Lógica y Pantallas) para que el proyecto sea ordenado.
- **Repositorios Compartidos:** Uso de una "Fuente Única de Verdad" para que los cambios en el administrador se vean reflejados al instante en el cliente.
- **Interfaz Moderna:** Diseño basado en **Material Design 3**, optimizado para una navegación fluida con una barra inferior de 5 secciones.
- **Navegación Robusta:** Uso de *Compose Navigation* para transiciones rápidas entre pantallas sin pérdida de datos.

## 🛠️ Stack Tecnológico
- **Lenguaje:** Kotlin (moderno y seguro).
- **Interfaz:** Jetpack Compose (herramienta más actual de Google para UI).
- **Imágenes:** Librería Coil (carga rápida y eficiente de fotos).
- **Herramientas de Construcción:** Gradle con Kotlin DSL y Catálogo de Versiones para una gestión limpia de librerías.
- **Compatibilidad:** Android 8.0 o superior (API 26+).

---
*Este proyecto demuestra el dominio de las herramientas modernas de desarrollo Android y la capacidad para crear soluciones funcionales para el sector gastronómico.*
