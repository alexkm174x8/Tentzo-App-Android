package com.example.apptentzo_android.ui.Map

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import androidx.compose.ui.viewinterop.AndroidView
import com.example.apptentzo_android.R

class RouteDisplay : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RouteDisplayContent()
        }
    }
}

@Composable
fun RouteDisplayContent() {
    // Componente de mapa que mostrará la ruta
    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    AndroidView(factory = { mapView }) { androidMapView -> // Renombrado a androidMapView
        coroutineScope.launch {
            val googleMap = androidMapView.awaitMap()  // Usamos androidMapView en lugar de mapView
            fetchRouteFromFirebase { routePoints ->
                drawRouteOnMap(googleMap, routePoints)
            }
        }
    }
}

// Función para recuperar las coordenadas desde Firebase
private fun fetchRouteFromFirebase(onRouteReady: (List<LatLng>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val rutaCollection = db.collection("Coordenadas") // Cambia "tus_colecciones" por el nombre real de tu colección

    // Consulta solo los documentos con id_ruta = 1
    rutaCollection.whereEqualTo("id_ruta", 1).get()
        .addOnSuccessListener { documents ->
            val routePoints = documents.mapNotNull { document ->
                val lat = document.getDouble("latitud") ?: return@mapNotNull null
                val lng = document.getDouble("longitud") ?: return@mapNotNull null
                LatLng(lat, lng)
            }
            onRouteReady(routePoints)
        }
        .addOnFailureListener { exception ->
            Log.e("RouteDisplay", "Error al obtener documentos", exception)
        }
}

// Función para dibujar la ruta en el mapa
private fun drawRouteOnMap(map: GoogleMap, routePoints: List<LatLng>) {
    if (routePoints.isNotEmpty()) {
        // Centrar la cámara en el primer punto
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(routePoints.first(), 15f))

        // Configurar y agregar la línea de la ruta
        val polylineOptions = PolylineOptions()
            .addAll(routePoints)
            .color(android.graphics.Color.BLUE)
            .width(5f)
        map.addPolyline(polylineOptions)
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

@Preview(showBackground = true)
@Composable
fun PreviewRouteDisplay() {
    RouteDisplayContent()
}
