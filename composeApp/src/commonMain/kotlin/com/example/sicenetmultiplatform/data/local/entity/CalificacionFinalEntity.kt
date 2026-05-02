package com.example.sicenetmultiplatform.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calificacion_final")
data class CalificacionFinalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val materia: String,
    val calificacionFinal: Int,
    val acreditado: String,
    val ultimaActualizacion: Long
)
