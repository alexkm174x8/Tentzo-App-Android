package com.example.apptentzo_android.ui.Map

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.apptentzo_android.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import com.google.maps.android.ktx.awaitMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@SuppressLint("MissingPermission")
@Composable
fun RouteDisplayContent(rutaId: String?, navController: NavController) {
    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var isRouteLoading by remember { mutableStateOf(true) }
    var isFollowingRoute by remember { mutableStateOf(false) }
    var nextPointBearing by remember { mutableStateOf(0f) }
    var totalRouteDistance by remember { mutableStateOf(0.0) }
    var walkedDistance by remember { mutableStateOf(0.0) }
    var hasIncremented by remember { mutableStateOf(false) } // Bandera para evitar múltiples incrementos

    Box(modifier = Modifier.fillMaxSize()) {
        // Botón de retroceso
        Image(
            painter = painterResource(id = R.drawable.arrow_back),
            contentDescription = "Atrás",
            colorFilter = ColorFilter.tint(Color(0xff7fc297)),
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .align(Alignment.TopStart)
                .clickable {
                    navController.popBackStack()
                }
                .zIndex(1f)
        )

        // Vista del mapa
        AndroidView(factory = { mapView }) { androidMapView ->
            coroutineScope.launch {
                val googleMap = androidMapView.awaitMap()

                // Habilitar seguimiento de ubicación en el mapa
                googleMap.isMyLocationEnabled = true

                rutaId?.let {
                    fetchRouteFromFirebase(it) { routePoints ->
                        if (routePoints.isNotEmpty()) {
                            drawRouteOnMap(googleMap, routePoints)
                            totalRouteDistance = calculateTotalDistance(routePoints)
                            isRouteLoading = false

                            if (isFollowingRoute) {
                                startUserLocationTracking(
                                    googleMap = googleMap,
                                    fusedLocationClient = fusedLocationClient,
                                    routePoints = routePoints,
                                    onBearingCalculated = { bearing ->
                                        nextPointBearing = bearing
                                    },
                                    onDistanceUpdated = { distance ->
                                        walkedDistance = distance
                                        checkAndIncrementRoute(walkedDistance, totalRouteDistance, hasIncremented)
                                    }
                                )
                            }
                        } else {
                            Log.e("RouteDisplay", "No route points found for id_ruta = $it")
                            isRouteLoading = false
                        }
                    }
                } ?: run {
                    Log.e("RouteDisplay", "rutaId is null")
                    isRouteLoading = false
                }
            }
        }

        // Flecha rotativa sobre la ubicación del usuario
        if (isFollowingRoute) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_icon),
                    contentDescription = "Direction Arrow",
                    modifier = Modifier
                        .size(64.dp)
                        .graphicsLayer(rotationZ = nextPointBearing) // Rota la flecha
                )
            }
        }

        // Indicador de carga
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

        Button(
            onClick = {
                isFollowingRoute = !isFollowingRoute
                if (isFollowingRoute && !hasIncremented) {
                    walkedDistance = 0.0 // Reiniciar distancia al comenzar
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff7FC297)), // Establece el color de fondo aquí
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .zIndex(1f) // Asegura que el botón permanezca encima
        ) {
            Text(
                text = if (isFollowingRoute) "Detener" else "Empezar",
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

// Función para obtener la vista del mapa con el ciclo de vida adecuado
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

// Función para obtener los puntos de la ruta desde Firebase
private fun fetchRouteFromFirebase(rutaId: String, onRouteReady: (List<LatLng>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val rutaCollection = db.collection("Coordenada")

    rutaCollection.whereEqualTo("id_ruta", rutaId.toIntOrNull())
        .orderBy("id")
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                Log.e("RouteDisplay", "No route points found for id_ruta = $rutaId")
            } else {
                val routePoints = documents.mapNotNull { document ->
                    val lat = document.getDouble("latitud") ?: return@mapNotNull null
                    val lng = document.getDouble("longitud") ?: return@mapNotNull null
                    LatLng(lat, lng)
                }
                onRouteReady(routePoints)
            }
        }
        .addOnFailureListener { exception ->
            Log.e("RouteDisplay", "Error fetching route points", exception)
        }
}

// Función para dibujar la ruta en el mapa
private fun drawRouteOnMap(map: GoogleMap, routePoints: List<LatLng>) {
    val polylineOptions = PolylineOptions()
        .addAll(routePoints)
        .color(android.graphics.Color.BLUE)
        .width(5f)
    map.addPolyline(polylineOptions)

    // Ajustar la cámara para incluir todos los puntos de la ruta
    val boundsBuilder = LatLngBounds.Builder()
    routePoints.forEach { boundsBuilder.include(it) }
    map.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100))
}

// Función para calcular la distancia total de la ruta
private fun calculateTotalDistance(routePoints: List<LatLng>): Double {
    var totalDistance = 0.0
    for (i in 0 until routePoints.size - 1) {
        totalDistance += haversineDistance(routePoints[i], routePoints[i + 1])
    }
    return totalDistance
}

// Función para rastrear la ubicación del usuario y actualizar la distancia recorrida
@SuppressLint("MissingPermission")
private fun startUserLocationTracking(
    googleMap: GoogleMap,
    fusedLocationClient: FusedLocationProviderClient,
    routePoints: List<LatLng>,
    onBearingCalculated: (Float) -> Unit,
    onDistanceUpdated: (Double) -> Unit
) {
    var previousLocation: LatLng? = null
    var accumulatedDistance = 0.0

    fusedLocationClient.requestLocationUpdates(
        LocationRequest.create().apply {
            interval = 5000 // Actualización cada 5 segundos
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        },
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val userLatLng = LatLng(location.latitude, location.longitude)

                    // Calcular distancia desde la última ubicación
                    previousLocation?.let {
                        accumulatedDistance += haversineDistance(it, userLatLng)
                        onDistanceUpdated(accumulatedDistance)
                    }
                    previousLocation = userLatLng

                    val nextPoint = routePoints.minByOrNull { haversineDistance(userLatLng, it) }

                    // Calcular el rumbo hacia el siguiente punto
                    nextPoint?.let {
                        val bearing = calculateBearing(userLatLng, it)
                        onBearingCalculated(bearing)
                    }

                    // Añadir marcador de ubicación del usuario
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(userLatLng)
                            .title("Tu Ubicación")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    )
                }
            }
        },
        null
    )
}

// Función para calcular el rumbo entre dos puntos LatLng
private fun calculateBearing(start: LatLng, end: LatLng): Float {
    val lat1 = Math.toRadians(start.latitude)
    val lon1 = Math.toRadians(start.longitude)
    val lat2 = Math.toRadians(end.latitude)
    val lon2 = Math.toRadians(end.longitude)
    val dLon = lon2 - lon1
    val y = sin(dLon) * cos(lat2)
    val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)
    return ((Math.toDegrees(atan2(y, x)) + 360) % 360).toFloat()
}

// Función para calcular la distancia Haversine entre dos puntos LatLng en metros
private fun haversineDistance(start: LatLng, end: LatLng): Double {
    val earthRadius = 6371e3 // en metros
    val dLat = Math.toRadians(end.latitude - start.latitude)
    val dLon = Math.toRadians(end.longitude - start.longitude)
    val lat1 = Math.toRadians(start.latitude)
    val lat2 = Math.toRadians(end.latitude)

    val a = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return earthRadius * c
}

// Función para verificar si la distancia recorrida es >= 90% de la distancia total y actualizar Firebase
private fun checkAndIncrementRoute(
    walkedDistance: Double,
    totalRouteDistance: Double,
    hasIncremented: Boolean
) {
    if (!hasIncremented && walkedDistance >= 0.9 * totalRouteDistance) {
        // Obtener el ID del usuario actual
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val db = FirebaseFirestore.getInstance()
            val userDocRef = db.collection("Usuario").document(it.uid)

            // Incrementar el atributo 'ruta' en 1
            userDocRef.update("rutas", FieldValue.increment(1))
                .addOnSuccessListener {
                    Log.d("RouteDisplay", "Atributo 'ruta' incrementado exitosamente.")
                }
                .addOnFailureListener { e ->
                    Log.e("RouteDisplay", "Error al incrementar el atributo 'ruta'.", e)
                }

        }
    }
}
