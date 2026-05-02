package com.example.sicenetmultiplatform.data.repository

import com.example.sicenetmultiplatform.data.local.dao.CalificacionDao
import com.example.sicenetmultiplatform.data.local.dao.CardexDao
import com.example.sicenetmultiplatform.data.local.dao.CargaAcademicaDao
import com.example.sicenetmultiplatform.data.local.dao.PerfilDao
import com.example.sicenetmultiplatform.data.local.entity.CalificacionFinalEntity
import com.example.sicenetmultiplatform.data.local.entity.CalificacionUnidadEntity
import com.example.sicenetmultiplatform.data.local.entity.CardexEntity
import com.example.sicenetmultiplatform.data.local.entity.CargaAcademicaEntity
import com.example.sicenetmultiplatform.data.local.entity.PerfilEntity
import kotlinx.coroutines.flow.Flow

class LocalRepository(
    private val perfilDao: PerfilDao,
    private val cargaAcademicaDao: CargaAcademicaDao,
    private val cardexDao: CardexDao,
    private val calificacionDao: CalificacionDao
) {

    // Perfil
    suspend fun guardarPerfil(perfil: PerfilEntity) {
        perfilDao.insertarPerfil(perfil)
    }

    fun obtenerPerfil(matricula: String): Flow<PerfilEntity?> {
        return perfilDao.obtenerPerfil(matricula)
    }

    suspend fun limpiarPerfil() {
        perfilDao.limpiar()
    }

    // Carga Académica
    suspend fun guardarCargaAcademica(
        matricula: String,
        carga: List<CargaAcademicaEntity>
    ) {
        cargaAcademicaDao.limpiar(matricula)
        cargaAcademicaDao.insertarTodo(carga)
    }

    fun obtenerCargaAcademica(matricula: String): Flow<List<CargaAcademicaEntity>> {
        return cargaAcademicaDao.obtenerCargaAcademica(matricula)
    }

    fun obtenerUltimaActualizacionCarga(matricula: String): Flow<Long?> {
        return cargaAcademicaDao.obtenerUltimaActualizacion(matricula)
    }

    // Cardex
    suspend fun guardarCardex(cardex: List<CardexEntity>) {
        cardexDao.limpiar()
        cardexDao.insertarTodo(cardex)
    }

    fun obtenerCardex(): Flow<List<CardexEntity>> {
        return cardexDao.obtenerCardex()
    }

    fun obtenerUltimaActualizacionCardex(): Flow<Long?> {
        return cardexDao.obtenerUltimaActualizacion()
    }

    // Calificaciones Finales
    suspend fun guardarCalificacionesFinales(
        calificaciones: List<CalificacionFinalEntity>
    ) {
        calificacionDao.limpiarFinales()
        calificacionDao.insertarFinales(calificaciones)
    }

    fun obtenerCalificacionesFinales(): Flow<List<CalificacionFinalEntity>> {
        return calificacionDao.obtenerFinales()
    }

    // Calificaciones Unidad
    suspend fun guardarCalificacionesUnidad(
        calificaciones: List<CalificacionUnidadEntity>
    ) {
        calificacionDao.limpiarUnidades()
        calificacionDao.insertarUnidades(calificaciones)
    }

    fun obtenerCalificacionesUnidad(): Flow<List<CalificacionUnidadEntity>> {
        return calificacionDao.obtenerUnidades()
    }
}