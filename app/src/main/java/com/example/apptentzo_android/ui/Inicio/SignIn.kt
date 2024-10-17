package com.example.apptentzo_android.ui.Inicio

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptentzo_android.R
import androidx.compose.material3.TextField
import androidx.compose.runtime.*

@Composable
fun SignIn( modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf<String>("") }
    var text2 by remember { mutableStateOf<String>("") }
    var text3 by remember { mutableStateOf<String>("") }
    var text4 by remember { mutableStateOf<String>("") }
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
                fontWeight = FontWeight.Bold),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 118.dp,
                    y = 87.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
        Text(
            text = "Registrarse",
            color = Color(0xff000201),
            style = TextStyle(
                fontSize = 55.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 64.dp,
                    y = 116.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
        Text(
            text = "Acceso rápido con:",
            color = Color(0xff000201),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Light),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 95.dp,
                    y = 759.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))

        Text(
            text = "Nombre Completo",
            color = Color.Black.copy(alpha = 0.89f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 111.dp,
                    y = 236.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .offset(y = -165.dp)
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
            text = "Email",
            color = Color.Black.copy(alpha = 0.89f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 185.dp,
                    y = 341.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .offset(y = -60.dp)
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
                .offset(x = 145.dp,
                    y = 442.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .offset(y = 35.dp)
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
                value = text3,
                onValueChange = { newText ->
                    text3 = newText
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
            text = "Repite tu Contraseña",
            color = Color.Black.copy(alpha = 0.89f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 91.dp,
                    y = 540.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .offset(y = 140.dp)
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
                value = text4,
                onValueChange = { newText ->
                    text4 = newText
                },
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically),
                placeholder = {
                    Text(text = "Correo", color = Color.Gray)
                },
                singleLine = true
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .offset(y = 220.dp)
                .requiredWidth(width = 289.dp)
                .requiredHeight(height = 48.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color(0xff7fc297))
                .clickable {
                }
                .padding(all = 12.dp)
        ) {
            Text(
                text = "Registrarse",
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
                .offset(x = 23.dp,
                    y = 212.dp)
                .requiredWidth(width = 384.dp)
                .requiredHeight(height = 528.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .border(border = BorderStroke(1.dp, Color(0xff2b3707)),
                    shape = RoundedCornerShape(30.dp)))
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
fun SignInPreview() {
    SignIn(Modifier)
}