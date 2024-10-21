package com.example.apptentzo_android.ui.Inicio

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.example.apptentzo_android.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SignIn(modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Inicializa FirebaseAuth y Firestore
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(30.dp))
            .background(color = Color.White)
    ) {
        // Título de la aplicación
        Text(
            text = "Vive el Tentzo",
            color = Color(0xff7fc297),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 118.dp, y = 87.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
        )

        // Texto de registro
        Text(
            text = "Registrarse",
            color = Color(0xff000201),
            style = TextStyle(
                fontSize = 55.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 64.dp, y = 116.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
        )

        // Nombre completo
        Text(
            text = "Nombre Completo",
            color = Color.Black.copy(alpha = 0.89f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 111.dp, y = 236.dp)
        )
        TextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text(text = "Nombre", color = Color.Gray) },
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(y = 30.dp)
                .requiredWidth(width = 336.dp)
                .requiredHeight(height = 56.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color.White)
                .border(BorderStroke(1.dp, Color(0xff757575)))
                .padding(horizontal = 16.dp)
        )

        // Email
        Text(
            text = "Email",
            color = Color.Black.copy(alpha = 0.89f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 185.dp, y = 341.dp)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text(text = "Correo", color = Color.Gray) },
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(y = 30.dp)
                .requiredWidth(width = 336.dp)
                .requiredHeight(height = 56.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color.White)
                .border(BorderStroke(1.dp, Color(0xff757575)))
                .padding(horizontal = 16.dp)
        )

        // Contraseña
        Text(
            text = "Contraseña",
            color = Color.Black.copy(alpha = 0.89f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 145.dp, y = 442.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(text = "Contraseña", color = Color.Gray) },
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(y = 30.dp)
                .requiredWidth(width = 336.dp)
                .requiredHeight(height = 56.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color.White)
                .border(BorderStroke(1.dp, Color(0xff757575)))
                .padding(horizontal = 16.dp)
            // Asegúrate de usar el tipo de texto adecuado para la contraseña
        )

        // Repetir Contraseña
        Text(
            text = "Repite tu Contraseña",
            color = Color.Black.copy(alpha = 0.89f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 91.dp, y = 540.dp)
        )
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text(text = "Repite la contraseña", color = Color.Gray) },
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(y = 30.dp)
                .requiredWidth(width = 336.dp)
                .requiredHeight(height = 56.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color.White)
                .border(BorderStroke(1.dp, Color(0xff757575)))
                .padding(horizontal = 16.dp)
            // Asegúrate de usar el tipo de texto adecuado para la contraseña
        )

        // Botón de registro
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .offset(y = 220.dp)
                .requiredWidth(width = 289.dp)
                .requiredHeight(height = 48.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                    // Validar las contraseñas y registrar al usuario
                    if (password == confirmPassword) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = hashMapOf(
                                        "user_id" to user_id,
                                        "name" to name,
                                        "email" to email,
                                        "password" to password, // Consider hashing the password before storing it
                                        "points" to 0,
                                        "profile_picture" to ""
                                    )
                                    val user_id = auth.currentUser?.uid
                                    if (user_id != null) {
                                        db.collection("usuario").document(user_id)
                                            .set(user)
                                            .addOnSuccessListener {
                                                Log.d("Firebase", "Usuario registrado correctamente")
                                                // Aquí puedes redirigir al usuario a otra pantalla
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w("Firebase", "Error al agregar usuario", e)
                                            }
                                    }
                                } else {
                                    Log.w("Firebase", "Error en registro", task.exception)
                                }
                            }
                    } else {
                        Log.w("Firebase", "Las contraseñas no coinciden")
                    }
                }
                .padding(all = 12.dp)
        ) {
            Text(
                text = "Registrarse",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .requiredWidth(width = 267.dp)
                    .requiredHeight(height = 35.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
        // Resto del código para Google Sign-In
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 23.dp, y = 212.dp)
                .requiredWidth(width = 384.dp)
                .requiredHeight(height = 528.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .border(border = BorderStroke(1.dp, Color(0xff2b3707)),
                    shape = RoundedCornerShape(30.dp)))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 161.dp, y = 808.dp)
                .requiredWidth(width = 88.dp)
                .requiredHeight(height = 89.dp)
                .clickable {
                    // Manejo para iniciar sesión con Google
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "google",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun SignInPreview() {
    SignIn(Modifier)
}
