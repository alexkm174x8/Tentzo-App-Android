package com.example.apptentzo_android.ui.Info

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class RouteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Info()
        }
    }
}

@Composable
fun InfoScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Redes Sociales",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 25.sp,
                modifier = Modifier.padding(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Divider(
                modifier = Modifier
                    .height(1.dp)
                    .weight(1f)
                    .padding(end = 10.dp),
                color = Color.Gray,
                thickness = 1.dp
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
            ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Blue)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {

            }
            Spacer(modifier = Modifier.width(25.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Blue)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {

            }
            Spacer(modifier = Modifier.width(25.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Blue)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {

            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mis Actividades",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 25.sp,
                modifier = Modifier.padding(20.dp)
            )
        }

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
                        //navController.navigate("detalle")
                    }
            ) {
                Text(
                    text = "Cenas con Arte",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
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
                        //navController.navigate("detalle")
                    }
            ) {
                Text(
                    text = "Taller de barro",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
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
                        //navController.navigate("detalle")
                    }
            ) {
                Text(
                    text = "Taller de plantas",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}


@Preview(showBackground = true, heightDp = 932, widthDp = 430)
@Composable
fun Info() {
    InfoScreen()
}
