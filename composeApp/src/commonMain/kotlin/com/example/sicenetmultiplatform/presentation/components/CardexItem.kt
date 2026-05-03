package com.example.sicenetmultiplatform.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sicenetmultiplatform.data.local.entity.CardexEntity

private val GreenPrimary = Color(0xFF2E7D32)

/**
 * Componente que muestra la información de una materia del kardex.
 * Basado en CardexItem.kt del proyecto Android original.
 *
 * @author
 */
@Composable
fun CardexItem(materia: CardexEntity) {

    val acreditado = materia.calificacion >= 70
    val enCurso    = materia.calificacion == 0

    val calColor = when {
        materia.calificacion >= 80 -> Color(0xFF2E7D32)
        materia.calificacion >= 70 -> Color(0xFF388E3C)
        materia.calificacion >  0  -> Color(0xFFC62828)
        else                       -> Color.Gray
    }

    val cardBackground = if (acreditado) Color(0xFFF9FFF9) else Color(0xFFFFF9F9)

    val statusColor = when {
        enCurso    -> Color.Gray
        acreditado -> GreenPrimary
        else       -> Color(0xFFC62828)
    }

    val statusLabel = when {
        enCurso    -> "En curso"
        acreditado -> "✓ Acreditada"
        else       -> "✗ No acreditada"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Círculo con calificación
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(calColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (materia.calificacion > 0) "${materia.calificacion}" else "—",
                    fontWeight = FontWeight.ExtraBold,
                    color = calColor,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Datos de la materia
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = materia.materia,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color(0xFF1A1A1A),
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Badge de estatus
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.10f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = statusLabel,
                        fontSize = 11.sp,
                        color = statusColor,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Periodo: ${materia.periodo}",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}