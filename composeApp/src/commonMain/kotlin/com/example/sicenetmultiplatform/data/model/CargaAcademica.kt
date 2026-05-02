package com.example.sicenetmultiplatform.data.model

data class CargaAcademica(
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
