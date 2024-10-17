package com.example.infoscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
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
import com.example.apptentzo_android.R

@Composable
fun InfoDetails(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 430.dp)
            .requiredHeight(height = 932.dp)
            .clip(shape = RoundedCornerShape(30.dp))
            .background(color = Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.actividad),
            contentDescription = "actividad",
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
                modifier = Modifier.fillMaxSize()
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
                text = "Arte y Cultura",
                color = Color(0xff7fc297),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically))
            Text(
                text = "Cenas con Arte",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically))
        }
        Text(
            text = "Detalles",
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
                text = "Para los amantes del arte es maravilloso poder conocer las obras del artista y poder ir descubriendo poco a poco sus estilos favoritos. Estamos  acostumbrados a hacer esto en las galerías de arte o en los museos pero ¿te imaginas tener el tiempo para disfrutar de sus obras con calma, entre amigos y con una deliciosa cena. ",
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
            text = "Próxima Fecha:",
            color = Color.Black,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold),
            modifier = modifier
                .offset(x = 30.dp, y = 739.dp)
                .requiredWidth(width = 308.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))

        Text(
            text = "24 de agosto 2024",
            color = Color.Black,
            style = TextStyle(
                fontSize = 15.sp),
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 30.dp, y = 779.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
        Text(
            text = "Costo:",
            color = Color.Black,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold),
            modifier = modifier
                .offset(x = 230.dp, y = 739.dp)
                .requiredWidth(width = 308.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))

        Text(
            text = "$200",
            color = Color.Black,
            style = TextStyle(
                fontSize = 15.sp),
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 230.dp, y = 779.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
    }
}

@Preview(widthDp = 430, heightDp = 932)
@Composable
private fun InfoDetailsPreview() {
    InfoDetails()
}