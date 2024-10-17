package com.example.apptentzo_android.ui.Login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Login(navController: NavController) {
    var email by remember { mutableStateOf("estefania@gmail.com") } // Datos de prueba
    var password by remember { mutableStateOf("estefania123") } // Datos de prueba

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(30.dp))
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Vive el Tentzo",
                color = Color(0xff7fc297),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Text(
                text = "Iniciar Sesión",
                color = Color(0xff000201),
                style = TextStyle(
                    fontSize = 55.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo") },
                modifier = Modifier
                    .requiredWidth(336.dp)
                    .requiredHeight(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(BorderStroke(1.dp, Color(0xff757575)))
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier
                    .requiredWidth(336.dp)
                    .requiredHeight(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(BorderStroke(1.dp, Color(0xff757575)))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .requiredWidth(336.dp)
                    .requiredHeight(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Color(0xff7fc297))
                    .clickable {
                        // Navegar a MenuScreen al hacer clic
                        navController.navigate("menu_screen") {
                            popUpTo("login_screen") { inclusive = true } // Evitar regresar a Login
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Iniciar Sesión",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Texto para recuperación de contraseña
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color(0xff247d3d).copy(alpha = 0.89f),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clickable { /* Manejar clic para recuperar contraseña */ }
            )
        }
    }
}


