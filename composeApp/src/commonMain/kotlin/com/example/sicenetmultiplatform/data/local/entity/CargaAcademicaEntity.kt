package com.example.sicenetmultiplatform.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carga_academica")
data class CargaAcademicaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val matricula: String,
    val claveMateria: String,
    val nombreMateria: String,
    val grupo: String,
    val docente: String,
    val creditos: Int,
    val horario: String,
    val semestre: Int,
    val ultimaActualizacion: Long
)
