// InfoScreen.kt
package com.example.apptentzo_android.ui.Info

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.apptentzo_android.R
import com.example.apptentzo_android.ui.model.Actividad
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class SocialMedia(val name: String, val url: String, val iconRes: Int)

@Composable
fun InfoScreen(navController: NavController) {
    var actividades by remember { mutableStateOf(listOf<Actividad>()) }
    val db = FirebaseFirestore.getInstance()

    // Estado para controlar la visualización del pop-up
    var showLogoutDialog by remember { mutableStateOf(false) }

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
                } else null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val redesSociales = listOf(
        SocialMedia("Instagram", "https://www.instagram.com/ocoyucanvidayconservacion/", R.drawable.insta),
        SocialMedia("Facebook", "https://www.facebook.com/ocoyucanvidayconservacion?mibextid=ZbWKwL", R.drawable.face),
        SocialMedia("X", "https://x.com/OcoyucanVYCAC/status/1849957105748373512", R.drawable.x),
        SocialMedia("Página Oficial", "https://www.ocoyucan.com/", R.drawable.oco),
        SocialMedia("YouTube", "https://www.youtube.com/@OcoyucanVidayConservacionA.C.?app=desktop", R.drawable.youtube)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween // Para organizar contenido y botón "Cerrar Sesión"
    ) {
        Column(
            modifier = Modifier.weight(1f) // Permite que el contenido ocupe espacio y empuja el botón hacia abajo
        ) {
            // ... (Contenido existente de la pantalla)
            // Aquí va el código de las redes sociales y actividades
            // ...

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

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .offset(y = -5.dp)
            ) {
                items(actividades) { actividad ->
                    ActividadItem(actividad) {
                        navController.navigate("InfoDetails/${actividad.id}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de Cerrar Sesión
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 35.dp)
                .offset(y = -10.dp)
                .requiredHeight(61.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .border(BorderStroke(1.dp, Color(0xffb6b6b6)), RoundedCornerShape(30.dp))
                .background(color = Color.White)
                .clickable {
                    // Mostrar el diálogo de confirmación
                    showLogoutDialog = true
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.salir),
                    contentDescription = "Log out",
                    modifier = Modifier
                        .requiredSize(30.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = "CERRAR SESIÓN Y SALIR",
                    color = Color(0xffd13e3e),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .requiredWidth(235.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            }
        }
    }

    // Diálogo de confirmación para cerrar sesión
    if (showLogoutDialog) {
        // Usamos Dialog para mostrar el pop-up
        Dialog(
            onDismissRequest = { showLogoutDialog = false }
        ) {
            Surface(
                shape = RoundedCornerShape(30.dp),
                color = Color.White,
                border = BorderStroke(3.dp, Color.White),
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(30.dp))
            ) {
                Box(
                    modifier = Modifier
                        .width(343.dp)
                        .height(179.dp)
                ) {
                    Text(
                        text = "¿Estás seguro de cerrar sesión?",
                        color = Color(0xff000003),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.TopCenter)
                            .padding(top = 22.dp, start = 22.dp, end = 22.dp)
                            .requiredHeight(height = 62.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                    // Botón "Cerrar Sesión"
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.BottomStart)
                            .padding(start = 22.dp, bottom = 22.dp)
                            .width(130.dp)
                            .height(49.dp)
                            .clip(shape = RoundedCornerShape(25.dp))
                            .background(color = Color(0xff7fc297))
                            .clickable {
                                // Cerrar sesión y navegar al login
                                FirebaseAuth.getInstance().signOut()
                                navController.navigate("login_screen") {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cerrar Sesión",
                            color = Color(0xff000003),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 14.sp
                            )
                        )
                    }
                    // Botón "Cancelar"
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.BottomEnd)
                            .padding(end = 22.dp, bottom = 22.dp)
                            .width(130.dp)
                            .height(49.dp)
                            .clip(shape = RoundedCornerShape(25.dp))
                            .background(color = Color(0xffd3fde2))
                            .clickable {
                                // Cerrar el diálogo sin hacer nada
                                showLogoutDialog = false
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancelar",
                            color = Color(0xff000003),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 14.sp
                            )
                        )
                    }
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
