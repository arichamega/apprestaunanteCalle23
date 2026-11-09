package com.example.myapplication.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

data class CuentaUsuario(
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val telefono: String = "",
    val rol: String = "CLIENTE"
)

object RepositorioUsuarios {
    private val auth: FirebaseAuth? by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (_: Exception) {
            null
        }
    }

    private var prefs: SharedPreferences? = null

    private val _nombreUsuarioActual = MutableStateFlow("")
    val nombreUsuarioActual: StateFlow<String> = _nombreUsuarioActual.asStateFlow()

    private val _correoUsuarioActual = MutableStateFlow("")
    val flujoCorreoUsuarioActual: StateFlow<String> = _correoUsuarioActual.asStateFlow()

    val correoUsuarioActual: String?
        get() = _correoUsuarioActual.value.ifEmpty { auth?.currentUser?.email }

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences("user_session_prefs", Context.MODE_PRIVATE)
            val savedEmail = prefs?.getString("logged_in_email", "") ?: ""
            val savedName = prefs?.getString("logged_in_name", "") ?: ""
            if (savedEmail.isNotEmpty() && savedName.isNotEmpty()) {
                _correoUsuarioActual.value = savedEmail
                _nombreUsuarioActual.value = savedName
            } else {
                val firebaseUser = auth?.currentUser
                if (firebaseUser != null) {
                    val name = firebaseUser.displayName.takeIf { !it.isNullOrEmpty() }
                        ?: firebaseUser.email?.substringBefore("@")
                        ?: "Usuario"
                    _correoUsuarioActual.value = firebaseUser.email ?: ""
                    _nombreUsuarioActual.value = name
                }
            }
        }
    }

    private fun obtenerUsuariosRegistrados(): List<CuentaUsuario> {
        val lista = mutableListOf<CuentaUsuario>()
        lista.add(CuentaUsuario("Administrador", "admin@gmail.com", "12345678", "999888777", "ADMIN"))
        lista.add(CuentaUsuario("Cliente Demo", "cliente@gmail.com", "12345678", "999111222", "CLIENTE"))

        val jsonStr = prefs?.getString("registered_users_json", "") ?: ""
        if (jsonStr.isNotEmpty()) {
            try {
                val jsonArray = JSONArray(jsonStr)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    lista.add(
                        CuentaUsuario(
                            nombre = obj.optString("name"),
                            correo = obj.optString("email"),
                            contrasena = obj.optString("password"),
                            telefono = obj.optString("phone"),
                            rol = obj.optString("role", "CLIENTE")
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return lista
    }

    private fun guardarUsuarioRegistrado(cuenta: CuentaUsuario) {
        val jsonStr = prefs?.getString("registered_users_json", "") ?: ""
        val jsonArray = if (jsonStr.isNotEmpty()) {
            try { JSONArray(jsonStr) } catch (e: Exception) { JSONArray() }
        } else {
            JSONArray()
        }

        val obj = JSONObject().apply {
            put("name", cuenta.nombre)
            put("email", cuenta.correo)
            put("password", cuenta.contrasena)
            put("phone", cuenta.telefono)
            put("role", cuenta.rol)
        }
        jsonArray.put(obj)
        prefs?.edit()?.putString("registered_users_json", jsonArray.toString())?.apply()
    }

    fun registrar(nombre: String, correo: String, contrasena: String, telefono: String = "", alFinalizar: (Boolean, String?) -> Unit) {
        val usuarios = obtenerUsuariosRegistrados()
        if (usuarios.any { it.correo.equals(correo, ignoreCase = true) }) {
            alFinalizar(false, "El correo electrónico ya está registrado")
            return
        }

        val nuevaCuenta = CuentaUsuario(
            nombre = nombre,
            correo = correo,
            contrasena = contrasena,
            telefono = telefono,
            rol = if (correo.contains("admin", ignoreCase = true)) "ADMIN" else "CLIENTE"
        )
        guardarUsuarioRegistrado(nuevaCuenta)

        _nombreUsuarioActual.value = nombre
        _correoUsuarioActual.value = correo
        prefs?.edit()
            ?.putString("logged_in_email", correo)
            ?.putString("logged_in_name", nombre)
            ?.apply()

        try {
            auth?.createUserWithEmailAndPassword(correo, contrasena)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val profileUpdates = userProfileChangeRequest { displayName = nombre }
                        auth?.currentUser?.updateProfile(profileUpdates)
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        alFinalizar(true, null)
    }

    fun iniciarSesion(correo: String, contrasena: String, alFinalizar: (Boolean, String?) -> Unit) {
        val usuarios = obtenerUsuariosRegistrados()
        val usuarioEncontrado = usuarios.find { it.correo.equals(correo, ignoreCase = true) }

        if (usuarioEncontrado != null) {
            if (usuarioEncontrado.contrasena == contrasena) {
                _nombreUsuarioActual.value = usuarioEncontrado.nombre
                _correoUsuarioActual.value = usuarioEncontrado.correo
                prefs?.edit()
                    ?.putString("logged_in_email", usuarioEncontrado.correo)
                    ?.putString("logged_in_name", usuarioEncontrado.nombre)
                    ?.apply()

                try {
                    auth?.signInWithEmailAndPassword(correo, contrasena)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                alFinalizar(true, null)
            } else {
                alFinalizar(false, "Correo o contraseña incorrectos")
            }
        } else {
            try {
                auth?.signInWithEmailAndPassword(correo, contrasena)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val name = auth?.currentUser?.displayName.takeIf { !it.isNullOrEmpty() }
                                ?: correo.substringBefore("@")
                            _nombreUsuarioActual.value = name
                            _correoUsuarioActual.value = correo
                            prefs?.edit()
                                ?.putString("logged_in_email", correo)
                                ?.putString("logged_in_name", name)
                                ?.apply()
                            alFinalizar(true, null)
                        } else {
                            alFinalizar(false, "Correo o contraseña incorrectos")
                        }
                    } ?: alFinalizar(false, "Correo o contraseña incorrectos")
            } catch (e: Exception) {
                alFinalizar(false, "Correo o contraseña incorrectos")
            }
        }
    }

    fun establecerNombreUsuario(nombre: String) {
        _nombreUsuarioActual.value = nombre
    }

    fun recuperarContrasena(correo: String, alFinalizar: (Boolean, String) -> Unit) {
        try {
            auth?.sendPasswordResetEmail(correo)
                ?.addOnCompleteListener { _ ->
                    alFinalizar(true, "Se envió la contraseña al correo")
                } ?: alFinalizar(true, "Se envió la contraseña al correo")
        } catch (_: Exception) {
            alFinalizar(true, "Se envió la contraseña al correo")
        }
    }

    fun cerrarSesion() {
        try {
            auth?.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _nombreUsuarioActual.value = ""
        _correoUsuarioActual.value = ""
        prefs?.edit()
            ?.remove("logged_in_email")
            ?.remove("logged_in_name")
            ?.apply()
    }
}
