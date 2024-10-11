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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.apptentzo_android.ui.Camera.CameraScreen
import com.example.apptentzo_android.ui.Map.MapScreen
import com.example.apptentzo_android.ui.Menu.HomeScreen
import com.example.biblioteca.PlantBank


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
    var selectedScreen by remember { mutableStateOf("menu_screen") } // Estado para la pantalla seleccionada

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, selectedScreen) { selectedScreen = it } }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationHost(navController = navController) { route ->
                selectedScreen = route // Actualiza la pantalla seleccionada
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, selectedScreen: String, onScreenSelected: (String) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            label = { Text("Menu") },
            selected = selectedScreen == "menu_screen",
            onClick = {
                if (selectedScreen != "menu_screen") { // Solo navega si no está en la pantalla seleccionada
                    navController.navigate("menu_screen") {
                        popUpTo("menu_screen") { inclusive = true } // Evitar apilar
                    }
                    onScreenSelected("menu_screen")
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Menu Icon") }
        )
        NavigationBarItem(
            label = { Text("Library") },
            selected = selectedScreen == "library_screen",
            onClick = {
                if (selectedScreen != "library_screen") {
                    navController.navigate("library_screen") {
                        popUpTo("library_screen") { inclusive = true }
                    }
                    onScreenSelected("library_screen")
                }
            },
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Plant Icon") }
        )
        NavigationBarItem(
            label = { Text("Camera") },
            selected = selectedScreen == "camera_screen",
            onClick = {
                if (selectedScreen != "camera_screen") {
                    navController.navigate("camera_screen") {
                        popUpTo("camera_screen") { inclusive = true }
                    }
                    onScreenSelected("camera_screen")
                }
            },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Camera Icon") }
        )
        NavigationBarItem(
            label = { Text("Map") },
            selected = selectedScreen == "map_screen",
            onClick = {
                if (selectedScreen != "map_screen") {
                    navController.navigate("map_screen") {
                        popUpTo("map_screen") { inclusive = true }
                    }
                    onScreenSelected("map_screen")
                }
            },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Map Icon") }
        )
        NavigationBarItem(
            label = { Text("Info") },
            selected = selectedScreen == "info_screen",
            onClick = {
                if (selectedScreen != "info_screen") {
                    navController.navigate("info_screen") {
                        popUpTo("info_screen") { inclusive = true }
                    }
                    onScreenSelected("info_screen")
                }
            },
            icon = { Icon(Icons.Default.Info, contentDescription = "Info Icon") }
        )
    }
}

@Composable
fun NavigationHost(navController: NavHostController, onRouteChanged: (String) -> Unit) {
    NavHost(navController = navController, startDestination = "menu_screen") {
        composable("menu_screen") { HomeScreen().also { onRouteChanged("menu_screen") } }
        composable("library_screen") {
            PlantBank(navController) // Pasa el navController a LibraryScreen
                .also { onRouteChanged("library_screen") }
        }
        composable("camera_screen") { CameraScreen().also { onRouteChanged("camera_screen") } }
        composable("map_screen") { MapScreen().also { onRouteChanged("map_screen") } }
        composable("info_screen") { InfoScreen().also { onRouteChanged("info_screen") } }
        composable("plant_details_screen/{plantId}") { backStackEntry ->
            // Obtiene el ID de la planta desde la ruta
            val plantId = backStackEntry.arguments?.getString("plantId")
            PlantInfo()//plantId = plantId) // Llama a la pantalla de detalles de la planta
        }
    }
}

@Preview(showBackground = true, heightDp = 932, widthDp = 430)
@Composable
fun Main() {
    MainScreen()
}