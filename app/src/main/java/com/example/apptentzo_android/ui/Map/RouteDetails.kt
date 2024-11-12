package com.example.apptentzo_android.ui.Map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.apptentzo_android.R
import com.example.apptentzo_android.ui.model.Ruta
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun RouteDetails(navController: NavHostController, rutaId: String?, modifier: Modifier = Modifier) {
    var ruta by remember { mutableStateOf<Ruta?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Obtener detalles de la ruta desde Firestore
    LaunchedEffect(rutaId) {
        if (rutaId != null) {
            isLoading = true
            try {
                val db = FirebaseFirestore.getInstance()
                val rutaSnapshot = db.collection("Ruta").document(rutaId).get().await()
                ruta = rutaSnapshot.toObject(Ruta::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            androidx.compose.material3.CircularProgressIndicator()
        }
    } else {
        ruta?.let { rutaData ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // Imagen de fondo de la ruta
                AsyncImage(
                    model = rutaData.imagen,
                    contentDescription = "Imagen de la Ruta",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentScale = ContentScale.Crop
                )

                // Botón de retroceso
                Image(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = "arrow_back",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .padding(16.dp)
                        .size(60.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )

                // Contenido superpuesto
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 350.dp)
                        .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    // Nombre de la ruta
                    Text(
                        text = rutaData.nombre,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Distancia y Tiempo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Distancia:",
                                color = Color.Black,
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text(
                                text = rutaData.distancia,
                                color = Color.Black,
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Light
                                )
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Tiempo:",
                                color = Color.Black,
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text(
                                text = rutaData.tiempo,
                                color = Color.Black,
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Light
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    // Detalles
                    Text(
                        text = "Detalles:",
                        color = Color.Black,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = rutaData.detalles,
                        color = Color.DarkGray,
                        style = TextStyle(
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    // Botón para iniciar ruta
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(color = Color(0xff7fc297))
                            .clickable {
                                navController.navigate("RouteDisplay/${rutaId}")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Iniciar ruta",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        } ?: run {
            // Si no se pudo cargar la información de la ruta
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No se pudo cargar la información de la ruta.", color = Color.Red)
            }
        }
    }
}
