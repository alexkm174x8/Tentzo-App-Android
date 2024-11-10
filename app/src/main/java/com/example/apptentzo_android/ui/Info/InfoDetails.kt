package com.example.apptentzo_android.ui.Info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.apptentzo_android.R
import com.example.apptentzo_android.ui.model.Actividad
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun InfoDetails(actividadId: String, navController: NavController) {
    var actividad by remember { mutableStateOf<Actividad?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Obtener instancia de Firestore
    val db = FirebaseFirestore.getInstance()

    // Consultar Firestore para obtener los datos de la actividad
    LaunchedEffect(actividadId) {
        isLoading = true
        try {
            val snapshot = db.collection("Actividad").document(actividadId).get().await()
            actividad = snapshot.toObject(Actividad::class.java)?.copy(id = actividadId)
        } catch (e: Exception) {
            e.printStackTrace() // Manejar el error en caso de fallo
        }
        isLoading = false
    }

    // Mostrar un indicador de carga mientras se obtienen los datos
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        actividad?.let { actividadData ->
            // Mostrar los datos de la actividad una vez cargados
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {
                // Imagen de fondo de la actividad
                AsyncImage(
                    model = actividadData.imagen,
                    contentDescription = "Imagen de ${actividadData.nombre}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(437.dp)
                )

                // Botón de retroceso
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(60.dp)
                        .offset(x = 17.dp, y = 33.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "icon",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Contenido de la actividad
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .offset(x = 30.dp, y = 470.dp)
                ) {
                    Text(
                        text = actividadData.tipo,
                        color = Color(0xff7fc297),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = actividadData.nombre,
                        color = Color.Black,
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // Título "Detalles"
                Text(
                    text = "Detalles",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .offset(x = 30.dp, y = 535.dp)
                        .requiredWidth(308.dp)
                )

                // Descripción de la actividad
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .width(367.dp)
                        .offset(x = 30.dp, y = 570.dp)
                ) {
                    Text(
                        text = actividadData.detalles,
                        color = Color.Black,
                        style = TextStyle(
                            fontSize = 15.sp,
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Información de fecha y costo
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 739.dp)
                        .padding(horizontal = 30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Próxima Fecha:",
                            color = Color.Black,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = actividadData.fecha,
                            color = Color.Black,
                            style = TextStyle(fontSize = 15.sp)
                        )
                    }
                    Column {
                        Text(
                            text = "Costo:",
                            color = Color.Black,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = actividadData.costo,
                            color = Color.Black,
                            style = TextStyle(fontSize = 15.sp)
                        )
                    }
                }
            }
        } ?: run {
            // Si no se pudo cargar la actividad, muestra un mensaje de error
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No se pudo cargar la actividad.", color = Color.Red)
            }
        }
    }
}
