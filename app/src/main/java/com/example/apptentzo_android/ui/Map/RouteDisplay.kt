// RouteDisplayContent.kt

package com.example.apptentzo_android.ui.Map

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import androidx.compose.ui.viewinterop.AndroidView
import com.example.apptentzo_android.ui.model.Ruta
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun RouteDisplayContent(rutaId: String?) {
    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var isRouteLoading by remember { mutableStateOf(true) }

    // Agregar un log para verificar el rutaId recibido
    Log.d("RouteDisplay", "Ruta ID recibido: $rutaId")

    AndroidView(factory = { mapView }) { androidMapView ->
        coroutineScope.launch {
            val googleMap = androidMapView.awaitMap()
            rutaId?.let { rutaId ->
                Log.d("RouteDisplay", "Fetching route for id_ruta = $rutaId")
                fetchRouteFromFirebase(rutaId) { routePoints ->
                    if (routePoints.isNotEmpty()) {
                        Log.d("RouteDisplay", "Route Points: $routePoints")
                        drawRouteOnMap(googleMap, routePoints)
                        isRouteLoading = false
                    } else {
                        Log.e("RouteDisplay", "No se encontraron puntos de ruta con id_ruta = $rutaId")
                        isRouteLoading = false
                    }
                }
            } ?: run {
                Log.e("RouteDisplay", "rutaId es nulo")
                isRouteLoading = false
            }
        }
    }

    // Mostrar indicador de carga mientras se dibuja la ruta
    if (isRouteLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}

// Función para recuperar las coordenadas desde Firestore e imprimirlas en Logcat
private fun fetchRouteFromFirebase(rutaId: String, onRouteReady: (List<LatLng>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val rutaCollection = db.collection("Coordenada")

    // Aplicar el filtro id_ruta = rutaId dinámicamente y ordenar por id ascendente
    rutaCollection.whereEqualTo("id_ruta", rutaId)
        .orderBy("id")
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                Log.e("RouteDisplay", "No se obtuvieron puntos de ruta para id_ruta = $rutaId")
            } else {
                val routePoints = documents.mapNotNull { document ->
                    val lat = document.getDouble("latitud") ?: return@mapNotNull null
                    val lng = document.getDouble("longitud") ?: return@mapNotNull null
                    LatLng(lat, lng)
                }
                Log.d("RouteDisplay", "Route Points: $routePoints")
                onRouteReady(routePoints)
            }
        }
        .addOnFailureListener { exception ->
            Log.e("RouteDisplay", "Error al obtener documentos", exception)
        }
}

// Función para dibujar la ruta en el mapa
private fun drawRouteOnMap(map: GoogleMap, routePoints: List<LatLng>) {
    if (routePoints.isNotEmpty()) {
        val polylineOptions = PolylineOptions()
            .addAll(routePoints)
            .color(android.graphics.Color.BLUE)
            .width(5f)
        map.addPolyline(polylineOptions)

        // Configuración de límites para centrar la ruta en la cámara
        val boundsBuilder = LatLngBounds.Builder()
        routePoints.forEach { boundsBuilder.include(it) }
        val bounds = boundsBuilder.build()

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        Log.d("RouteDisplay", "Cámara centrada en la ruta.")
    }
}

// Función para manejar el ciclo de vida de MapView en Compose
@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context).apply { onCreate(Bundle()) } }

    DisposableEffect(mapView) {
        mapView.onResume()
        onDispose { mapView.onPause() }
    }
    return mapView
}
