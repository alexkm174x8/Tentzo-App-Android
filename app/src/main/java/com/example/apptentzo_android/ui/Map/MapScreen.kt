package com.example.apptentzo_android.ui.Map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.apptentzo_android.ui.model.Ruta
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.tasks.await

@Composable
fun MapScreen(navController: NavHostController) {
    var boxHeight by remember { mutableStateOf(200.dp) }
    val minHeight = 200.dp
    val maxHeight = 700.dp
    val density = LocalDensity.current.density // Obtener la densidad

    // Manejo de permisos
    val context = LocalContext.current
    var isLocationPermissionGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        isLocationPermissionGranted = permissions.values.all { it }
    }

    LaunchedEffect(Unit) {
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        isLocationPermissionGranted = hasPermissions(context, locationPermissions)

        if (!isLocationPermissionGranted) {
            launcher.launch(locationPermissions)
        }
    }

    // Variables de estado para las rutas
    var rutasList by remember { mutableStateOf<List<Ruta>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Obtener rutas desde Firestore
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val db = FirebaseFirestore.getInstance()
            val rutasSnapshot = db.collection("Ruta").get().await()
            val rutas = rutasSnapshot.documents.mapNotNull { document ->
                val ruta = document.toObject(Ruta::class.java)
                ruta?.copy(id_ruta = document.id) // Asegurar que el ID se asigna correctamente
            }
            rutasList = rutas
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isLoading = false
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // GoogleMap en el fondo
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = MapProperties(isMyLocationEnabled = isLocationPermissionGranted),
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        )

        // Caja superpuesta en la parte inferior del mapa
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .fillMaxWidth()
                .height(boxHeight)
                .background(Color.White)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val dragInDp = (dragAmount.y / density).dp
                        boxHeight = (boxHeight - dragInDp).coerceIn(minHeight, maxHeight)
                    }
                }
        ) {
            // Contenido de la caja
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rutas",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (isLoading) {
                    // Mostrar indicador de carga
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    // Mostrar lista de rutas
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(rutasList) { ruta ->
                            RutaItem(ruta = ruta, navController = navController)
                        }
                    }
                }
            }
        }
    }
}

// Composable para cada Ruta
@Composable
fun RutaItem(ruta: Ruta, navController: NavHostController) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .background(Color(0xff7FC297))
            .height(115.dp)
            .fillMaxWidth()
            .clickable {
                // Navegar a RouteDetails pasando el ID de la ruta
                navController.navigate("RouteDetails/${ruta.id_ruta}")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen de la ruta ajustada al tamaño del cuadro sin deformarse
            if (ruta.imagen.isNotEmpty()) {
                AsyncImage(
                    model = ruta.imagen,
                    contentDescription = "Imagen de la Ruta",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Nombre de la ruta
            Text(
                text = ruta.nombre,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// Función para verificar permisos
private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}
