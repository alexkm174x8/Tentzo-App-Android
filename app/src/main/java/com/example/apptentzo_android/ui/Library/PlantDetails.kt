package com.example.apptentzo_android

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
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
                val fuente = doc.getString("fuente") ?: ""
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
        // Mostrar la información de la planta
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            // Imagen de la planta
            AsyncImage(
                model = planta!!.imagen,
                contentDescription = planta!!.nomComun,
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(437.dp),
                contentScale = ContentScale.Crop
            )
            // Botón de retroceso
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .requiredWidth(60.dp)
                    .requiredHeight(60.dp)
                    .offset(x = 17.dp, y = 33.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = "icon",
                    modifier = Modifier.fillMaxSize()
                )
            }
            // Información de la planta
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                modifier = Modifier
                    .offset(x = 30.dp, y = 470.dp)
            ) {
                Text(
                    text = planta!!.nomCientifico,
                    color = Color(0xff7fc297),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                Text(
                    text = planta!!.nomComun,
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            }
            Text(
                text = "Datos curiosos",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .offset(x = 30.dp, y = 535.dp)
                    .requiredWidth(308.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                modifier = Modifier
                    .requiredWidth(367.dp)
                    .offset(x = 30.dp, y = 570.dp)
            ) {
                Text(
                    text = planta!!.descripcion,
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            }
            Text(
                text = "Fuente consultada",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .offset(x = 30.dp, y = 739.dp)
                    .requiredWidth(308.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
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
