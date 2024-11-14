package com.example.apptentzo_android.ui.Camera

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import android.util.Base64
import androidx.compose.material3.Text
import java.io.ByteArrayOutputStream
import android.util.Log
import org.json.JSONObject
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
    var plantBase64 by remember { mutableStateOf<String?>(null) }
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
        plantBase64 = bitmap?.let { convertBitmapToBase64(it) }

        // Log the Base64 string
        Log.d("CameraScreen", "Image Base64: ${plantBase64?.take(100)}...")  // Limit length for readability
        identifyPlant(imageBase64 = plantBase64 ?: "", latitude = null, longitude = null, apiKey = "0B3G3gYo0gziMjliprpRFc5XVB2EbG9swngse8W4ZbnKOdNUOu")
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
                modifier = Modifier.size(200.dp)
            )
        } ?: run {
            //mensaje si es que no se otorgan permisos
        }
    }
}

fun convertBitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

/////////////////API
fun identifyPlant(imageBase64: String, latitude: Double?, longitude: Double?, apiKey: String) {
    val client = OkHttpClient()
    val url = "https://plant.id/api/v3/identification"

    // Build JSON body
    val jsonBody = JSONObject().apply {
        put("images", listOf("data:image/jpg;base64,$imageBase64"))
        if (latitude != null && longitude != null) {
            put("latitude", latitude)
            put("longitude", longitude)
        }
        put("similar_images", true)
    }

    val mediaType = "application/json".toMediaType()
    val requestBody = jsonBody.toString().toRequestBody(mediaType)

    // Create the request
    val request = Request.Builder()
        .url(url)
        .addHeader("Api-Key", apiKey)
        .addHeader("Content-Type", "application/json")
        .post(requestBody)
        .build()

    // Execute the request
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println("Request failed: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!it.isSuccessful) {
                    println("Request failed: ${it.message}")
                    return
                }

                // Parse and handle the response
                val responseBody = it.body?.string()
                if (responseBody != null) {
                    val jsonResponse = JSONObject(responseBody)
                    val result = jsonResponse.optJSONObject("result")
                    val classification = result?.optJSONObject("classification")
                    val suggestions = classification?.optJSONArray("suggestions")

                    // Extract the "name" field from the first suggestion
                    if (suggestions != null && suggestions.length() > 0) {
                        val firstSuggestion = suggestions.getJSONObject(0)
                        val plantName = firstSuggestion.optString("name", "Unknown Plant")
                        Log.d("CameraScreen","Plant Name: $plantName")
                        // You can now use the plantName variable for further processing
                    } else {
                        Log.d("CameraScreen","No plant suggestions found.")
                    }
                }
            }
        }
    })
}


@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    CameraScreen()
}
