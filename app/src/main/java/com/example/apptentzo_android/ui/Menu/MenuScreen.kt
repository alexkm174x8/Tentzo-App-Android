package com.example.apptentzo_android.ui.Menu

import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.apptentzo_android.R
import com.example.apptentzo_android.ui.Info.SocialMedia
import com.example.apptentzo_android.ui.model.Actividad
import com.example.apptentzo_android.ui.model.Insignia
import com.example.apptentzo_android.ui.model.InsigniaRequirement
import com.example.apptentzo_android.ui.model.Parameter
import com.example.apptentzo_android.ui.model.Planta
import com.example.apptentzo_android.ui.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.random.Random

@Composable
fun HomeScreen(navController: NavController? = null, modifier: Modifier = Modifier) {
    // Estados para controlar los diálogos
    var showViewDialog by remember { mutableStateOf(false) }
    var showDialogInsignia by remember { mutableStateOf(false) }
    var showDialogFlor by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedInsignia: Insignia? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    val mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser

    var usuario by remember { mutableStateOf<Usuario?>(null) }
    val userId = currentUser?.uid
    var insigniasList by remember { mutableStateOf<List<Insignia>>(emptyList()) }
    var florDelDia by remember { mutableStateOf<Planta?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Definir la URL de la imagen predeterminada
    val defaultImageUrl = "res/drawable/foto.png" // Reemplaza con tu URL real

    // Launcher para seleccionar imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && userId != null) {
            // Subir imagen a Firebase Storage y actualizar Firestore
            coroutineScope.launch {
                try {
                    val storageRef = FirebaseStorage.getInstance().reference
                    val profileImagesRef = storageRef.child("profileImages/${userId}/${UUID.randomUUID()}")
                    profileImagesRef.putFile(uri).await()
                    val downloadUrl = profileImagesRef.downloadUrl.await().toString()

                    // Actualizar la URL en Firestore
                    val db = FirebaseFirestore.getInstance()
                    db.collection("Usuario").document(userId)
                        .update("foto_perfil", downloadUrl)
                        .await()

                    // Actualizar el estado local del usuario
                    usuario = usuario?.copy(foto_perfil = downloadUrl)

                    snackbarHostState.showSnackbar("Foto de perfil actualizada")

                } catch (e: Exception) {
                    e.printStackTrace()
                    snackbarHostState.showSnackbar("Error al actualizar la foto de perfil")
                }
            }
        }
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
        // Mostrar indicador de carga
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
                    text = "${user.nombre}",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Light
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 190.dp,
                            y = 142.dp
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

                // Títulos adicionales
                Text(
                    text = "Senderista",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 188.dp,
                            y = 110.dp
                        )
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )

                Text(
                    text = "¿Listo para la aventura?",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 17.dp,
                            y = 30.dp
                        )
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )

                // Foto de perfil
                IconButton(
                    onClick = {
                        // Mostrar diálogo para ver la imagen
                        showViewDialog = true
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
                                .clip(RoundedCornerShape(100.dp))
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.foto), // Imagen predeterminada
                            contentDescription = "foto_perfil",
                            modifier = Modifier
                                .requiredSize(134.dp)
                                .clip(RoundedCornerShape(100.dp))
                        )
                    }
                }

                // Botón de editar perfil
                IconButton(
                    onClick = {
                        // Mostrar diálogo de edición
                        showEditDialog = true
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

                Image(
                    painter = painterResource(id = R.drawable.wey),
                    contentDescription = "paisaje",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 240.dp,
                            y = 170.dp
                        )
                        .requiredWidth(width = 216.dp)
                        .requiredHeight(height = 83.dp)
                )

                // Diálogo para ver la foto de perfil
                if (showViewDialog) {
                    Dialog(
                        onDismissRequest = { showViewDialog = false },
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
                                    .requiredWidth(width = 343.dp)
                                    .requiredHeight(height = 351.dp)
                            ) {
                                if (user.foto_perfil != null && user.foto_perfil.isNotEmpty()) {
                                    AsyncImage(
                                        model = user.foto_perfil,
                                        contentDescription = "foto_perfil",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .fillMaxSize()
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.foto), // Imagen predeterminada
                                        contentDescription = "foto_perfil",
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }

                // Diálogo de insignia seleccionada
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
                                        .clip(RoundedCornerShape(60.dp)),
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

                // Diálogo de edición de perfil
                if (showEditDialog) {
                    Dialog(
                        onDismissRequest = { showEditDialog = false },
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
                                    .requiredWidth(width = 343.dp)
                                    .requiredHeight(height = 137.dp)
                            ) {
                                Text(
                                    text = "¿Qué deseas realizar?",
                                    color = Color(0xff000003),
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 25.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier
                                        .align(alignment = Alignment.TopCenter)
                                        .padding(top = 15.dp, start = 22.dp, end = 22.dp)
                                        .requiredHeight(height = 62.dp)
                                        .wrapContentHeight(align = Alignment.CenterVertically)
                                )
                                // Botón "Eliminar"
                                Box(
                                    modifier = Modifier
                                        .align(alignment = Alignment.BottomStart)
                                        .padding(start = 22.dp, bottom = 22.dp)
                                        .width(149.dp)
                                        .height(49.dp)
                                        .clip(shape = RoundedCornerShape(25.dp))
                                        .background(color = Color(0xff7fc297))
                                        .clickable {
                                            // Eliminar la foto de perfil y establecer la imagen predeterminada
                                            coroutineScope.launch {
                                                try {
                                                    val db = FirebaseFirestore.getInstance()
                                                    // Define la URL de la imagen predeterminada
                                                    val defaultImageUrl = "https://yourapp.com/default_foto.png" // Reemplaza con tu URL real

                                                    // Actualizar Firestore solo si userId no es nulo
                                                    userId?.let { nonNullUserId ->
                                                        db.collection("Usuario").document(nonNullUserId)
                                                            .update("foto_perfil", defaultImageUrl)
                                                            .await()

                                                        // Actualizar el estado local del usuario
                                                        usuario = usuario?.copy(foto_perfil = defaultImageUrl)

                                                        showEditDialog = false

                                                        snackbarHostState.showSnackbar("Foto de perfil eliminada")

                                                    } ?: run {
                                                        // Manejar el caso cuando userId es nulo
                                                        Log.e("HomeScreen", "Error: userId es nulo. No se pudo actualizar la foto de perfil.")
                                                        snackbarHostState.showSnackbar("Error al eliminar la foto de perfil: usuario no autenticado.")
                                                    }

                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                    snackbarHostState.showSnackbar("Error al eliminar la foto de perfil")
                                                }
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Eliminar",
                                        color = Color(0xff000003),
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            fontSize = 18.sp
                                        )
                                    )
                                }
                                // Botón "Cambiar"
                                Box(
                                    modifier = Modifier
                                        .align(alignment = Alignment.BottomEnd)
                                        .padding(end = 22.dp, bottom = 22.dp)
                                        .width(142.dp)
                                        .height(46.dp)
                                        .clip(shape = RoundedCornerShape(25.dp))
                                        .background(color = Color(0xffd3fde2))
                                        .clickable {
                                            // Cerrar el diálogo y lanzar el selector de imágenes
                                            showEditDialog = false
                                            imagePickerLauncher.launch("image/*")
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Cambiar",
                                        color = Color(0xff000003),
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            fontSize = 18.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Mostrar mensajes de Snackbar
                SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
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
