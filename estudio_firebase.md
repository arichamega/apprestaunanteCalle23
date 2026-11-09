# 🎓 Guía de Estudio desde Cero: Firebase Authentication en Android

Este documento explica de forma sencilla y paso a paso cómo funciona el sistema de registro e inicio de sesión que acabamos de implementar con Firebase en tu aplicación.

---

## 📌 1. El Concepto Clave: ¿Qué es Firebase Auth?
En lugar de crear una base de datos propia para guardar contraseñas (lo cual es peligroso e inseguro), delegamos esto a **Google Firebase**. 
Firebase se encarga de:
- Encriptar y guardar los usuarios de forma segura.
- Validar si el correo y la contraseña coinciden.
- Controlar quién ha iniciado sesión en el teléfono.

---

## 🏗️ 2. ¿Dónde está el código de Firebase en tu proyecto?

El flujo se divide en **dos partes principales** que se comunican entre sí:

### Parte A: La Lógica de Datos (`data/repository/UserRepository.kt`)
Aquí es donde se manejan las funciones nativas de Firebase. Si abres este archivo, verás estos componentes clave:

1. **La Instancia de Firebase:**
   ```kotlin
   private val auth: FirebaseAuth = FirebaseAuth.getInstance()
   ```
   *Explicación:* Es la puerta de acceso. Esta línea le dice a la app: "Conéctate al servidor de Firebase asignado a este proyecto".

2. **La Función de Registro (`register`):**
   ```kotlin
   auth.createUserWithEmailAndPassword(email, pass)
   ```
   *Explicación:* Es un método propio de Firebase. Tú le pasas el correo y la clave, y Firebase crea el usuario automáticamente en la nube.
   - `.addOnCompleteListener`: Es un "escuchador". Como la app tiene que viajar por internet hasta los servidores de Google, esta función espera la respuesta y te avisa si la operación fue exitosa (`task.isSuccessful`) o si falló (por ejemplo, si el correo ya existía).

3. **La Función de Inicio de Sesión (`login`):**
   ```kotlin
   auth.signInWithEmailAndPassword(email, pass)
   ```
   *Explicación:* Le envía a Firebase las credenciales que el usuario escribió. Si existen y coinciden, Firebase le da "luz verde" a la app para entrar al menú.

---

### Parte B: La Interfaz de Usuario (`ui/auth/AuthScreen.kt`)
Esta es la pantalla visual que interactúa con el usuario. Aquí aplicamos tus reglas de negocio antes de llamar a Firebase:

1. **Las Restricciones Locales:**
   Antes de gastar internet enviando datos a Firebase, la app valida las condiciones que pediste:
   ```kotlin
   if (!email.endsWith("@gmail.com")) { ... } // Requisito de Gmail
   if (password.length < 8) { ... }           // Requisito de 8 dígitos
   ```

2. **El Estado de Carga (`isLoading`):**
   Como las peticiones a Firebase toman un par de segundos según la velocidad del internet, usamos una variable `isLoading`. Si es `true`, esconde el botón y muestra un círculo de progreso (`CircularProgressIndicator`), evitando que el usuario presione el botón muchas veces seguidas.

---

## 🧭 3. Pasos para que funcione en tu Consola de Firebase

Para que este código se conecte con tu cuenta de Google, debes seguir estos pasos en la web:

1. Ve a [Firebase Console](https://console.firebase.google.com/).
2. Crea un proyecto llamado **Calle 28**.
3. En el menú lateral izquierdo, ve a **Build** ➔ **Authentication**.
4. Haz clic en la pestaña **Sign-in method** (Método de inicio de sesión).
5. Selecciona **Correo electrónico/Contraseña** y dale a **Habilitar** (y guardar). ¡Este paso es obligatorio para que el código funcione!
6. Registra tu aplicación Android usando el paquete `com.example.myapplication`, descarga el archivo `google-services.json` y colócalo en la carpeta `app/` de tu proyecto.

---
*¡Felicidades por dar este paso! Entender este flujo te permitirá implementar sistemas de usuarios seguros en cualquier aplicación futura.*
