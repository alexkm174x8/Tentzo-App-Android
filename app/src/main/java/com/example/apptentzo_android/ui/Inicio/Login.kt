package com.example.apptentzo_android.ui.Login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import com.example.apptentzo_android.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.runtime.*




@Composable
fun Login( modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf<String>("") }
    var text2 by remember { mutableStateOf<String>("") }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(30.dp))
            .background(color = Color.White)
    ) {
        Text(
            text = "Vive el Tentzo",
            color = Color(0xff7fc297),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 128.dp, y = 84.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 127.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Iniciar Sesión",
                color = Color(0xff000201),
                style = TextStyle(
                    fontSize = 55.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xff000201),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Light
                        )
                    ) {
                        append("¿No tienes cuenta? ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xff7fc297),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("Regístrate")
                    }
                },
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .offset(y = 20.dp)
                    .clickable {
                    }
            )

        }

        Text(
            text = "Acceso rápido con:",
            color = Color(0xff000201),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Light
            ),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 95.dp, y = 751.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
        Text(
            text = "Correo",
            color = Color.Black.copy(alpha = 0.89f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 176.dp, y = 310.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .offset(y = -90.dp)
                .requiredWidth(width = 336.dp)
                .requiredHeight(height = 40.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color.White)
                .border(
                    border = BorderStroke(1.dp, Color(0xff757575)),
                    shape = RoundedCornerShape(8.dp)
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
                    Text(text = "Correo", color = Color.Gray)
                },
                singleLine = true
            )
        }
        Text(
            text = "Contraseña",
            color = Color.Black.copy(alpha = 0.89f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 145.dp, y = 428.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .offset(y = 30.dp)
                .requiredWidth(width = 336.dp)
                .requiredHeight(height = 40.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color.White)
                .border(
                    border = BorderStroke(1.dp, Color(0xff757575)),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            TextField(
                value = text2,
                onValueChange = { newText ->
                    text2 = newText
                },
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically),
                placeholder = {
                    Text(text = "Contraseña", color = Color.Gray)
                },
                singleLine = true
            )
        }

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = Color(0xff247d3d).copy(alpha = 0.89f),
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 92.dp, y = 540.dp)
                .clickable {
                }
        )


        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 23.dp, y = 275.dp)
                .requiredWidth(width = 384.dp)
                .requiredHeight(height = 448.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .border(
                    border = BorderStroke(1.dp, Color(0xff2b3707)),
                    shape = RoundedCornerShape(30.dp)
                )
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .offset(y = 180.dp)
                .requiredWidth(width = 289.dp)
                .requiredHeight(height = 48.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                }
                .padding(all = 12.dp)
        ) {
            Text(
                text = "Iniciar Sesión",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .requiredWidth(width = 267.dp)
                    .requiredHeight(height = 35.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }

        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 161.dp, y = 808.dp)
                .requiredWidth(width = 88.dp)
                .requiredHeight(height = 89.dp)
                .clickable {

                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "google",
                modifier = Modifier.fillMaxSize(),

                )
        }
    }
}

@Preview(widthDp = 430, heightDp = 932)
@Composable
private fun LoginPreview() {
    Login(Modifier)
}