package com.example.sicenetmultiplatform.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sicenetmultiplatform.presentation.viewmodel.PerfilViewModel

// ── Paleta de colores ──────────────────────────────────────────────
private val GreenPrimary = Color(0xFF2E7D32)
private val GreenLight   = Color(0xFF4CAF50)
private val GreenDark    = Color(0xFF1B5E20)

/**
 * Pantalla del perfil académico del alumno.
 * Basada en PerfilScreen.kt del proyecto Android original.
 *
 * @author Erick Omar Pérez González
 */
@Composable
fun PerfilScreen(viewModel: PerfilViewModel) {

    val perfil by viewModel.perfil.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GreenDark, GreenPrimary, GreenLight)
                )
            )
    ) {
        if (perfil == null) {
            // ── Estado de carga ────────────────────────────────────
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Cargando perfil...",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            val p = perfil!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Título
                Text(
                    text = "Mi Perfil",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 36.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Tarjeta principal 
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.95f)
                    )
                ) {
                    Column(modifier = Modifier.padding(32.dp)) {

                        PerfilItem(
                            label = "Nombre",
                            valor = p.nombre,
                            valorSize = 24,
                            valorColor = GreenPrimary,
                            valorWeight = FontWeight.Bold
                        )

                        PerfilDivider()

                        PerfilItem(
                            label = "Matrícula",
                            valor = p.matricula
                        )

                        PerfilDivider()

                        PerfilItem(
                            label = "Carrera",
                            valor = p.carrera,
                            valorSize = 18
                        )

                        PerfilDivider()

                        PerfilItem(
                            label = "Semestre Actual",
                            valor = p.semActual.toString()
                        )

                        PerfilDivider()

                        PerfilItem(
                            label = "Especialidad",
                            valor = p.especialidad,
                            valorSize = 18
                        )

                        PerfilDivider()

                        PerfilItem(
                            label = "Créditos Acumulados",
                            valor = p.cdtosAcumulados.toString()
                        )

                        PerfilDivider()

                        PerfilItem(
                            label = "Créditos Actuales",
                            valor = p.cdtosActuales.toString()
                        )

                        PerfilDivider()

                        PerfilItem(
                            label = "Estatus",
                            valor = p.estatus
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// ── Componente reutilizable para cada campo del perfil ─────────────
@Composable
private fun PerfilItem(
    label: String,
    valor: String,
    valorSize: Int = 20,
    valorColor: Color = Color.Black,
    valorWeight: FontWeight = FontWeight.SemiBold
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            color = GreenPrimary.copy(alpha = 0.7f),
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = valor,
            fontSize = valorSize.sp,
            color = valorColor,
            fontWeight = valorWeight,
            lineHeight = (valorSize + 6).sp
        )
    }
}

//Divisor entre campos
@Composable
private fun PerfilDivider() {
    Spacer(modifier = Modifier.height(24.dp))
    HorizontalDivider(
        color = GreenLight.copy(alpha = 0.3f),
        thickness = 1.dp
    )
    Spacer(modifier = Modifier.height(24.dp))
}