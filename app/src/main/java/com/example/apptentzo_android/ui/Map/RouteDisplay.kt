package com.example.apptentzo_android.ui.Map

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    Box(modifier = Modifier.fillMaxSize()) {
        // Back button
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

        // Map view
        AndroidView(factory = { mapView }) { androidMapView ->
            coroutineScope.launch {
                val googleMap = androidMapView.awaitMap()

                // Enable location tracking on map
                googleMap.isMyLocationEnabled = true

                rutaId?.let {
                    fetchRouteFromFirebase(it) { routePoints ->
                        if (routePoints.isNotEmpty()) {
                            drawRouteOnMap(googleMap, routePoints)
                            isRouteLoading = false

                            if (isFollowingRoute) {
                                startUserLocationTracking(
                                    googleMap = googleMap,
                                    fusedLocationClient = fusedLocationClient,
                                    routePoints = routePoints,
                                    onBearingCalculated = { bearing ->
                                        nextPointBearing = bearing
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

        // Rotating arrow on top of user's location
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
                        .graphicsLayer(rotationZ = nextPointBearing) // Rotates the arrow
                )
            }
        }

        // Loading indicator
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

        // Button to start/stop route-following mode
        Button(
            onClick = { isFollowingRoute = !isFollowingRoute },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .background(Color.Green.copy(alpha = 0.7f), shape = CircleShape)
                .zIndex(1f) // Ensure the button stays on top
        ) {
            Text(
                text = if (isFollowingRoute) "Stop Following" else "Start Following",
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

// Function to fetch route points from Firebase
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

// Function to draw the route on the map
private fun drawRouteOnMap(map: GoogleMap, routePoints: List<LatLng>) {
    val polylineOptions = PolylineOptions()
        .addAll(routePoints)
        .color(android.graphics.Color.BLUE)
        .width(5f)
    map.addPolyline(polylineOptions)

    // Adjust camera to include all route points
    val boundsBuilder = LatLngBounds.Builder()
    routePoints.forEach { boundsBuilder.include(it) }
    map.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100))
}

// Function to start tracking user's location and calculate direction
@SuppressLint("MissingPermission")
private fun startUserLocationTracking(
    googleMap: GoogleMap,
    fusedLocationClient: FusedLocationProviderClient,
    routePoints: List<LatLng>,
    onBearingCalculated: (Float) -> Unit
) {
    fusedLocationClient.requestLocationUpdates(
        LocationRequest.create().apply {
            interval = 5000 // Update every 5 seconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        },
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    val nextPoint = routePoints.minByOrNull { haversineDistance(userLatLng, it) }

                    // Calculate bearing to the next point
                    nextPoint?.let {
                        val bearing = calculateBearing(userLatLng, it)
                        onBearingCalculated(bearing)
                    }

                    googleMap.addMarker(
                        MarkerOptions()
                            .position(userLatLng)
                            .title("Your Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    )
                }
            }
        },
        null
    )
}

// Function to calculate bearing between two LatLng points
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

// Haversine distance calculation (for finding nearest point)
private fun haversineDistance(start: LatLng, end: LatLng): Double {
    val earthRadius = 6371e3 // in meters
    val dLat = Math.toRadians(end.latitude - start.latitude)
    val dLon = Math.toRadians(end.longitude - start.longitude)
    val lat1 = Math.toRadians(start.latitude)
    val lat2 = Math.toRadians(end.latitude)

    val a = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return earthRadius * c
}

// MapView lifecycle handling
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
