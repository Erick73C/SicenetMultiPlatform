package com.example.sicenetmultiplatform.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cardex")
data class CardexEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val materia: String,
    val calificacion: Int,
    val acreditado: String,
    val periodo: String,
    val ultimaActualizacion: Long
)