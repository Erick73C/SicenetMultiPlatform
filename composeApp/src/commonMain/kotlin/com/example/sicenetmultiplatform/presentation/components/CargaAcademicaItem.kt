package com.example.sicenetmultiplatform.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sicenetmultiplatform.data.local.entity.CargaAcademicaEntity

private val GreenPrimary = Color(0xFF2E7D32)
private val GreenLight   = Color(0xFF4CAF50)

/**
 * Componente que muestra la información de una materia
 * de la carga académica del alumno.
 * Basado en CargaAcademicaItem.kt del proyecto Android original.
 *
 * @author
 */
@Composable
fun CargaAcademicaItem(materia: CargaAcademicaEntity) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            //Barra verde lateral
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .background(GreenPrimary)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp, top = 14.dp, bottom = 14.dp)
            ) {

                //Nombre materia + badge créditos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = materia.nombreMateria,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1B2A1B),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // Badge de créditos
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = GreenLight.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = GreenPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${materia.creditos} cr.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = GreenPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Clave materia
                Text(
                    text = "Clave: ${materia.claveMateria}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFFE8F5E9), thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))

                // Info rows
                CargaInfoRow(Icons.Default.Group,    "Grupo",   materia.grupo)
                CargaInfoRow(Icons.Default.Person,   "Docente", materia.docente)
                CargaInfoRow(Icons.Default.Schedule, "Horario", materia.horario)
            }
        }
    }
}

@Composable
private fun CargaInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GreenLight,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label: ",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF555555)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = Color(0xFF333333)
        )
    }
}