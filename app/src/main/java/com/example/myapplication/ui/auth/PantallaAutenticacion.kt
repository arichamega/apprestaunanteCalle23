package com.example.myapplication.ui.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.repository.RepositorioUsuarios
import com.example.myapplication.ui.components.LogoCalle28

// 🚩 [PANTALLA BIENVENIDA]: Pantalla de Bienvenida Inicial
@Composable
fun PantallaBienvenida(alExplorarMenu: () -> Unit, alIniciarSesion: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            LogoCalle28(size = 140)
            
            Text(
                text = "Sabor vibrante, experiencia única.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Medium
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // inicio boton: explorar menu
                Button(
                    onClick = alExplorarMenu,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Explorar menú", color = Color.White, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // inicio boton: ir a iniciar sesion
                TextButton(onClick = alIniciarSesion) {
                    Text("Iniciar sesión", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                }
                
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

// 🚩 [RF08 - CONTROL DE ERROR EN INICIAR SESIÓN]: Pantalla de Inicio de Sesión con validación de credenciales
@Composable
fun PantallaIniciarSesion(
    alIniciarSesionExitoso: (String) -> Unit, 
    alHacerClicEnRegistrarse: () -> Unit,
    alHacerClicEnRecuperarContrasena: () -> Unit = {}
) {
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var estaCargando by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    val contexto = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoCalle28(size = 100)
            
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { 
                    correo = it 
                    mensajeError = null
                },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !estaCargando,
                isError = mensajeError != null
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { 
                    contrasena = it 
                    mensajeError = null
                },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !estaCargando,
                isError = mensajeError != null
            )

            if (mensajeError != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = mensajeError!!,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (estaCargando) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else {
                // inicio boton: ingresar login (RF08)
                Button(
                    onClick = { 
                        if (correo.isEmpty() || contrasena.isEmpty()) {
                            mensajeError = "Por favor llena todos los campos"
                            Toast.makeText(contexto, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        
                        estaCargando = true
                        mensajeError = null
                        RepositorioUsuarios.iniciarSesion(correo, contrasena) { exito, error ->
                            estaCargando = false
                            if (exito) {
                                val rol = if (correo.contains("admin", ignoreCase = true)) "ADMIN" else "CLIENTE"
                                alIniciarSesionExitoso(rol)
                            } else {
                                mensajeError = error ?: "Correo o contraseña incorrectos"
                                Toast.makeText(contexto, mensajeError, Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("Ingresar", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = alHacerClicEnRecuperarContrasena,
                enabled = !estaCargando
            ) {
                Text("¿Olvidaste tu contraseña?", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = alHacerClicEnRegistrarse, enabled = !estaCargando) {
                Text("¿No tienes una cuenta? Regístrate aquí", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

// 🚩 [PANTALLA RECUPERAR CUENTA]: Pestaña para recuperar cuenta
@Composable
fun PantallaRecuperarCuenta(alVolverAlLogin: () -> Unit) {
    var correoRecuperacion by rememberSaveable { mutableStateOf("") }
    val contexto = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoCalle28(size = 80)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Recuperar Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = correoRecuperacion,
                onValueChange = { correoRecuperacion = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (correoRecuperacion.isEmpty()) {
                        Toast.makeText(contexto, "Por favor ingresa tu correo", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(contexto, "Se te envió un correo de recuperación de cuenta", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Enviar Correo", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = alVolverAlLogin) {
                Text("Volver al Login", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

// 🚩 [RF06 - VERIFICACIÓN DE CORREO DUPLICADO]: Pantalla de Registro con comprobación de correo existente
@Composable
fun PantallaRegistroCuenta(alRegistrarExitoso: (String) -> Unit, alIniciarSesionClick: () -> Unit) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var estaCargando by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    val contexto = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoCalle28(size = 80)
            
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(text = "Crea tu cuenta para empezar a disfrutar", color = MaterialTheme.colorScheme.secondary)

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { 
                    nombre = it
                    mensajeError = null
                },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !estaCargando
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { 
                    correo = it
                    mensajeError = null
                },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !estaCargando,
                isError = mensajeError?.contains("correo", ignoreCase = true) == true ||
                        mensajeError?.contains("registrado", ignoreCase = true) == true ||
                        mensajeError?.contains("uso", ignoreCase = true) == true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = { 
                    if (it.all { char -> char.isDigit() } && it.length <= 9) {
                        telefono = it
                        mensajeError = null
                    }
                },
                label = { Text("Teléfono (9 dígitos)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !estaCargando,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { 
                    contrasena = it
                    mensajeError = null
                },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !estaCargando
            )

            if (mensajeError != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = mensajeError!!,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (estaCargando) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else {
                // inicio boton: registrarse (RF06)
                Button(
                    onClick = {
                        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                            mensajeError = "Por favor llena todos los campos"
                            Toast.makeText(contexto, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        
                        if (!correo.endsWith("@gmail.com")) {
                            mensajeError = "El correo debe ser @gmail.com"
                            Toast.makeText(contexto, "El correo debe ser @gmail.com", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (telefono.length != 9 || !telefono.startsWith("9")) {
                            mensajeError = "El teléfono debe tener 9 dígitos e iniciar con 9"
                            Toast.makeText(contexto, "El teléfono debe tener 9 dígitos e iniciar con 9", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        
                        if (contrasena.length < 8) {
                            mensajeError = "La contraseña debe tener al menos 8 caracteres"
                            Toast.makeText(contexto, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        estaCargando = true
                        mensajeError = null
                        RepositorioUsuarios.registrar(nombre, correo, contrasena, telefono) { exito, error ->
                            estaCargando = false
                            if (exito) {
                                Toast.makeText(contexto, "Sesión iniciada correctamente", Toast.LENGTH_SHORT).show()
                                val rol = if (correo.contains("admin", ignoreCase = true)) "ADMIN" else "CLIENTE"
                                alRegistrarExitoso(rol)
                            } else {
                                mensajeError = error ?: "El correo electrónico ya está en uso"
                                Toast.makeText(contexto, mensajeError, Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("Registrarse", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = alIniciarSesionClick, enabled = !estaCargando) {
                Text("¿Ya tienes una cuenta? Inicia sesión", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
