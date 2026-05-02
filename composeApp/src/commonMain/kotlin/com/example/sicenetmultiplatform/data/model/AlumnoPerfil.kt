package com.example.sicenetmultiplatform.data.model

data class AlumnoPerfil(
    val matricula: String,
    val nombre: String,
    val carrera: String,
    val especialidad: String,
    val estatus: String,
    val semActual: Int,
    val cdtosAcumulados: Int,
    val cdtosActuales: Int,
    val fechaReins: String,
    val modEducativo: Int,
    val urlFoto: String,
    val inscrito: Boolean
)
