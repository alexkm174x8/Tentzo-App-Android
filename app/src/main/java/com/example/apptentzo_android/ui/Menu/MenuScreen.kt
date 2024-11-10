package com.example.apptentzo_android.ui.Menu

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.apptentzo_android.R
import com.example.apptentzo_android.ui.model.Insignia
import com.example.apptentzo_android.ui.model.InsigniaRequirement
import com.example.apptentzo_android.ui.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun HomeScreen(navController: NavController? = null, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    var showDialogInsignia by remember { mutableStateOf(false) }
    var selectedInsignia: Insignia? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    val mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser
    val userId = currentUser?.uid

    var usuario by remember { mutableStateOf<Usuario?>(null) }
    var insigniasList by remember { mutableStateOf<List<Insignia>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Obtener datos del usuario e insignias desde Firestore
    LaunchedEffect(userId) {
        isLoading = true
        if (userId != null) {
            try {
                val db = FirebaseFirestore.getInstance()
                // Obtener datos del usuario
                val userSnapshot = db.collection("Usuario").document(userId).get().await()
                usuario = userSnapshot.toObject(Usuario::class.java)?.copy(id = userId)

                // Obtener insignias
                val insigniaSnapshots = db.collection("Insignia").get().await()
                insigniasList = insigniaSnapshots.documents.mapNotNull { doc ->
                    val id = doc.id
                    val imagen_b = doc.getString("imagen_b")
                    val imagen_d = doc.getString("imagen_d")
                    if (imagen_b != null && imagen_d != null) {
                        Insignia(id = id, imagen_b = imagen_b, imagen_d = imagen_d)
                    } else {
                        null
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        usuario?.let { user ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {
                // Saludo al usuario
                Text(
                    text = "¡Hola, ${user.nombre}!",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 19.dp,
                            y = 47.dp
                        )
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )

                // Sección "Mis insignias"
                Text(
                    text = "Mis insignias",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 29.dp,
                            y = 274.dp
                        )
                        .requiredWidth(width = 228.dp)
                        .requiredHeight(height = 28.dp)
                )

                // Sección "Flor del Día"
                Text(
                    text = "Flor del Día",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 150.dp,
                            y = 461.dp
                        )
                        .requiredWidth(width = 228.dp)
                        .requiredHeight(height = 28.dp)
                )

                // Definir requisitos de insignias
                val insigniaRequirements = listOf(
                    InsigniaRequirement("1", "rutas", 1),
                    InsigniaRequirement("2", "rutas", 3),
                    InsigniaRequirement("3", "rutas", 4),
                    InsigniaRequirement("4", "rutas", 9),
                    InsigniaRequirement("5", "plantas", 1),
                    InsigniaRequirement("6", "plantas", 5),
                    InsigniaRequirement("7", "plantas", 10),
                    InsigniaRequirement("8", "plantas", 20)
                )

                // Mostrar insignias
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 330.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(insigniasList.size) { index ->
                        val insignia = insigniasList[index]
                        val unlocked = isInsigniaUnlocked(insignia.id, user, insigniaRequirements)
                        val imageUrl = if (unlocked) {
                            insignia.imagen_d
                        } else {
                            insignia.imagen_b
                        }
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "insignia",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(87.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    // Mostrar diálogo con la insignia al hacer clic
                                    selectedInsignia = insignia
                                    showDialogInsignia = true
                                }
                        )
                    }
                }

                // Imagen del clima
                Image(
                    painter = painterResource(id = R.drawable.sol),
                    contentDescription = "sol",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 184.dp,
                            y = 103.dp
                        )
                        .requiredWidth(width = 87.dp)
                        .requiredHeight(height = 85.dp)
                )

                // Temperatura
                Text(
                    text = "25 °C",
                    color = Color(0xff2e354d),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .requiredWidth(width = 131.dp)
                        .requiredHeight(height = 48.dp)
                        .offset(
                            x = 270.dp,
                            y = 120.dp
                        )
                )

                // Divisores
                Divider(
                    color = Color(0xffd1d1d1),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 28.dp,
                            y = 438.dp
                        )
                        .requiredWidth(width = 380.dp)
                )
                Divider(
                    color = Color(0xffd1d1d1),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 201.dp,
                            y = 288.dp
                        )
                        .requiredWidth(width = 204.dp)
                )

                // Imagen de la flor del día
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 60.dp, y = 500.dp)
                        .requiredWidth(300.dp)
                        .requiredHeight(250.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .clickable {
                            showDialog = true
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.flordia),
                        contentDescription = "flordia",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Botón de Emergencia
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 52.dp, y = 760.dp)
                        .requiredWidth(340.dp)
                        .requiredHeight(56.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(color = Color(0xff7FC297))
                        .clickable {
                            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:911"))
                            context.startActivity(dialIntent)
                        }
                ) {
                    Text(
                        text = "Emergencia",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .fillMaxSize()
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                }

                // Botón de Cerrar Sesión
                Box(
                    modifier = Modifier
                        .offset(x = 349.dp, y = 36.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color(0xffb6b6b6)),
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(20.dp))
                            .requiredWidth(width = 56.dp)
                            .requiredHeight(height = 60.dp)
                    ) {
                        IconButton(
                            onClick = {
                                // Acción de cerrar sesión
                                FirebaseAuth.getInstance().signOut()
                                navController?.navigate("login_screen") {
                                    popUpTo("menu_screen") { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logout),
                                contentDescription = "Log out",
                                modifier = Modifier
                                    .align(alignment = Alignment.Center)
                                    .requiredSize(size = 37.dp)
                            )
                        }
                    }
                }

                // Foto de perfil
                IconButton(
                    onClick = {
                        // Mostrar diálogo con la foto de perfil
                        showDialog = true
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 19.dp, y = 103.dp)
                        .requiredSize(134.dp)
                ) {
                    val imageUrl = user.foto_perfil
                    if (imageUrl != null && imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "foto_perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .requiredSize(134.dp)
                                .clip(shape = RoundedCornerShape(100.dp))
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.foto), // Imagen predeterminada
                            contentDescription = "foto_perfil",
                            modifier = Modifier
                                .requiredSize(134.dp)
                                .clip(shape = RoundedCornerShape(100.dp))
                        )
                    }
                }

                // Botón de editar perfil
                IconButton(
                    onClick = {
                        // Acción para editar perfil
                    },
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 122.dp, y = 195.dp)
                        .requiredWidth(42.dp)
                        .requiredHeight(46.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "edit",
                        modifier = Modifier
                            .requiredWidth(35.dp)
                            .requiredHeight(40.dp)
                    )
                }

                // Imagen de paisaje
                Image(
                    painter = painterResource(id = R.drawable.paisaje),
                    contentDescription = "paisaje",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 192.dp,
                            y = 175.dp
                        )
                        .requiredWidth(width = 216.dp)
                        .requiredHeight(height = 83.dp)
                )

                // Diálogo de la foto de perfil
                if (showDialog) {
                    Dialog(
                        onDismissRequest = { showDialog = false },
                        properties = DialogProperties(dismissOnBackPress = true)
                    ) {
                        val imageUrl = user.foto_perfil
                        Box(
                            modifier = Modifier
                                .size(width = 368.dp, height = 351.dp)
                        ) {
                            if (imageUrl != null && imageUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = "foto_perfil",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .offset(x = (-4).dp, y = (-13).dp)
                                        .requiredWidth(width = 377.dp)
                                        .requiredHeight(height = 378.dp)
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.foto), // Imagen predeterminada
                                    contentDescription = "foto_perfil",
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .offset(x = (-4).dp, y = (-13).dp)
                                        .requiredWidth(width = 377.dp)
                                        .requiredHeight(height = 378.dp)
                                )
                            }
                        }
                    }
                }

                // Diálogo de la insignia seleccionada
                if (showDialogInsignia && selectedInsignia != null) {
                    Dialog(
                        onDismissRequest = { showDialogInsignia = false },
                        properties = DialogProperties(dismissOnBackPress = true)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 300.dp, height = 300.dp)
                                .background(Color.White, shape = RoundedCornerShape(20.dp))
                        ) {
                            val unlocked = isInsigniaUnlocked(selectedInsignia!!.id, user, insigniaRequirements)
                            val imageUrl = if (unlocked) {
                                selectedInsignia!!.imagen_d
                            } else {
                                selectedInsignia!!.imagen_b
                            }
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "insignia",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(20.dp))
                            )
                        }
                    }
                }
            }
        } ?: run {
            // Si no se pudo cargar la información del usuario
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No se pudo cargar la información del usuario.", color = Color.Red)
            }
        }
    }
}

// Función auxiliar para verificar si la insignia está desbloqueada
fun isInsigniaUnlocked(insigniaId: String, usuario: Usuario, requirements: List<InsigniaRequirement>): Boolean {
    val requirement = requirements.find { it.insigniaId == insigniaId }
    return if (requirement != null) {
        val userCount = if (requirement.type == "rutas") {
            usuario.rutas
        } else {
            usuario.plantas
        }
        userCount >= requirement.requiredCount
    } else {
        false
    }
}


