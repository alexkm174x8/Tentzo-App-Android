package com.example.apptentzo_android.ui.Menu

import com.example.apptentzo_android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen()
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    var showDialog2 by remember { mutableStateOf(false) }
    var showDialog3 by remember { mutableStateOf(false) }
    var showDialog4 by remember { mutableStateOf(false) }
    var showDialog5 by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .requiredWidth(width = 430.dp)
            .requiredHeight(height = 932.dp)
            .clip(shape = RoundedCornerShape(30.dp))
            .background(color = Color.White)
    ) {
        Text(
            text = "¡Hola, Estefanía!",
            color = Color.Black,
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 19.dp,
                    y = 47.dp
                )
                .wrapContentHeight(align = Alignment.CenterVertically))
        Text(
            text = "Mis insignias",
            color = Color.Black,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 29.dp,
                    y = 274.dp
                )
                .requiredWidth(width = 228.dp)
                .requiredHeight(height = 28.dp))
        Text(
            text = "Mis Servicios",
            color = Color.Black,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 29.dp,
                    y = 461.dp
                )
                .requiredWidth(width = 228.dp)
                .requiredHeight(height = 28.dp))



        val insignias = listOf(
            R.drawable.insignia1,
            R.drawable.insignia2,
            R.drawable.insignia3,
            R.drawable.insignia4,
            R.drawable.insignia4
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 330.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(insignias.size) { index ->
                Image(
                    painter = painterResource(id = insignias[index]),
                    contentDescription = "insignia",
                    modifier = Modifier
                        .size(87.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.sol),
            contentDescription = "sol",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 184.dp,
                    y = 103.dp
                )
                .requiredWidth(width = 87.dp)
                .requiredHeight(height = 85.dp))
        Text(
            text = "25 °C",
            color = Color(0xff2e354d),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium),
            modifier = modifier
                .requiredWidth(width = 131.dp)
                .requiredHeight(height = 48.dp)
                .offset(
                    x = 270.dp,
                    y = 120.dp
                )
        )


        Divider(
            color = Color(0xffd1d1d1),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 28.dp,
                    y = 438.dp
                )
                .requiredWidth(width = 380.dp))
        Divider(
            color = Color(0xffd1d1d1),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 201.dp,
                    y = 288.dp
                )
                .requiredWidth(width = 204.dp))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 15.dp,
                    y = 513.dp
                )
                .requiredWidth(width = 187.dp)
                .requiredHeight(height = 87.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(color = Color(0xffe9f4ca)))
        Text(
            text = "pasos",
            color = Color(0xff594949),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 85.dp,
                    y = 561.dp
                )
                .requiredWidth(width = 100.dp)
                .requiredHeight(height = 35.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
        Image(
            painter = painterResource(id = R.drawable.tenis),
            contentDescription = "tenis",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 30.dp,
                    y = 525.dp
                )
                .requiredWidth(width = 60.dp)
                .requiredHeight(height = 59.dp))

        Text(
            text = "1,236",
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 90.dp,
                    y = 527.dp
                )
                .requiredWidth(width = 100.dp)
                .requiredHeight(height = 35.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 17.dp,
                    y = 629.dp
                )
                .requiredWidth(width = 189.dp)
                .requiredHeight(height = 87.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(color = Color(0xffe9f4ca)))
        Text(
            text = "minutos",
            color = Color(0xff594949),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 97.dp,
                    y = 674.dp
                )
                .requiredWidth(width = 100.dp)
                .requiredHeight(height = 35.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
        Text(
            text = "66",
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 94.dp,
                    y = 643.dp
                )
                .requiredWidth(width = 100.dp)
                .requiredHeight(height = 35.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
        Image(
            painter = painterResource(id = R.drawable.clock),
            contentDescription = "clock",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 34.dp,
                    y = 645.dp
                )
                .requiredWidth(width = 60.dp)
                .requiredHeight(height = 59.dp))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 226.dp, y = 461.dp)
                .requiredWidth(189.dp)
                .requiredHeight(151.dp)
                .clip(RoundedCornerShape(30.dp))
                .clickable {
                    showDialog4 = true
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.flordia),
                contentDescription = "flordia",
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 52.dp, y = 760.dp)
                .requiredWidth(340.dp)
                .requiredHeight(56.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(color = Color(0xff7FC297))
                .clickable {
                    // Acción al presionar el botón de emergencia
                }
        ) {
            Text(
                text = "Emergencia",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .align(alignment = Alignment.Center) // Alinea el texto en el centro del Box
                    .fillMaxSize() // A
                    .wrapContentHeight(align = Alignment.CenterVertically) // Alinea verticalmente
            )
        }

        Box(
            modifier = Modifier
                .offset(x = 349.dp, y = 36.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xffb6b6b6)),
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .requiredWidth(width = 56.dp)
                    .requiredHeight(height = 60.dp)
            ) {
                IconButton(
                    onClick = {
                        showDialog5 = true
                    },
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "Log out",
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .requiredSize(size = 37.dp)
                    )
                }
            }
        }


        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 226.dp,
                    y = 629.dp
                )
                .requiredWidth(width = 189.dp)
                .requiredHeight(height = 87.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(color = Color(0xffe9f4ca)))
        Text(
            text = "120",
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 307.dp,
                    y = 643.dp
                )
                .requiredWidth(width = 100.dp)
                .requiredHeight(height = 35.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
        Image(
            painter = painterResource(id = R.drawable.corazon),
            contentDescription = "corazon",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 241.dp,
                    y = 643.dp
                )
                .requiredWidth(width = 72.dp)
                .requiredHeight(height = 59.dp))
        Text(
            text = "kcal",
            color = Color(0xff594949),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 307.dp,
                    y = 674.dp
                )
                .requiredWidth(width = 100.dp)
                .requiredHeight(height = 35.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))


        IconButton(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 19.dp, y = 103.dp)
                .requiredSize(134.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.fani),
                contentDescription = "fani",
                modifier = Modifier
                    .requiredSize(134.dp)
                    .clip(shape = RoundedCornerShape(100.dp))
            )
        }


        IconButton(
            onClick = {
                showDialog2 = true
            },
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 122.dp, y = 195.dp)
                .requiredWidth(42.dp)
                .requiredHeight(46.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "edit",
                modifier = Modifier
                    .requiredWidth(35.dp)
                    .requiredHeight(40.dp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.paisaje),
            contentDescription = "paisaje",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 192.dp,
                    y = 175.dp
                )
                .requiredWidth(width = 216.dp)
                .requiredHeight(height = 83.dp))

        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false },
                properties = DialogProperties(dismissOnBackPress = true)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 368.dp, height = 351.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fani),
                        contentDescription = "fani",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(x = (-4).dp, y = (-13).dp)
                            .requiredWidth(width = 377.dp)
                            .requiredHeight(height = 378.dp)
                    )
                }
            }
        }

        if (showDialog2) {
            Dialog(
                onDismissRequest = { showDialog2 = false },
                properties = DialogProperties(dismissOnBackPress = true)
            ) {
                Box(
                    modifier = modifier
                        .requiredWidth(width = 303.dp)
                        .requiredHeight(height = 317.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(color = Color(0xff7fc297))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.importar),
                        contentDescription = "importar",
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(x = 34.dp,
                                y = 44.dp)
                            .requiredWidth(width = 231.dp)
                            .requiredHeight(height = 238.dp))
                }
            }
        }

        if (showDialog3) {
            Dialog(
                onDismissRequest = { showDialog3 = false },
                properties = DialogProperties(dismissOnBackPress = true)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 368.dp, height = 351.dp)
                        .background(Color.White, shape = RoundedCornerShape(30.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fani),
                        contentDescription = "fani",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(x = (-4).dp, y = (-13).dp)
                            .requiredWidth(width = 377.dp)
                            .requiredHeight(height = 378.dp)
                    )
                }
            }
        }

        if (showDialog4) {
            Dialog(
                onDismissRequest = { showDialog4 = false },
                properties = DialogProperties(dismissOnBackPress = true)
            ) {
                Box(
                    modifier = modifier
                        .requiredWidth(width = 384.dp)
                        .requiredHeight(height = 494.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                        .border(border = BorderStroke(3.dp, Color.White),
                            shape = RoundedCornerShape(30.dp))
                ) {
                    Text(
                        text = "Flor del Día",
                        color = Color(0xff000003),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(x = 49.dp,
                                y = 247.dp)
                            .requiredWidth(width = 285.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically))
                    Text(
                        text = "Descripción",
                        color = Color(0xff000003),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Light),
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(x = 50.dp,
                                y = 301.dp)
                            .requiredWidth(width = 285.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically))
                    Image(
                        painter = painterResource(id = R.drawable.flor),
                        contentDescription = "Rectangle 54",
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(x = 18.dp,
                                y = 28.dp)
                            .requiredWidth(width = 347.dp)
                            .requiredHeight(height = 204.dp)
                            .clip(shape = RoundedCornerShape(30.dp)))
                }
            }
        }

        if (showDialog5) {
            Dialog(
                onDismissRequest = { showDialog5 = false },
                properties = DialogProperties(dismissOnBackPress = true)
            ){
                Surface(
                    shape = RoundedCornerShape(30.dp),
                    color = Color.White,
                    border = BorderStroke(3.dp, Color.White),
                    modifier = modifier
                        .clip(shape = RoundedCornerShape(30.dp))
                ){
                    Box(
                        modifier = Modifier
                            .requiredWidth(width = 343.dp)
                            .requiredHeight(height = 179.dp)
                    ){
                        Text(
                            text = "¿Estás seguro de cerrar sesión?",
                            color = Color(0xff000003),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(x = 22.dp, y = 22.dp)
                                .requiredWidth(width = 299.dp)
                                .requiredHeight(height = 62.dp)
                                .wrapContentHeight(align = Alignment.CenterVertically)
                        )

                        // Botón para Cerrar Sesión
                        Box(
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(x = 22.dp, y = 101.dp)
                                .requiredWidth(width = 149.dp)
                                .requiredHeight(height = 49.dp)
                                .clip(shape = RoundedCornerShape(25.dp))
                                .background(color = Color(0xff7fc297))
                                .clickable {
                                    // Manejar la acción de cerrar sesión aquí
                                }
                        ) {
                            Text(
                                text = "Cerrar Sesión",
                                color = Color(0xff000003),
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontSize = 18.sp),
                                modifier = Modifier
                                    .fillMaxSize() // Hace que el texto ocupe todo el tamaño del botón
                                    .wrapContentHeight(align = Alignment.CenterVertically)
                            )
                        }

                        // Botón para Cancelar
                        Box(
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(x = 185.dp, y = 104.dp)
                                .requiredWidth(width = 142.dp)
                                .requiredHeight(height = 46.dp)
                                .clip(shape = RoundedCornerShape(25.dp))
                                .background(color = Color(0xffd3fde2))
                                .clickable {
                                    // Manejar la acción de cancelar aquí
                                }
                        ) {
                            Text(
                                text = "Cancelar",
                                color = Color(0xff000003),
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontSize = 18.sp),
                                modifier = Modifier
                                    .fillMaxSize() // Hace que el texto ocupe todo el tamaño del botón
                                    .wrapContentHeight(align = Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }

            }

    }
}



@Preview(widthDp = 430, heightDp = 932)
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}