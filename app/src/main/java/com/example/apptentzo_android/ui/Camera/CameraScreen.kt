package com.example.apptentzo_android.ui.Camera

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException

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

@Composable
fun CameraScreen() {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var plantInfo by remember { mutableStateOf<PlantInfo?>(null) }
    val context = LocalContext.current

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

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Imagen capturada",
                modifier = Modifier.size(500.dp)
                    .offset(y=80.dp)
            )
        } ?: run {
            // Mensaje si es que no se otorgan permisos o no hay imagen
            Text(text = "Permiso de cámara requerido.", color = Color.Red)
        }


        plantInfo?.let { info ->
            // Popup con el diseño especificado
            Text(
                text = "Especie Encontrada",
                color = Color.Black,
                style = TextStyle(fontSize = 30.sp),
                maxLines = 2,
                modifier = Modifier
                .offset(x = -60.dp, y = 100.dp)
            )
            Box(
                modifier = Modifier
                    .requiredWidth(490.dp)
                    .requiredHeight(150.dp)
                    .offset(x = 55.dp, y = 150.dp)
                    .background(Color.White)
            ) {

                // Fondo verde con esquinas redondeadas
                Box(
                    modifier = Modifier
                        .requiredWidth(380.dp)
                        .requiredHeight(150.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color(0xff7fc297))
                        .offset(x = 40.dp, y = -40.dp)
                ) {
                    // Colocamos una Row para organizar la imagen y los textos
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(y=10.dp)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Imagen de la planta a la izquierda
                        val painter = rememberAsyncImagePainter(info.imageUrl)
                        Image(
                            painter = painter,
                            contentDescription = "Imagen de la planta",
                            modifier = Modifier
                                .size(90.dp)
                                .offset(x = -10.dp)
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
                                style = TextStyle(fontSize = 22.sp),
                                maxLines = 2
                            )

                            // Nombre científico
                            Text(
                                text = info.scientificName,
                                color = Color.White,
                                style = TextStyle(fontSize = 18.sp),
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        } ?: Text(
            text = "No se ha identificado ninguna planta aún.",
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}

fun convertBitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

// Función modificada identifyPlant con callback
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
        put("plant_details", listOf("common_names", "url", "name_authority", "wiki_description", "taxonomy"))
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
