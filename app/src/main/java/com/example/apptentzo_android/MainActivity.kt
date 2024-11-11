package com.example.apptentzo_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.apptentzo_android.ui.Camera.CameraScreen
import com.example.apptentzo_android.ui.Info.InfoDetails
import com.example.apptentzo_android.ui.Info.InfoScreen
import com.example.apptentzo_android.ui.Inicio.Logo
import com.example.apptentzo_android.ui.Login.Login
import com.example.apptentzo_android.ui.Map.MapScreen
import com.example.apptentzo_android.ui.Map.RouteDetails
import com.example.apptentzo_android.ui.Map.RouteDisplayContent
import com.example.apptentzo_android.ui.Menu.HomeScreen
import com.example.apptentzo_android.ui.SignIn.SignIn
import com.example.biblioteca.PlantBank
import com.google.firebase.auth.FirebaseAuth

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
    val mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser

    // Observa la ruta actual para mostrar u ocultar la barra de navegación
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in listOf(
        "menu_screen",
        "library_screen",
        "camera_screen",
        "map_screen",
        "info_screen"
    )

    if (currentUser != null) {
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(navController)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "menu_screen",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("menu_screen") {
                    HomeScreen(navController)
                }
                composable("library_screen") {
                    PlantBank(navController)
                }
                composable("library_screen") {
                    PlantBank(navController = navController)
                }
                composable(
                    route = "plant_details_screen/{plantaId}",
                    arguments = listOf(navArgument("plantaId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val plantaId = backStackEntry.arguments?.getString("plantaId") ?: ""
                    PlantInfo(navController = navController, plantaId = plantaId)
                }
                composable("camera_screen") {
                    CameraScreen()
                }
                composable("map_screen") {
                    MapScreen(navController)
                }
                composable("info_screen") {
                    InfoScreen(navController)
                }
                composable("InfoDetails/{actividadId}") { backStackEntry ->
                    val actividadId = backStackEntry.arguments?.getString("actividadId")
                    actividadId?.let {
                        InfoDetails(actividadId = it, navController = navController)
                    }
                }
                // Actualización: RouteDetails ahora recibe rutaId como parámetro
                composable(
                    "RouteDetails/{rutaId}",
                    arguments = listOf(navArgument("rutaId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val rutaId = backStackEntry.arguments?.getString("rutaId")
                    rutaId?.let {
                        RouteDetails(navController = navController, rutaId = it)
                    }
                }
                composable("RouteDisplay") {
                    RouteDisplayContent()
                }

            }
        }
    } else {
        NavHost(navController = navController, startDestination = "logo_screen") {
            composable("logo_screen") {
                Logo(navController)
            }
            composable("login_screen") {
                Login(navController)
            }
            composable("signin_screen") {
                SignIn(navController)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, Color(0xffb6b6b6)), RoundedCornerShape(15.dp))
    ) {
        NavigationBarItem(
            selected = currentRoute == "menu_screen",
            onClick = {
                if (currentRoute != "menu_screen") {
                    navController.navigate("menu_screen") {
                        popUpTo("menu_screen") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.casaicon),
                    contentDescription = "Menu Icon",
                    modifier = Modifier.size(32.dp)
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == "library_screen",
            onClick = {
                if (currentRoute != "library_screen") {
                    navController.navigate("library_screen") {
                        popUpTo("library_screen") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.planticon),
                    contentDescription = "Library Icon",
                    modifier = Modifier.size(32.dp)
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == "camera_screen",
            onClick = {
                if (currentRoute != "camera_screen") {
                    navController.navigate("camera_screen") {
                        popUpTo("camera_screen") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.camaraicon),
                    contentDescription = "Camera Icon",
                    modifier = Modifier.size(32.dp)
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == "map_screen",
            onClick = {
                if (currentRoute != "map_screen") {
                    navController.navigate("map_screen") {
                        popUpTo("map_screen") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.mapaicon),
                    contentDescription = "Map Icon",
                    modifier = Modifier.size(32.dp)
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == "info_screen",
            onClick = {
                if (currentRoute != "info_screen") {
                    navController.navigate("info_screen") {
                        popUpTo("info_screen") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.infoicon),
                    contentDescription = "Info Icon",
                    modifier = Modifier.size(32.dp)
                )
            }
        )
    }
}
