package com.example.sicenetmultiplatform.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sicenetmultiplatform.presentation.components.CardexItem
import com.example.sicenetmultiplatform.presentation.viewmodel.CardexViewModel
import java.text.SimpleDateFormat
import java.util.*

private val GreenPrimary = Color(0xFF2E7D32)
private val GreenLight   = Color(0xFF4CAF50)
private val GreenDark    = Color(0xFF1B5E20)

/**
 * Pantalla del kardex académico del alumno.
 * Basada en CardexScreen.kt del proyecto Android original.
 *
 * @author
 */
@Composable
fun CardexScreen(viewModel: CardexViewModel) {

    LaunchedEffect(Unit) {
        viewModel.verificarYSincronizar()
    }

    val cardex              by viewModel.cardex.collectAsState()
    val ultimaActualizacion by viewModel.ultimaActualizacion.collectAsState()
    val isLoading           by viewModel.isLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8F1))
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            // Header con gradiente
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(GreenDark, GreenPrimary, GreenLight)
                            ),
                            shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                        )
                        .padding(top = 36.dp, bottom = 28.dp, start = 24.dp, end = 24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "📋 Kárdex Académico",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        ultimaActualizacion?.let {
                            val fmt = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            Text(
                                text = "Actualizado: ${fmt.format(Date(it))}",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.80f),
                                textAlign = TextAlign.Center
                            )
                        }

                        // Chips resumen
                        if (cardex.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(20.dp))
                            val aprobadas  = cardex.count { it.calificacion >= 70 }
                            val reprobadas = cardex.size - aprobadas
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                CardexStatChip("Total",        "${cardex.size}", Color.White)
                                CardexStatChip("Aprobadas",    "$aprobadas",     Color(0xFFA5D6A7))
                                CardexStatChip("No aprobadas", "$reprobadas",    Color(0xFFEF9A9A))
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Estado de carga o lista
            if (cardex.isEmpty() || isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(top = 80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = GreenPrimary,
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Cargando kárdex...",
                                color = GreenPrimary,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            } else {
                items(cardex) { materia ->
                    CardexItem(materia)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

// Chip de estadística en el header
@Composable
private fun CardexStatChip(label: String, value: String, chipColor: Color) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = chipColor.copy(alpha = 0.25f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)
            Text(label, color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
        }
    }
}