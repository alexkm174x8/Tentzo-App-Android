package com.example.apptentzo_android.ui.Inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.apptentzo_android.R

@Composable
fun Logo(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 430.dp)
            .requiredHeight(height = 932.dp)
            .clip(shape = RoundedCornerShape(30.dp))
            .background(color = Color(0xff7fc297))
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // Asegúrate de que este recurso exista
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.Center)
                .requiredSize(size = 300.dp) // Ajusta el tamaño según sea necesario
        )
    }

    // Navegar después de 3 segundos
    LaunchedEffect(Unit) {
        delay(1000) // Espera 3 segundos
        navController.navigate("login_screen") { // Asegúrate de usar el mismo nombre que definiste en NavHost
            popUpTo("logo_screen") { inclusive = true } // Evitar que el usuario regrese a la pantalla del logo
        }
    }
}
