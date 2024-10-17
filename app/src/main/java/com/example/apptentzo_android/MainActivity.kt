package com.example.apptentzo_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apptentzo_android.ui.Camera.CameraScreen
import com.example.apptentzo_android.ui.Info.InfoScreen
import com.example.apptentzo_android.ui.Inicio.Logo
import com.example.apptentzo_android.ui.Login.Login
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

    NavHost(navController = navController, startDestination = "logo_screen") {
        composable("logo_screen") {
            Logo(navController) // Pantalla de logo sin barra de navegación
        }
        composable("login_screen") {
            Login(navController) // Pantalla de login sin barra de navegación
        }
        // Pantallas que tienen la barra de navegación
        composable("menu_screen") {
            Scaffold(
                bottomBar = { BottomNavigationBar(navController = navController, selectedScreen) { selectedScreen = it } }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                    HomeScreen() // Contenido de la pantalla de inicio
                }
            }
        }
        composable("library_screen") {
            Scaffold(
                bottomBar = { BottomNavigationBar(navController = navController, selectedScreen) { selectedScreen = it } }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                    PlantBank(navController)
                }
            }
        }
        composable("camera_screen") {
            Scaffold(
                bottomBar = { BottomNavigationBar(navController = navController, selectedScreen) { selectedScreen = it } }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                    CameraScreen()
                }
            }
        }
        composable("map_screen") {
            Scaffold(
                bottomBar = { BottomNavigationBar(navController = navController, selectedScreen) { selectedScreen = it } }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                    MapScreen()
                }
            }
        }
        composable("info_screen") {
            Scaffold(
                bottomBar = { BottomNavigationBar(navController = navController, selectedScreen) { selectedScreen = it } }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                    InfoScreen()
                }
            }
        }
        composable("plant_details_screen/{plantId}") { backStackEntry ->
            val plantId = backStackEntry.arguments?.getString("plantId")
            PlantInfo() // Implementa la lógica para la pantalla de detalles de la planta
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, selectedScreen: String, onScreenSelected: (String) -> Unit) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = 9.dp, y = -30.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = Color.White)
            .border(BorderStroke(1.dp, Color(0xffb6b6b6)),
                shape = RoundedCornerShape(15.dp))
    ) {
        NavigationBarItem(
            selected = selectedScreen == "menu_screen",
            onClick = {
                if (selectedScreen != "menu_screen") {
                    navController.navigate("menu_screen") {
                        popUpTo("menu_screen") { inclusive = true }
                    }
                    onScreenSelected("menu_screen")
                }
            },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.casaicon),
                    contentDescription = "Menu Icon",
                    modifier = Modifier.requiredWidth(30.dp).requiredHeight(30.dp)
                )
            }
        )
        NavigationBarItem(
            selected = selectedScreen == "library_screen",
            onClick = {
                if (selectedScreen != "library_screen") {
                    navController.navigate("library_screen") {
                        popUpTo("library_screen") { inclusive = true }
                    }
                    onScreenSelected("library_screen")
                }
            },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.planticon),
                    contentDescription = "Library Icon",
                    modifier = Modifier.requiredWidth(30.dp).requiredHeight(30.dp)
                )
            }
        )
        NavigationBarItem(
            selected = selectedScreen == "camera_screen",
            onClick = {
                if (selectedScreen != "camera_screen") {
                    navController.navigate("camera_screen") {
                        popUpTo("camera_screen") { inclusive = true }
                    }
                    onScreenSelected("camera_screen")
                }
            },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.camaraicon),
                    contentDescription = "Camera Icon",
                    modifier = Modifier.requiredWidth(30.dp).requiredHeight(30.dp)
                )
            }
        )
        NavigationBarItem(
            selected = selectedScreen == "map_screen",
            onClick = {
                if (selectedScreen != "map_screen") {
                    navController.navigate("map_screen") {
                        popUpTo("map_screen") { inclusive = true }
                    }
                    onScreenSelected("map_screen")
                }
            },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.mapaicon),
                    contentDescription = "Map Icon",
                    modifier = Modifier.requiredWidth(30.dp).requiredHeight(30.dp)
                )
            }
        )
        NavigationBarItem(
            selected = selectedScreen == "info_screen",
            onClick = {
                if (selectedScreen != "info_screen") {
                    navController.navigate("info_screen") {
                        popUpTo("info_screen") { inclusive = true }
                    }
                    onScreenSelected("info_screen")
                }
            },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.infoicon),
                    contentDescription = "Info Icon",
                    modifier = Modifier.requiredWidth(30.dp).requiredHeight(30.dp)
                )
            }
        )
    }
}
