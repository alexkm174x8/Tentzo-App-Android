package com.example.apptentzo_android.ui.Menu

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.apptentzo_android.R
import com.example.apptentzo_android.ui.model.Insignia
import com.example.apptentzo_android.ui.model.InsigniaRequirement
import com.example.apptentzo_android.ui.model.Planta
import com.example.apptentzo_android.ui.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

@Composable
fun HomeScreen(navController: NavController? = null, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    var showDialogInsignia by remember { mutableStateOf(false) }
    var showDialogFlor by remember { mutableStateOf(false) }
    var selectedInsignia: Insignia? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    val mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser
    val userId = currentUser?.uid

    var usuario by remember { mutableStateOf<Usuario?>(null) }
    var insigniasList by remember { mutableStateOf<List<Insignia>>(emptyList()) }
    var florDelDia by remember { mutableStateOf<Planta?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Variables para el clima aleatorio
    var randomTemperature by remember { mutableStateOf(0) }
    var randomWeatherDescription by remember { mutableStateOf("") }
    var isWeatherLoading by remember { mutableStateOf(true) }

    // Variable para la imagen seleccionada
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Remember the launcher for picking images from the gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Manejar la URI de la imagen seleccionada
            selectedImageUri = it
        }
    }

    // LaunchedEffect para subir la imagen cuando cambia selectedImageUri
    LaunchedEffect(selectedImageUri) {
        selectedImageUri?.let { uri ->
            val storageRef = FirebaseStorage.getInstance().reference
            val profileImagesRef = storageRef.child("profile_images/${userId}.jpg")

            try {
                // Mostrar indicador de carga
                isLoading = true

                // Subir la imagen a Firebase Storage
                profileImagesRef.putFile(uri).await()

                // Obtener la URL de descarga
                val downloadUrl = profileImagesRef.downloadUrl.await().toString()

                // Actualizar el campo 'foto_perfil' en Firestore
                val db = FirebaseFirestore.getInstance()
                db.collection("Usuario").document(userId!!).update("foto_perfil", downloadUrl).await()

                // Actualizar el estado 'usuario'
                usuario = usuario?.copy(foto_perfil = downloadUrl)

                Toast.makeText(context, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                e.printStackTrace()
                // Manejar el error
                Toast.makeText(context, "Error al actualizar la foto de perfil", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }

    // Generar temperatura y estado del clima aleatorios
    LaunchedEffect(Unit) {
        isWeatherLoading = true
        // Generar temperatura aleatoria entre 15°C y 35°C
        randomTemperature = Random.nextInt(15, 36)

        // Lista de posibles estados del clima
        val weatherDescriptions = listOf(
            "Cielo Despejado",
            "Nubes Parciales",
            "Neblina",
            "Llovizna",
            "Lluvia",
            "Tormenta",
            "Nieve"
        )
        // Seleccionar una descripción aleatoria
        randomWeatherDescription = weatherDescriptions.random()
        isWeatherLoading = false
    }

    // Obtener datos del usuario, insignias y planta aleatoria desde Firestore
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
                    val nombre = doc.getString("nombre") ?: ""
                    val descripcion = doc.getString("descripcion") ?: ""
                    val imagen_b = doc.getString("imagen_b")
                    val imagen_d = doc.getString("imagen_d")
                    if (imagen_b != null && imagen_d != null) {
                        Insignia(
                            id = id,
                            nombre = nombre,
                            descripcion = descripcion,
                            imagen_b = imagen_b,
                            imagen_d = imagen_d
                        )
                    } else {
                        null
                    }
                }

                // Obtener plantas y seleccionar una aleatoria para la "Flor del Día"
                val plantaSnapshots = db.collection("Planta").get().await()
                val plantasList = plantaSnapshots.documents.mapNotNull { doc ->
                    val id = doc.id
                    val nomComun = doc.getString("nomComun") ?: ""
                    val nomCientifico = doc.getString("nomCientifico") ?: ""
                    val descripcion = doc.getString("descripcion") ?: ""
                    val imagen = doc.getString("imagen") ?: ""
                    if (imagen.isNotEmpty()) {
                        Planta(
                            id = id,
                            nomComun = nomComun,
                            nomCientifico = nomCientifico,
                            descripcion = descripcion,
                            imagen = imagen
                        )
                    } else {
                        null
                    }
                }
                if (plantasList.isNotEmpty()) {
                    florDelDia = plantasList.random()
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

                // Sección "Especie del Día"
                Text(
                    text = "¡ Conóceme !",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 130.dp,
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

                // Temperatura y Estado del Clima
                if (isWeatherLoading) {
                    // Mostrar indicador de carga para el clima
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(x = 180.dp, y = 170.dp)
                            .size(50.dp),
                        contentAlignment = Alignment.Center
                    ) {

                    }
                } else {
                    // Mostrar la información del clima aleatorio
                    Row(
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(x = 170.dp, y = 100.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(8.dp)
                    ) {
                        // Icono del clima (puedes usar un ícono genérico o basado en la descripción)
                        val iconResource = when (randomWeatherDescription) {
                            "Cielo Despejado" -> R.drawable.ic_clear_sky
                            "Nubes Parciales" -> R.drawable.ic_partly_cloudy
                            "Neblina" -> R.drawable.ic_fog
                            "Llovizna" -> R.drawable.ic_drizzle
                            "Lluvia" -> R.drawable.ic_rain
                            "Tormenta" -> R.drawable.ic_thunderstorm
                            "Nieve" -> R.drawable.ic_snow
                            else -> R.drawable.ic_unknow
                        }
                        Image(
                            painter = painterResource(id = iconResource),
                            contentDescription = "Icono del Clima",
                            modifier = Modifier
                                .size(100.dp)
                                .offset(x= 0.dp, y = -10.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Información del clima
                        Column {
                            Text(
                                text = "$randomTemperature °C",
                                color = Color(0xff2e354d),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = randomWeatherDescription,
                                color = Color(0xff2e354d),
                                fontSize = 16.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }

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
                florDelDia?.let { flor ->
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(x = 60.dp, y = 500.dp)
                            .requiredWidth(300.dp)
                            .requiredHeight(200.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .clickable {
                                // Mostrar diálogo con los detalles de la flor del día
                                showDialogFlor = true
                            }
                    ) {
                        AsyncImage(
                            model = flor.imagen,
                            contentDescription = "Conóceme",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Botón de Emergencia
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 35.dp, y = 730.dp)
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
                        // Lanzar el selector de imágenes
                        imagePickerLauncher.launch("image/*")
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
                    val unlocked = isInsigniaUnlocked(selectedInsignia!!.id, user, insigniaRequirements)
                    Dialog(
                        onDismissRequest = { showDialogInsignia = false },
                        properties = DialogProperties(dismissOnBackPress = true)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(30.dp),
                            border = BorderStroke(3.dp, Color.White),
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(30.dp))
                                .requiredWidth(width = 390.dp)
                                .requiredHeight(height = 390.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .requiredWidth(width = 384.dp)
                                    .requiredHeight(height = 389.dp)
                            ) {
                                // Imagen de la insignia
                                Box(
                                    modifier = Modifier
                                        .align(alignment = Alignment.TopStart)
                                        .offset(x = 77.dp, y = 10.dp)
                                        .requiredSize(size = 230.dp)
                                ) {
                                    AsyncImage(
                                        model = if (unlocked) {
                                            selectedInsignia!!.imagen_d
                                        } else {
                                            selectedInsignia!!.imagen_b
                                        },
                                        contentDescription = "insignia image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .align(alignment = Alignment.TopStart)
                                            .offset(x = 17.dp, y = 40.dp)
                                            .requiredWidth(width = 200.dp)
                                            .requiredHeight(height = 200.dp)
                                    )
                                }
                                // Nombre de la insignia o mensaje de espera
                                Text(
                                    text = if (unlocked) selectedInsignia!!.nombre else "Insignia en Espera",
                                    color = Color(0xff000003),
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier
                                        .align(alignment = Alignment.TopStart)
                                        .offset(x = 25.dp, y = 267.dp)
                                        .requiredWidth(width = 334.dp)
                                        .wrapContentHeight(align = Alignment.CenterVertically)
                                )
                                // Descripción de la insignia o mensaje de espera
                                Text(
                                    text = if (unlocked) selectedInsignia!!.descripcion else "¡Aún no has alcanzado el objetivo! Completa los requisitos para desbloquear esta insignia.",
                                    color = Color(0xff000003),
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Light
                                    ),
                                    modifier = Modifier
                                        .align(alignment = Alignment.TopStart)
                                        .offset(x = 27.dp, y = 316.dp)
                                        .requiredWidth(width = 331.dp)
                                        .wrapContentHeight(align = Alignment.CenterVertically)
                                )
                            }
                        }
                    }
                }

                // Diálogo de la Flor del Día
                if (showDialogFlor && florDelDia != null) {
                    Dialog(
                        onDismissRequest = { showDialogFlor = false },
                        properties = DialogProperties(dismissOnBackPress = true)
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
                                    .requiredWidth(width = 384.dp)
                                    .requiredHeight(height = 494.dp)
                            ) {
                                // Imagen de la planta
                                AsyncImage(
                                    model = florDelDia!!.imagen,
                                    contentDescription = "Imagen de la Flor del Día",
                                    modifier = Modifier
                                        .align(alignment = Alignment.TopStart)
                                        .offset(x = 70.dp, y = 28.dp)
                                        .requiredWidth(width = 250.dp)
                                        .requiredHeight(height = 190.dp)
                                        .clip(shape = RoundedCornerShape(60.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                // Título: "Especie del Día"
                                Text(
                                    text = "¡ Conóceme !",
                                    color = Color(0xff000003),
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier
                                        .align(alignment = Alignment.TopStart)
                                        .offset(x = 49.dp, y = 247.dp)
                                        .requiredWidth(width = 285.dp)
                                        .wrapContentHeight(align = Alignment.CenterVertically)
                                )
                                // Nombre común de la planta
                                Text(
                                    text = florDelDia!!.nomComun,
                                    color = Color(0xff000003),
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    modifier = Modifier
                                        .align(alignment = Alignment.TopStart)
                                        .offset(x = 50.dp, y = 287.dp)
                                        .requiredWidth(width = 285.dp)
                                        .wrapContentHeight(align = Alignment.CenterVertically)
                                )
                                // Nombre científico
                                Text(
                                    text = florDelDia!!.nomCientifico,
                                    color = Color(0xff000003),
                                    fontStyle = FontStyle.Italic,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 13.sp
                                    ),
                                    modifier = Modifier
                                        .align(alignment = Alignment.TopStart)
                                        .offset(x = 50.dp, y = 319.dp)
                                        .requiredWidth(width = 285.dp)
                                        .wrapContentHeight(align = Alignment.CenterVertically)
                                )
                                // Descripción
                                Text(
                                    text = florDelDia!!.descripcion,
                                    color = Color(0xff000003),
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Light
                                    ),
                                    modifier = Modifier
                                        .align(alignment = Alignment.TopStart)
                                        .offset(x = 50.dp, y = 349.dp)
                                        .requiredWidth(width = 285.dp)
                                        .wrapContentHeight(align = Alignment.CenterVertically)
                                )
                            }
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
