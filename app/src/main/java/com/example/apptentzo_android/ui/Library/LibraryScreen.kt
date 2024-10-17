package com.example.biblioteca

import com.example.apptentzo_android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantBank(navController: NavHostController) {

    var text by remember { mutableStateOf<String>("") }
    Box(
        modifier = Modifier
            .requiredWidth(width = 430.dp)
            .requiredHeight(height = 932.dp)
            .clip(shape = RoundedCornerShape(30.dp))
            .background(color = Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.hoja),
            contentDescription = "hoja",
            modifier = Modifier
                .requiredSize(size = 40.dp)
                .offset(
                    x = 34.dp,
                    y = 41.dp
                ))
        Text(
            text = "Biblioteca",
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 88.dp,
                    y = 41.dp
                ))
        Box(
            modifier = Modifier
                .offset(
                    x = 18.dp,
                    y = 113.dp
                )
                .requiredWidth(width = 385.dp)
                .requiredHeight(height = 54.dp)
                .clip(shape = RoundedCornerShape(9999.dp))
                .border(
                    border = BorderStroke(1.dp, Color(0xffd9d9d9)),
                    shape = RoundedCornerShape(9999.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically),
                placeholder = {
                    Text(text = "Buscar...", color = Color.Gray)
                },
                singleLine = true
            )
        }


        IconButton(
            onClick = { } ,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 352.dp, y = 122.dp)
                .requiredWidth(35.dp)
                .requiredHeight(36.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.buscar),
                contentDescription = "buscar",
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        // Primera fila
        Box(
            modifier = Modifier
                .offset(x = 28.dp, y = 197.dp)
                .requiredWidth(109.dp)
                .requiredHeight(106.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                    navController.navigate("plant_details_screen/1") // Cambia "1" por el ID real de la planta
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.flor1),
                contentDescription = "flor1",
                modifier = Modifier
                    .requiredWidth(92.dp)
                    .requiredHeight(93.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = "Especie 1",
            color = Color(0xff121d29),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .offset(
                    x = 46.dp,
                    y = 307.dp
                )
                .requiredWidth(109.dp)
                .align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .offset(x = 161.dp, y = 197.dp)
                .requiredWidth(109.dp)
                .requiredHeight(106.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.flor2),
                contentDescription = "flor2",
                modifier = Modifier
                    .requiredWidth(92.dp)
                    .requiredHeight(93.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = "Especie 2",
            color = Color(0xff121d29),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .offset(x = 174.dp, y = 307.dp)
                .requiredWidth(109.dp)
                .align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .offset(x = 294.dp, y = 197.dp)
                .requiredWidth(109.dp)
                .requiredHeight(106.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.flor3),
                contentDescription = "flor3",
                modifier = Modifier
                    .requiredWidth(92.dp)
                    .requiredHeight(93.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = "Especie 3",
            color = Color(0xff121d29),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .offset(x = 310.dp, y = 307.dp)
                .requiredWidth(109.dp)
                .align(Alignment.TopStart)
        )

        // Segunda fila
        Box(
            modifier = Modifier
                .offset(x = 28.dp, y = 358.dp)
                .requiredWidth(109.dp)
                .requiredHeight(106.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.flor4),
                contentDescription = "flor4",
                modifier = Modifier
                    .requiredWidth(92.dp)
                    .requiredHeight(93.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = "Especie 4",
            color = Color(0xff121d29),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .offset(x = 46.dp, y = 468.dp)
                .requiredWidth(109.dp)
                .align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .offset(x = 161.dp, y = 358.dp)
                .requiredWidth(109.dp)
                .requiredHeight(106.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.flor5),
                contentDescription = "flor5",
                modifier = Modifier
                    .requiredWidth(92.dp)
                    .requiredHeight(93.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = "Especie 5",
            color = Color(0xff121d29),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .offset(x = 174.dp, y = 468.dp)
                .requiredWidth(109.dp)
                .align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .offset(x = 294.dp, y = 358.dp)
                .requiredWidth(109.dp)
                .requiredHeight(106.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.flor6),
                contentDescription = "flor6",
                modifier = Modifier
                    .requiredWidth(92.dp)
                    .requiredHeight(93.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = "Especie 6",
            color = Color(0xff121d29),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .offset(x = 310.dp, y = 468.dp)
                .requiredWidth(109.dp)
                .align(Alignment.TopStart)
        )

        // Tercera fila
        Box(
            modifier = Modifier
                .offset(x = 28.dp, y = 519.dp)
                .requiredWidth(109.dp)
                .requiredHeight(106.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.flor7),
                contentDescription = "flor7",
                modifier = Modifier
                    .requiredWidth(92.dp)
                    .requiredHeight(93.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = "Especie 7",
            color = Color(0xff121d29),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .offset(x = 46.dp, y = 629.dp)
                .requiredWidth(109.dp)
                .align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .offset(x = 161.dp, y = 519.dp)
                .requiredWidth(109.dp)
                .requiredHeight(106.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.flor8),
                contentDescription = "flor8",
                modifier = Modifier
                    .requiredWidth(92.dp)
                    .requiredHeight(93.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = "Especie 8",
            color = Color(0xff121d29),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .offset(x = 174.dp, y = 629.dp)
                .requiredWidth(109.dp)
                .align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .offset(x = 294.dp, y = 519.dp)
                .requiredWidth(109.dp)
                .requiredHeight(106.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.flor9),
                contentDescription = "flor9",
                modifier = Modifier
                    .requiredWidth(92.dp)
                    .requiredHeight(93.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = "Especie 9",
            color = Color(0xff121d29),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .offset(x = 310.dp, y = 629.dp)
                .requiredWidth(109.dp)
                .align(Alignment.TopStart)

        )

        // Cuarta fila
        Box(
            modifier = Modifier
                .offset(x = 28.dp, y = 680.dp)
                .requiredWidth(109.dp)
                .requiredHeight(106.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.flor10),
                contentDescription = "flor10",
                modifier = Modifier
                    .requiredWidth(92.dp)
                    .requiredHeight(93.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = "Especie 10",
            color = Color(0xff121d29),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .offset(x = 46.dp, y = 790.dp)
                .requiredWidth(109.dp)
                .align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .offset(x = 161.dp, y = 680.dp)
                .requiredWidth(109.dp)
                .requiredHeight(106.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.flor11),
                contentDescription = "flor11",
                modifier = Modifier
                    .requiredWidth(92.dp)
                    .requiredHeight(93.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = "Especie 11",
            color = Color(0xff121d29),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .offset(x = 174.dp, y = 790.dp)
                .requiredWidth(109.dp)
                .align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .offset(x = 294.dp, y = 680.dp)
                .requiredWidth(109.dp)
                .requiredHeight(106.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.flor12),
                contentDescription = "flor12",
                modifier = Modifier
                    .requiredWidth(92.dp)
                    .requiredHeight(93.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = "Especie 12",
            color = Color(0xff121d29),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .offset(x = 310.dp, y = 790.dp)
                .requiredWidth(109.dp)
                .align(Alignment.TopStart)
        )

    }
}


@Preview(widthDp = 430, heightDp = 932)
@Composable
private fun PlantBankPreview() {
    val navController = rememberNavController()
    PlantBank(navController = navController)
}
