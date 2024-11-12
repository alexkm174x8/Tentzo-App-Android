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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
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
            RouteDisplayContent(idRuta = 1)
        }
    }
}

@Composable
fun RouteDisplayContent(idRuta: Int) {
    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    AndroidView(factory = { mapView }) { androidMapView ->
        coroutineScope.launch {
            val googleMap = androidMapView.awaitMap()
            fetchRouteFromFirebase(idRuta) { routePoints ->
                if (routePoints.isNotEmpty()) {
                    drawRouteOnMap(googleMap, routePoints)
                } else {
                    Log.e("RouteDisplay", "No se encontraron puntos de ruta con id_ruta = $idRuta")
                }
            }
        }
    }
}

// Función para recuperar las coordenadas desde Firebase e imprimirlas en Logcat
private fun fetchRouteFromFirebase(idRuta: Int, onRouteReady: (List<LatLng>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val rutaCollection = db.collection("Coordenada")

    // Aplicar el filtro id_ruta = idRuta dinámicamente y ordenar por id ascendente
    rutaCollection.whereEqualTo("id_ruta", idRuta)
        .orderBy("id")
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                Log.e("RouteDisplay", "No se obtuvieron puntos de ruta para id_ruta = $idRuta")
            } else {
                for (document in documents) {
                    Log.d("RouteDisplay", "Documento encontrado: ${document.data}")
                }

                val routePoints = documents.mapNotNull { document ->
                    val lat = document.getDouble("latitud") ?: return@mapNotNull null
                    val lng = document.getDouble("longitud") ?: return@mapNotNull null
                    LatLng(lat, lng)
                }
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

@Preview(showBackground = true)
@Composable
fun PreviewRouteDisplay() {
    RouteDisplayContent(idRuta = 1)
}
