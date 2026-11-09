package com.example.myapplication.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPromociones(
    alVolverAPerfil: () -> Unit = {},
    alVolverAlMenu: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Promociones Especiales", fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¡2x1 en Ceviches los Martes!", fontSize = 18.sp, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(24.dp))

            // inicio boton: volver al perfil
            Button(onClick = alVolverAPerfil) {
                Text("Volver al Perfil")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // inicio boton: ir a la carta directamente
            OutlinedButton(onClick = alVolverAlMenu) {
                Text("Ir a la Carta")
            }
        }
    }
}
