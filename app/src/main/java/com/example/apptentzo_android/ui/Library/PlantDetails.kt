package com.example.apptentzo_android

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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


@Composable
fun PlantInfo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 430.dp)
            .requiredHeight(height = 932.dp)
            .clip(shape = RoundedCornerShape(30.dp))
            .background(color = Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.image1),
            contentDescription = "image 1",
            modifier = modifier
                .requiredWidth(width = 430.dp)
                .requiredHeight(height = 437.dp))
        IconButton(
            onClick = { },
            modifier = modifier
                .requiredWidth(60.dp)
                .requiredHeight(60.dp)
                .offset(x = 17.dp, y = 33.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "icon",
                modifier = Modifier.fillMaxSize() // Asegura que la imagen ocupe todo el botón
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
            modifier = modifier
                .offset(
                    x = 30.dp,
                    y = 470.dp)
        ) {
            Text(
                text = "Epipremnum aureum",
                color = Color(0xff7fc297),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically))
            Text(
                text = "Planta Potos",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically))
        }
        Text(
            text = "Datos curiosos",
            color = Color.Black,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold),
            modifier = modifier
                .offset(x = 30.dp, y = 535.dp)
                .requiredWidth(width = 308.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
            modifier = modifier
                .requiredWidth(width = 367.dp)
                .offset(x = 30.dp, y = 570.dp)
        ) {
            Text(
                text = "Es una liana que puede alcanzar 20 m de alto, con tallos de hasta 4 cm de diámetro. Trepa mediante raíces aéreas que se enganchan a las ramas de los árboles. Las hojas son perennes, alternas y acorazonadas, enteras en las plantas jóvenes, pero irregularmente pinnadas en las maduras y de hasta 1 m de largo por 45 cm de ancho (en las plantas jóvenes no superan los 20 cm de largo).",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 15.sp,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
        Text(
            text = "Fuente consultada",
            color = Color.Black,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold),
            modifier = modifier
                .offset(x = 30.dp, y = 739.dp)
                .requiredWidth(width = 308.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
        Text(
            text = "https://www.inaturalist.org/",
            color = Color.Black,
            style = TextStyle(
                fontSize = 15.sp),
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 30.dp, y = 779.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))

    }
}


@Preview(widthDp = 430, heightDp = 932)
@Composable
private fun PlantInfoPreview() {
    PlantInfo(Modifier)
}