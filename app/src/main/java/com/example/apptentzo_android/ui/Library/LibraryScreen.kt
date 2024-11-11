package com.example.biblioteca

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.apptentzo_android.R
import com.example.apptentzo_android.ui.model.Planta
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantBank(navController: NavHostController) {
    var text by remember { mutableStateOf("") }
    var plantasList by remember { mutableStateOf<List<Planta>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Obtener datos desde Firebase
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val db = FirebaseFirestore.getInstance()
            val plantaSnapshots = db.collection("Planta").get().await()
            val allPlantas = plantaSnapshots.documents.mapNotNull { doc ->
                val id = doc.id
                val nomComun = doc.getString("nomComun") ?: ""
                val nomCientifico = doc.getString("nomCientifico") ?: ""
                val sinonimo = doc.getString("sinonimo") ?: ""
                val descripcion = doc.getString("descripcion") ?: ""
                val imagen = doc.getString("imagen") ?: ""
                if (imagen.isNotEmpty()) {
                    Planta(
                        id = id,
                        nomComun = nomComun,
                        nomCientifico = nomCientifico,
                        sinonimo = sinonimo,
                        descripcion = descripcion,
                        imagen = imagen
                    )
                } else {
                    null
                }
            }
            // Mostrar todas las plantas
            plantasList = allPlantas
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isLoading = false
    }

    // Aplicar filtro de búsqueda
    val filteredPlantas = if (text.isEmpty()) {
        plantasList
    } else {
        plantasList.filter { planta ->
            val nombreParaBusqueda = if (planta.nomComun.isNotEmpty()) planta.nomComun else planta.nomCientifico
            nombreParaBusqueda.contains(text, ignoreCase = true) ||
                    planta.nomCientifico.contains(text, ignoreCase = true) ||
                    planta.sinonimo.contains(text, ignoreCase = true)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        // Título y barra de búsqueda
        Image(
            painter = painterResource(id = R.drawable.hoja),
            contentDescription = "hoja",
            modifier = Modifier
                .size(40.dp)
                .offset(x = 34.dp, y = 41.dp)
        )
        Text(
            text = "Biblioteca",
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 88.dp, y = 41.dp)
        )
        // Barra de búsqueda
        Box(
            modifier = Modifier
                .offset(x = 10.dp, y = 113.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(54.dp)
                .clip(RoundedCornerShape(9999.dp))
                .border(
                    border = BorderStroke(1.dp, Color(0xffd9d9d9)),
                    shape = RoundedCornerShape(9999.dp)
                )
        ) {
            TextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically),
                placeholder = {
                    Text(text = "Buscar...", color = Color.Gray)
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        // Mostrar indicador de carga
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Mostrar lista de plantas
            val rows = filteredPlantas.chunked(3) // Agrupar en filas de 3
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 200.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(rows) { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp), // Espacio vertical entre filas
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { planta ->
                            PlantItem(planta, navController)
                        }
                        // Añadir espacios si la fila tiene menos de 3 elementos
                        if (row.size < 3) {
                            for (i in 1..(3 - row.size)) {
                                Spacer(modifier = Modifier.width(109.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlantItem(planta: Planta, navController: NavHostController) {
    val nombreMostrar = when {
        planta.nomComun.isNotEmpty() -> planta.nomComun
        planta.nomCientifico.isNotEmpty() -> planta.nomCientifico
        else -> planta.sinonimo
    }

    Column(
        modifier = Modifier
            .requiredWidth(109.dp)
            .wrapContentHeight()
            .clickable {
                navController.navigate("plant_details_screen/${planta.id}")
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .requiredWidth(109.dp)
                .requiredHeight(106.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(color = Color(0xff7fc297))
        ) {
            AsyncImage(
                model = planta.imagen,
                contentDescription = nombreMostrar,
                modifier = Modifier
                    .requiredWidth(92.dp)
                    .requiredHeight(93.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = nombreMostrar,
            color = Color(0xff121d29),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .requiredWidth(109.dp)
                .padding(top = 4.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(widthDp = 430, heightDp = 932)
@Composable
private fun PlantBankPreview() {
    val navController = rememberNavController()
    PlantBank(navController = navController)
}
