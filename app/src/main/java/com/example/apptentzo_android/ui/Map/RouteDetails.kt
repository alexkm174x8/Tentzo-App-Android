package com.example.apptentzo_android.ui.Map

import android.os.Bundle
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
fun RouteDetails() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .fillMaxWidth()
                .height(600.dp)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.width(200.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Distancia:",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Column(
                        modifier = Modifier.width(200.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Tiempo:",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.width(200.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "3 km",
                            fontSize = 30.sp,
                            color = Color.Black
                        )
                    }
                    Column(
                        modifier = Modifier.width(200.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "60 min",
                            fontSize = 30.sp,
                            color = Color.Black
                        )
                    }
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                Row{
                    Text(
                        text = "Detalles:",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Row {
                    Text(
                        text = "La ruta \"Tentzo\" tiene una longitud de 8 kilómetros. A lo largo del recorrido, podrás disfrutar de una gran variedad de ecosistemas, desde bosques de pinos hasta áreas abiertas con praderas floridas.",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 20.dp),
                    )
                }
                Row (
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 90.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(16.dp)
                            .width(300.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FC297))
                    ) {
                        Text(
                            text = "Iniciar Ruta",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 932, widthDp = 430)
@Composable
fun Routes() {
    RouteDetails()
}
