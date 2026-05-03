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
import com.example.sicenetmultiplatform.presentation.components.CalificacionCard
import com.example.sicenetmultiplatform.presentation.viewmodel.CalificacionesViewModel

private val GreenPrimary = Color(0xFF2E7D32)
private val GreenLight   = Color(0xFF4CAF50)
private val GreenDark    = Color(0xFF1B5E20)
private val GreenSurface = Color(0xFFF1F8F1)

/**
 * Pantalla de calificaciones finales y por unidad del alumno.
 * Basada en CalificacionesScreen.kt del proyecto Android original.
 *
 * @author
 */
@Composable
fun CalificacionesScreen(viewModel: CalificacionesViewModel) {

    LaunchedEffect(Unit) {
        viewModel.verificarYSincronizar()
    }

    val finales   by viewModel.calificacionesFinales.collectAsState()
    val unidades  by viewModel.calificacionesUnidad.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenSurface)
    ) {
        if (finales.isEmpty() || isLoading) {
            // Estado de carga
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = GreenPrimary,
                    modifier = Modifier.size(56.dp),
                    strokeWidth = 4.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Cargando calificaciones...",
                    color = GreenPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        } else {
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
                                shape = RoundedCornerShape(
                                    bottomStart = 28.dp,
                                    bottomEnd   = 28.dp
                                )
                            )
                            .padding(
                                top    = 36.dp,
                                bottom = 28.dp,
                                start  = 24.dp,
                                end    = 24.dp
                            )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🎓 Reporte de Calificaciones",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "${finales.size} materias registradas",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.80f)
                            )

                            // ── Chips de resumen ───────────────────
                            Spacer(modifier = Modifier.height(20.dp))
                            val aprobadas  = finales.count { it.calificacionFinal >= 70 }
                            val reprobadas = finales.count { it.calificacionFinal in 1..69 }
                            val pendientes = finales.count { it.calificacionFinal == 0 }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                CalifHeaderChip("✅ Aprob.",  "$aprobadas")
                                CalifHeaderChip("❌ Repro.",  "$reprobadas")
                                CalifHeaderChip("⏳ Pend.",   "$pendientes")
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                // Lista de materias
                items(finales) { materiaFinal ->
                    val unidadesMateria = unidades.filter {
                        it.materia.equals(materiaFinal.materia, ignoreCase = true)
                    }
                    CalificacionCard(
                        materiaFinal = materiaFinal,
                        unidades     = unidadesMateria
                    )
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

// Chip de resumen en el header
@Composable
private fun CalifHeaderChip(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 22.sp
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.85f)
        )
    }
}