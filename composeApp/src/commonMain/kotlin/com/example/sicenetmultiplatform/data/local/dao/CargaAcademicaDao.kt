package com.example.sicenetmultiplatform.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sicenetmultiplatform.data.local.entity.CargaAcademicaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CargaAcademicaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodo(lista: List<CargaAcademicaEntity>)

    @Query("DELETE FROM carga_academica WHERE matricula = :matricula")
    suspend fun limpiar(matricula: String)

    @Query("SELECT * FROM carga_academica WHERE matricula = :matricula")
    fun obtenerCargaAcademica(matricula: String): Flow<List<CargaAcademicaEntity>>

    @Query("SELECT MAX(ultimaActualizacion) FROM carga_academica WHERE matricula = :matricula")
    fun obtenerUltimaActualizacion(matricula: String): Flow<Long?>
}