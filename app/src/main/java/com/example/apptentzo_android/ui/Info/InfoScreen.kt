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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptentzo_android.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer

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

    val actividades = listOf("Cenas con Arte", "Taller de barro")

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
                    .clip(RoundedCornerShape(30.dp))
                    .background(color = Color(0xffE9F4CA))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(68.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.face),
                        contentDescription = "Menu Icon",
                        modifier = Modifier
                            .requiredWidth(70.dp)
                            .requiredHeight(70.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.width(25.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(color = Color(0xffE9F4CA))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(68.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.insta),
                        contentDescription = "Menu Icon",
                        modifier = Modifier
                            .requiredWidth(70.dp)
                            .requiredHeight(70.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(25.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(color = Color(0xffE9F4CA))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(68.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.x),
                        contentDescription = "Menu Icon",
                        modifier = Modifier
                            .requiredWidth(70.dp)
                            .requiredHeight(70.dp)
                    )
                }
            }

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Más actividades",
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
       //
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x= 15.dp, y = 20.dp)

        ) {
            for (actividad in actividades) {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .width(400.dp)
                        .padding(10.dp)
                        .clickable {
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.actividadifo),
                        contentDescription = "Fondo de Actividad",
                        modifier = Modifier
                            .requiredWidth(width = 400.dp)
                            .requiredHeight(height = 150.dp)
                            .clip(shape = RoundedCornerShape(20.dp))
                    )

                    Text(
                        text = actividad,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.BottomStart)
                            .offset(x= 20.dp, y = 20.dp),
                    )
                }
                Spacer(modifier = Modifier.height(70.dp))
            }
        }

    }
}


@Preview(showBackground = true, heightDp = 932, widthDp = 430)
@Composable
fun Info() {
    InfoScreen()
}
