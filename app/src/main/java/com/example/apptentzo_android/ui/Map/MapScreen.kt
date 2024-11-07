package com.example.apptentzo_android.ui.Map

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


class MapActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapScreen()
        }
    }
}

@Composable
fun MapScreen() {
    var boxHeight by remember { mutableStateOf(200.dp) }
    val minHeight = 200.dp
    val maxHeight = 700.dp
    val density = LocalDensity.current.density // Get the density here

    // Permission handling code
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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // GoogleMap at the background
        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            properties = MapProperties(isMyLocationEnabled = isLocationPermissionGranted),
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        )

        // Box overlay at the bottom of the map
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
                        val dragInDp = (dragAmount.y / density).dp // Use density directly here
                        boxHeight = (boxHeight - dragInDp).coerceIn(minHeight, maxHeight)
                    }
                }
        ) {
            // Content of the Box
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(25.dp))
                            .background(Color.Blue)
                            .height(115.dp)
                            .width(400.dp)
                            .padding(10.dp)
                            .clickable {
                                // Click action
                            }
                    ) {
                        Text(
                            text = "Ruta 1",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.align(Alignment.BottomStart)
                        )
                    }
                }
            }
        }
    }
}

private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}

@Preview(showBackground = true, heightDp = 932, widthDp = 430)
@Composable
fun Map() {
    MapScreen()
}
