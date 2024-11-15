// CameraScreen.kt

package com.example.apptentzo_android.ui.Camera

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.apptentzo_android.R
import com.example.apptentzo_android.ui.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CameraScreen()
        }
    }
}

// Definimos una data class para almacenar la información de la planta
data class PlantInfo(
    val name: String,
    val scientificName: String,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen() {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var plantInfo by remember { mutableStateOf<PlantInfo?>(null) }
    val context = LocalContext.current

    // Obtener el UID del usuario actual
    val mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser
    val userId = currentUser?.uid

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permiso concedido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Intente otra vez", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        imageBitmap = bitmap
        val plantBase64 = bitmap?.let { convertBitmapToBase64(it) }

        // Llamar a la función identifyPlant con el callback
        identifyPlant(
            imageBase64 = plantBase64 ?: "",
            latitude = null,
            longitude = null,
            apiKey = "0B3G3gYo0gziMjliprpRFc5XVB2EbG9swngse8W4ZbnKOdNUOu", // Reemplaza con tu clave de API
            onPlantIdentified = { info ->
                plantInfo = info

                // Incrementar el campo 'plantas' en Firebase si se identificó una planta
                if (info.name != "Error en la identificación" && userId != null) {
                    incrementPlantCount(userId)
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(imageBitmap) {
        if (imageBitmap == null) {
            cameraLauncher.launch(null)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Puedes poner un título si lo deseas */ },
                actions = {
                    // Si hay una imagen, mostrar el ícono de descarga
                    if (imageBitmap != null) {
                        IconButton(onClick = {
                            imageBitmap?.let { bitmap ->
                                saveImageToGallery(context, bitmap)
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.descargar),
                                contentDescription = "Descargar imagen",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xff7fc297)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            imageBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Imagen capturada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f) // Ajusta la relación de aspecto según sea necesario
                )
            } ?: run {
                // Mensaje si no se otorgan permisos o no hay imagen
                Text(
                    text = "Permiso de cámara requerido.",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            plantInfo?.let { info ->
                Spacer(modifier = Modifier.height(16.dp))

                // Título
                Text(
                    text = "Especie Encontrada",
                    color = Color.Black,
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Caja para la información de la planta
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xff7fc297))
                        .padding(16.dp)
                ) {
                    // Colocamos una Row para organizar la imagen y los textos
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Imagen de la planta a la izquierda
                        val painter = rememberAsyncImagePainter(info.imageUrl)
                        Image(
                            painter = painter,
                            contentDescription = "Imagen de la planta",
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Columna para los nombres a la derecha
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            // Nombre común
                            Text(
                                text = info.name,
                                color = Color.White,
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium),
                                maxLines = 2
                            )

                            // Nombre científico
                            Text(
                                text = info.scientificName,
                                color = Color.White,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontStyle = FontStyle.Italic
                                ),
                                maxLines = 2
                            )
                        }
                    }
                }
            } ?: Text(
                text = "No se ha identificado ninguna planta aún.",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

fun convertBitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

// Función para identificar la planta
fun identifyPlant(
    imageBase64: String,
    latitude: Double?,
    longitude: Double?,
    apiKey: String,
    onPlantIdentified: (PlantInfo) -> Unit
) {
    val client = OkHttpClient()
    val url = "https://api.plant.id/v2/identify"

    // Construir el cuerpo JSON
    val jsonBody = JSONObject().apply {
        put("images", listOf("data:image/jpeg;base64,$imageBase64"))
        put("modifiers", listOf("similar_images"))
        put("plant_language", "es")
        put(
            "plant_details",
            listOf("common_names", "url", "name_authority", "wiki_description", "taxonomy")
        )
        if (latitude != null && longitude != null) {
            put("latitude", latitude)
            put("longitude", longitude)
        }
    }

    val mediaType = "application/json".toMediaType()
    val requestBody = jsonBody.toString().toRequestBody(mediaType)

    // Crear la solicitud
    val request = Request.Builder()
        .url(url)
        .addHeader("Api-Key", apiKey)
        .addHeader("Content-Type", "application/json")
        .post(requestBody)
        .build()

    // Ejecutar la solicitud
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("CameraScreen", "Request failed: ${e.message}")
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                onPlantIdentified(
                    PlantInfo(
                        name = "Error en la identificación",
                        scientificName = "",
                        imageUrl = ""
                    )
                )
            }
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!it.isSuccessful) {
                    Log.e("CameraScreen", "Request failed: ${it.message}")
                    val mainHandler = Handler(Looper.getMainLooper())
                    mainHandler.post {
                        onPlantIdentified(
                            PlantInfo(
                                name = "Error en la identificación",
                                scientificName = "",
                                imageUrl = ""
                            )
                        )
                    }
                    return
                }

                // Analizar y manejar la respuesta
                val responseBody = it.body?.string()
                if (responseBody != null) {
                    val jsonResponse = JSONObject(responseBody)
                    val suggestions = jsonResponse.optJSONArray("suggestions")

                    // Extraer el campo "plant_name", "common_names" e "image_url" de la primera sugerencia
                    if (suggestions != null && suggestions.length() > 0) {
                        val firstSuggestion = suggestions.getJSONObject(0)
                        val plantName = firstSuggestion.optString("plant_name", "Planta desconocida")
                        val plantDetails = firstSuggestion.optJSONObject("plant_details")
                        val commonNamesArray = plantDetails?.optJSONArray("common_names")
                        val commonName = commonNamesArray?.optString(0) ?: "Nombre común desconocido"

                        val similarImages = firstSuggestion.optJSONArray("similar_images")
                        val imageUrl = if (similarImages != null && similarImages.length() > 0) {
                            val firstImage = similarImages.getJSONObject(0)
                            firstImage.optString("url", "")
                        } else {
                            ""
                        }

                        Log.d("CameraScreen", "Plant Name: $plantName")
                        Log.d("CameraScreen", "Common Name: $commonName")
                        Log.d("CameraScreen", "Image URL: $imageUrl")

                        // Actualizar plantInfo en el hilo principal
                        val mainHandler = Handler(Looper.getMainLooper())
                        mainHandler.post {
                            onPlantIdentified(
                                PlantInfo(
                                    name = commonName,
                                    scientificName = plantName,
                                    imageUrl = imageUrl
                                )
                            )
                        }
                    } else {
                        Log.d("CameraScreen", "No se encontraron sugerencias de plantas.")
                        val mainHandler = Handler(Looper.getMainLooper())
                        mainHandler.post {
                            onPlantIdentified(
                                PlantInfo(
                                    name = "No se encontró ninguna planta.",
                                    scientificName = "",
                                    imageUrl = ""
                                )
                            )
                        }
                    }
                } else {
                    val mainHandler = Handler(Looper.getMainLooper())
                    mainHandler.post {
                        onPlantIdentified(
                            PlantInfo(
                                name = "Error en la identificación",
                                scientificName = "",
                                imageUrl = ""
                            )
                        )
                    }
                }
            }
        }
    })
}

// Función para guardar la imagen en la galería
fun saveImageToGallery(context: Context, bitmap: Bitmap) {
    val filename = "Imagen_${System.currentTimeMillis()}.jpg"
    val outputStream: OutputStream?
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.WIDTH, bitmap.width)
        put(MediaStore.Images.Media.HEIGHT, bitmap.height)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val uri = context.contentResolver.insert(contentUri, contentValues)

    if (uri != null) {
        outputStream = context.contentResolver.openOutputStream(uri)
        outputStream?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(uri, contentValues, null, null)
        }
        Toast.makeText(context, "Imagen guardada en la galería", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
    }
}

// Función para incrementar el campo 'plantas' en Firebase
fun incrementPlantCount(userId: String) {
    val db = FirebaseFirestore.getInstance()
    val userRef = db.collection("Usuario").document(userId)

    // Usamos FieldValue.increment para incrementar atómicamente el valor
    userRef.update("plantas", com.google.firebase.firestore.FieldValue.increment(1))
        .addOnSuccessListener {
            Log.d("CameraScreen", "Campo 'plantas' incrementado exitosamente.")
        }
        .addOnFailureListener { e ->
            Log.e("CameraScreen", "Error al incrementar el campo 'plantas': ${e.message}")
        }
}
