package com.example.sicenetmultiplatform.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sicenetmultiplatform.data.local.entity.CalificacionFinalEntity
import com.example.sicenetmultiplatform.data.local.entity.CalificacionUnidadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalificacionDao {

    //Finales
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarFinales(calificaciones: List<CalificacionFinalEntity>)

    @Query("SELECT * FROM calificacion_final")
    fun obtenerFinales(): Flow<List<CalificacionFinalEntity>>

    @Query("DELETE FROM calificacion_final")
    suspend fun limpiarFinales()

    // Unidade
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUnidades(calificaciones: List<CalificacionUnidadEntity>)

    @Query("SELECT * FROM calificacion_unidad")
    fun obtenerUnidades(): Flow<List<CalificacionUnidadEntity>>

    @Query("DELETE FROM calificacion_unidad")
    suspend fun limpiarUnidades()
}