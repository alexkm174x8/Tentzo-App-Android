package com.example.apptentzo_android.ui.Info

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.apptentzo_android.R
import com.example.apptentzo_android.ui.model.Actividad
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class SocialMedia(val name: String, val url: String, val iconRes: Int)

@Composable
fun InfoScreen(navController: NavController) {
    // Estado para almacenar las actividades
    var actividades by remember { mutableStateOf(listOf<Actividad>()) }

    // Obtener instancia de Firestore
    val db = FirebaseFirestore.getInstance()

    // Recuperar actividades desde Firestore
    LaunchedEffect(Unit) {
        try {
            val snapshot = db.collection("Actividad").get().await()
            actividades = snapshot.documents.mapNotNull { doc ->
                val id = doc.id
                val nombre = doc.getString("nombre")
                val tipo = doc.getString("tipo")
                val detalles = doc.getString("detalles")
                val fecha = doc.getString("fecha")
                val costo = doc.getString("costo")
                val imagen = doc.getString("imagen")
                if (nombre != null && imagen != null) {
                    Actividad(
                        id = id,
                        nombre = nombre,
                        tipo = tipo ?: "",
                        detalles = detalles ?: "",
                        fecha = fecha ?: "",
                        costo = costo ?: "",
                        imagen = imagen
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            // Manejar errores aquí
            e.printStackTrace()
        }
    }

    // Lista de redes sociales
    val redesSociales = listOf(
        SocialMedia(
            name = "Instagram",
            url = "https://www.instagram.com/ocoyucanvidayconservacion/",
            iconRes = R.drawable.insta
        ),
        SocialMedia(
            name = "Facebook",
            url = "https://www.facebook.com/ocoyucanvidayconservacion?mibextid=ZbWKwL",
            iconRes = R.drawable.face
        ),
        SocialMedia(
            name = "X",
            url = "https://x.com/OcoyucanVYCAC/status/1849957105748373512",
            iconRes = R.drawable.x
        ),
        SocialMedia(
            name = "Página Oficial",
            url = "https://www.ocoyucan.com/",
            iconRes = R.drawable.oco
        ),
        SocialMedia(
            name = "YouTube",
            url = "https://www.youtube.com/@OcoyucanVidayConservacionA.C.?app=desktop",
            iconRes = R.drawable.youtube
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Sección de Redes Sociales
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Redes Sociales",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 25.sp,
                modifier = Modifier.padding(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Divider(
                modifier = Modifier
                    .height(1.dp)
                    .weight(1f)
                    .padding(end = 10.dp),
                color = Color.Gray,
                thickness = 1.dp
            )
        }

        // Iconos de Redes Sociales en LazyRow
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(redesSociales) { redSocial ->
                SocialIcon(redSocial)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sección de Más Actividades
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Más actividades",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 25.sp,
                modifier = Modifier.padding(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Divider(
                modifier = Modifier
                    .height(1.dp)
                    .weight(1f)
                    .padding(end = 10.dp),
                color = Color.Gray,
                thickness = 1.dp
            )
        }

        // Lista de Actividades
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 20.dp)
        ) {
            items(actividades) { actividad ->
                ActividadItem(actividad) {
                    navController.navigate("InfoDetails/${actividad.id}")
                }
            }
        }
    }
}

@Composable
fun ActividadItem(actividad: Actividad, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 10.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.BottomStart
    ) {
        // Cargar imagen desde URL usando Coil
        AsyncImage(
            model = actividad.imagen,
            contentDescription = "Fondo de Actividad",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
        )
        Text(
            text = actividad.nombre,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(20.dp)
        )
    }
}

@Composable
fun SocialIcon(socialMedia: SocialMedia) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = Color(0xffE9F4CA))
            .clickable {
                // Abrir el enlace correspondiente
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(socialMedia.url))
                context.startActivity(intent)
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = socialMedia.iconRes),
            contentDescription = socialMedia.name,
            modifier = Modifier.size(40.dp)
        )
    }
}
