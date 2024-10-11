package com.example.apptentzo_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apptentzo_android.ui.Info.InfoScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.tooling.preview.Preview
import com.example.apptentzo_android.ui.Camera.CameraScreen
import com.example.apptentzo_android.ui.Map.MapScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationHost(navController = navController)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            label = { Text("Menu") },
            selected = false,
            onClick = { navController.navigate("menu_screen") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Menu Icon") }
        )
        NavigationBarItem(
            label = { Text("Library") },
            selected = false,
            onClick = { navController.navigate("library_screen") },
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Plant Icon") }
        )
        NavigationBarItem(
            label = { Text("Camera") },
            selected = false,
            onClick = { navController.navigate("camera_screen") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Camera Icon") }
        )
        NavigationBarItem(
            label = { Text("Map") },
            selected = false,
            onClick = { navController.navigate("map_screen") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Map Icon") }
        )
        NavigationBarItem(
            label = { Text("Info") },
            selected = false,
            onClick = { navController.navigate("info_screen") },
            icon = { Icon(Icons.Default.Info, contentDescription = "Info Icon") }
        )

    }
}

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "info_screen") {
        composable("camera_screen") { CameraScreen() }
        composable("map_screen") { MapScreen() }
        composable("info_screen") { InfoScreen() }


    }
}

@Preview(showBackground = true, heightDp = 932, widthDp = 430)
@Composable
fun Main() {
    MainScreen()
}