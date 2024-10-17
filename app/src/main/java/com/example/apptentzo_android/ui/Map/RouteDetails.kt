package com.example.apptentzo_android.ui.Map

import android.os.Bundle
import com.example.apptentzo_android.R
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign


class RouteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RouteDetails()
        }
    }
}

@Composable
fun RouteDetails(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 430.dp)
            .requiredHeight(height = 932.dp)
    ) {
        Box(
            modifier = Modifier
                .requiredWidth(width = 430.dp)
                .requiredHeight(height = 932.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(color = Color.White)
        ) {
            Image(
                painter = painterResource(id = R.drawable.rutafondo),
                contentDescription = "rutafondo",
                modifier = modifier
                    .requiredWidth(width = 430.dp)
                    .requiredHeight(height = 437.dp)
                    .offset(y = -40.dp))
            Text(
                text = "60 min",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Light),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 278.dp,
                        y = 444.dp))
            Text(
                text = "3 km",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Light),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 62.dp,
                        y = 444.dp))
            Text(
                text = "Distancia:",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 28.dp,
                        y = 398.dp))
            Text(
                text = "Tiempo:",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 263.dp,
                        y = 398.dp))
            Text(
                text = "Detalles:",
                color = Color.Black,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 21.dp,
                        y = 540.dp)
                    .requiredWidth(width = 291.dp))
            Text(
                text = "Ruta Tentzo",
                color = Color.White.copy(alpha = 0.89f),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 79.dp,
                        y = 52.dp))
            Text(
                text = "La ruta Tentzo tiene una longitud de 8 kilómetros. A lo largo del recorrido, podrás disfrutar de una gran variedad de ecosistemas, desde bosques de pinos hasta áreas abiertas con praderas floridas.",
                color = Color(0xff1e3045),
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 21.dp,
                        y = 560.dp)
                    .requiredWidth(width = 380.dp)
                    .requiredHeight(height = 194.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically))

            Divider(
                color = Color(0xffd1d1d1),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 28.dp,
                        y = 510.dp)
                    .requiredWidth(width = 380.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 45.dp,
                        y = 763.dp)
                    .requiredWidth(width = 340.dp)
                    .requiredHeight(height = 56.dp)
                    .clip(shape = RoundedCornerShape(30.dp))
                    .background(color = Color(0xff7fc297))
                    .clickable {
                        // Acción al presionar el botón de emergencia
                    })
                Text(
                    text = "Iniciar ruta",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 81.dp,
                            y = 773.dp
                        )
                        .requiredWidth(width = 267.dp)
                        .requiredHeight(height = 35.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            Image(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "arrow_back",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp,
                        end = 373.dp,
                        top = 42.dp,
                        bottom = 831.dp))
        }
}
}

@Preview(showBackground = true, heightDp = 932, widthDp = 430)
@Composable
fun Routes() {
    RouteDetails()
}
