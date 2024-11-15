package com.example.apptentzo_android

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.apptentzo_android.R
import com.example.apptentzo_android.ui.model.Planta
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun PlantInfo(
    navController: NavController,
    plantaId: String
) {
    var planta by remember { mutableStateOf<Planta?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Obtener la planta desde Firebase
    LaunchedEffect(plantaId) {
        isLoading = true
        try {
            val db = FirebaseFirestore.getInstance()
            val doc = db.collection("Planta").document(plantaId).get().await()
            if (doc.exists()) {
                val id = doc.id
                val nomComun = doc.getString("nomComun") ?: ""
                val nomCientifico = doc.getString("nomCientifico") ?: ""
                val descripcion = doc.getString("descripcion") ?: ""
                val imagen = doc.getString("imagen") ?: ""
                planta = Planta(
                    id = id,
                    nomComun = nomComun,
                    nomCientifico = nomCientifico,
                    descripcion = descripcion,
                    imagen = imagen
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (planta != null) {
        val scrollState = rememberScrollState()
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp

        // Mostrar la información de la planta
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
        ) {
            // Imagen de la planta y botón de retroceso
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.3f) // Ajustamos la altura en función del tamaño de la pantalla
            ) {
                // Imagen de la planta
                AsyncImage(
                    model = planta!!.imagen,
                    contentDescription = planta!!.nomComun,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Botón de retroceso
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.7f), shape = CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "Atrás",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información de la planta
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Nombre científico
                Text(
                    text = planta!!.nomCientifico,
                    color = Color(0xff7fc297),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                // Nombre común
                Text(
                    text = planta!!.nomComun,
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Título "Datos curiosos"
                Text(
                    text = "Datos curiosos",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Descripción
                Text(
                    text = planta!!.descripcion,
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 15.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    } else {
        // Mostrar mensaje de error si la planta no existe
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No se pudo cargar la información de la planta.", color = Color.Red)
        }
    }
}
