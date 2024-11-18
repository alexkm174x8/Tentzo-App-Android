package com.example.apptentzo_android.ui.SignIn

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptentzo_android.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.navigation.NavController

@Composable
fun SignIn(navController: NavController, modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Inicializa FirebaseAuth y Firestore
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // Obtenemos la configuración de la pantalla para manejar tamaños relativos
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Título de la aplicación
            Text(
                text = "Vive el Tentzo",
                color = Color(0xff7fc297),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Texto de registro
            Text(
                text = "Registrarse",
                color = Color(0xff000201),
                style = TextStyle(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Formulario de registro
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(30.dp))
                    .border(
                        border = BorderStroke(1.dp, Color(0xff2b3707)),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(16.dp)
            ) {
                // Nombre completo
                Text(
                    text = "Nombre Completo",
                    color = Color.Black.copy(alpha = 0.89f),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(text = "Nombre", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email
                Text(
                    text = "Email",
                    color = Color.Black.copy(alpha = 0.89f),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text(text = "Correo", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Contraseña
                Text(
                    text = "Contraseña",
                    color = Color.Black.copy(alpha = 0.89f),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text(text = "Contraseña", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Repetir Contraseña
                Text(
                    text = "Repite tu Contraseña",
                    color = Color.Black.copy(alpha = 0.89f),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text(text = "Repite la contraseña", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de registro
                Button(
                    onClick = {
                        // Validar campos
                        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                            errorMessage = "Por favor llena todos los campos"
                            showErrorDialog = true
                            return@Button
                        }

                        if (password.length < 8) {
                            errorMessage = "La contraseña debe tener al menos 8 caracteres"
                            showErrorDialog = true
                            return@Button
                        }

                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            errorMessage = "Por favor ingresa un correo electrónico válido"
                            showErrorDialog = true
                            return@Button
                        }

                        if (password != confirmPassword) {
                            errorMessage = "Las contraseñas no coinciden"
                            showErrorDialog = true
                            return@Button
                        }

                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user_id = auth.currentUser?.uid
                                    val user = hashMapOf(
                                        "user_id" to user_id,
                                        "nombre" to name,
                                        "email" to email,
                                        "rutas" to 0,
                                        "plantas" to 0,
                                        "foto_perfil" to ""
                                    )
                                    if (user_id != null) {
                                        db.collection("Usuario").document(user_id)
                                            .set(user)
                                            .addOnSuccessListener {
                                                Log.d("Firebase", "Usuario registrado correctamente")
                                                // Navegar a otra pantalla o mostrar un mensaje
                                                navController.navigate("menu_screen") {
                                                    popUpTo("login_screen") { inclusive = true } // Evitar regresar a Login
                                                }
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w("Firebase", "Error al agregar usuario", e)
                                                errorMessage = "Error al registrar usuario"
                                                showErrorDialog = true
                                            }
                                    }
                                } else {
                                    Log.w("Firebase", "Error en registro", task.exception)
                                    errorMessage = "Error al registrar usuario"
                                    showErrorDialog = true
                                }
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff7fc297)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Registrarse",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

        }

        // Diálogo de error
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text(text = "Error") },
                text = { Text(text = errorMessage) },
                confirmButton = {
                    TextButton(
                        onClick = { showErrorDialog = false }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
