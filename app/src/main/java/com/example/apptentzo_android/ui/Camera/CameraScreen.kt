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

@Composable
fun CameraScreen() {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var plantName by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permiso concedido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
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
            onPlantIdentified = { name ->
                plantName = name
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
            )
        } ?: run {
            // Mensaje si es que no se otorgan permisos o no hay imagen
            Text(text = "Permiso de cámara requerido.", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        plantName?.let { name ->
            // Popup con el diseño especificado
            Box(
                modifier = Modifier
                    .requiredWidth(430.dp)
                    .requiredHeight(207.dp)
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(Color.White)
            ) {
                // Fondo verde con esquinas redondeadas
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color(0xff7fc297))
                )

                // Título
                Text(
                    text = "Especies identificadas:",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 23.dp, y = 21.dp)
                        .requiredWidth(391.dp)
                )

                // Nombre de la planta
                Text(
                    text = name,
                    color = Color.White,
                    style = TextStyle(fontSize = 15.sp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 147.dp, y = 104.dp)
                )
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
    onPlantIdentified: (String) -> Unit
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
            // Actualizar plantName en caso de fallo
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                onPlantIdentified("Error en la identificación")
            }
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!it.isSuccessful) {
                    Log.e("CameraScreen", "Request failed: ${it.message}")
                    val mainHandler = Handler(Looper.getMainLooper())
                    mainHandler.post {
                        onPlantIdentified("Error en la identificación")
                    }
                    return
                }

                // Analizar y manejar la respuesta
                val responseBody = it.body?.string()
                if (responseBody != null) {
                    val jsonResponse = JSONObject(responseBody)
                    val suggestions = jsonResponse.optJSONArray("suggestions")

                    // Extraer el campo "plant_name" de la primera sugerencia
                    if (suggestions != null && suggestions.length() > 0) {
                        val firstSuggestion = suggestions.getJSONObject(0)
                        val plantName = firstSuggestion.optString("plant_name", "Planta desconocida")
                        Log.d("CameraScreen", "Plant Name: $plantName")

                        // Actualizar plantName en el hilo principal
                        val mainHandler = Handler(Looper.getMainLooper())
                        mainHandler.post {
                            onPlantIdentified(plantName)
                        }
                    } else {
                        Log.d("CameraScreen", "No se encontraron sugerencias de plantas.")
                        val mainHandler = Handler(Looper.getMainLooper())
                        mainHandler.post {
                            onPlantIdentified("No se encontró ninguna planta.")
                        }
                    }
                } else {
                    val mainHandler = Handler(Looper.getMainLooper())
                    mainHandler.post {
                        onPlantIdentified("Error en la identificación")
                    }
                }
            }
        }
    })
}