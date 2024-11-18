package com.example.apptentzo_android.ui.Login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

@Composable
fun Login(navController: NavController, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var auth = FirebaseAuth.getInstance()

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

            Text(
                text = "¿No tienes cuenta? Regístrate",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color(0xff247d3d),
                ),
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .clickable {
                        navController.navigate("signin_screen")
                    }
                    .padding(top = 20.dp)
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
                        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            showErrorDialog = true
                            Log.w("Firebase", "Correo electrónico inválido")
                        } else {
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        navController.navigate("menu_screen") {
                                            popUpTo("login_screen") { inclusive = true }
                                        }
                                    } else {
                                        showErrorDialog = true // Mostrar el diálogo de error
                                        Log.w("Firebase", "Error en inicio de sesión", task.exception)
                                    }
                                }
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

            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color(0xff247d3d).copy(alpha = 0.89f),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clickable {
                        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            showErrorDialog = true
                            Log.w("Firebase", "Correo electrónico inválido para recuperación")
                        } else {
                            showResetDialog = true
                        }
                    }
            )
        }
    }

    // Diálogo de error
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "Error") },
            text = { Text(text = "Correo o contraseña incorrecta. Intenta de nuevo.") },
            confirmButton = {
                TextButton(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }

    // Diálogo de confirmación para enviar el correo de recuperación
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(text = "Recuperar Contraseña") },
            text = { Text(text = "¿Deseas enviar un correo para restablecer tu contraseña a $email?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        sendPasswordResetEmail(email) {
                            showResetDialog = false
                            showSuccessDialog = true
                        }
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showResetDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }

    // Diálogo de éxito al enviar el correo de recuperación
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text(text = "Correo Enviado") },
            text = { Text(text = "Se ha enviado un correo a $email para restablecer tu contraseña.") },
            confirmButton = {
                TextButton(
                    onClick = { showSuccessDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

/**
 * Función para enviar el correo de restablecimiento de contraseña.
 *
 * @param email Correo electrónico del usuario.
 * @param onSuccess Callback a ejecutar cuando el correo se envíe exitosamente.
 */
private fun sendPasswordResetEmail(email: String, onSuccess: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
                Log.d("Firebase", "Correo de restablecimiento de contraseña enviado a $email")
            } else {
                Log.e("Firebase", "Error al enviar correo de restablecimiento", task.exception)
                // Aquí puedes manejar el error mostrando otro diálogo o mensaje
            }
        }
}
