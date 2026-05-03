package com.example.sicenetmultiplatform.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sicenetmultiplatform.data.local.entity.CalificacionFinalEntity
import com.example.sicenetmultiplatform.data.local.entity.CalificacionUnidadEntity

/**
 * Componente que muestra la calificación final y por unidad de una materia.
 * Incluye botón para expandir/colapsar unidades cuando hay más de 5.
 * Basado en CalificacionCard.kt del proyecto Android original.
 *
 * @author Erick Omar Pérez González
 */
@Composable
fun CalificacionCard(
    materiaFinal: CalificacionFinalEntity,
    unidades: List<CalificacionUnidadEntity>
) {
    val calFinal = materiaFinal.calificacionFinal
    var expandido by remember { mutableStateOf(false) }

    val (scoreColor, scoreBg) = when {
        calFinal >= 80 -> Pair(Color(0xFF1B5E20), Color(0xFFE8F5E9))
        calFinal >= 70 -> Pair(Color(0xFF2E7D32), Color(0xFFF1F8F1))
        calFinal >  0  -> Pair(Color(0xFFC62828), Color(0xFFFFEBEE))
        else           -> Pair(Color.Gray,         Color(0xFFF5F5F5))
    }

    val unidadesValidas = unidades.filter {
        !(unidades.size == 1 && it.calificacion == 0)
    }

    val tienemasDecinco = unidadesValidas.size > 5

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = materiaFinal.materia,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color(0xFF1A1A1A),
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.width(12.dp))

                // Círculo con calificación final
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(scoreBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (calFinal == 0) "—" else "$calFinal",
                        fontWeight = FontWeight.ExtraBold,
                        color = scoreColor,
                        fontSize = 16.sp
                    )
                }
            }

            // Sección de unidades
            if (unidadesValidas.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(10.dp))

                // Primera fila: primeras 5 unidades + botón
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Unidades:",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // Primeras 5 unidades siempre visibles
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        unidadesValidas.take(5).forEach { unidad ->
                            UnidadBadge(unidad)
                        }
                    }

                    // Botón expandir si hay más de 5
                    if (tienemasDecinco) {
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = { expandido = !expandido },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (expandido)
                                    Icons.Default.KeyboardArrowUp
                                else
                                    Icons.Default.KeyboardArrowDown,
                                contentDescription = if (expandido) "Colapsar" else "Ver más",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Unidades adicionales (expandibles)
                if (tienemasDecinco) {
                    AnimatedVisibility(visible = expandido) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 68.dp)
                            ) {
                                unidadesValidas.drop(5).forEach { unidad ->
                                    UnidadBadge(unidad)
                                }
                            }
                        }
                    }
                }

            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "⏳ Calificaciones de unidades pendientes",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// Badge reutilizable para cada unidad
@Composable
private fun UnidadBadge(unidad: CalificacionUnidadEntity) {
    val uColor = if (unidad.calificacion >= 70)
        Color(0xFF2E7D32) else Color(0xFFC62828)
    val uBg = if (unidad.calificacion >= 70)
        Color(0xFFE8F5E9) else Color(0xFFFFEBEE)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(uBg)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "U${unidad.unidad}: ${unidad.calificacion}",
            fontSize = 11.sp,
            color = uColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}