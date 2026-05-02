package com.example.sicenetmultiplatform.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sicenetmultiplatform.data.local.entity.CardexEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardexDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodo(lista: List<CardexEntity>)

    @Query("SELECT * FROM cardex")
    fun obtenerCardex(): Flow<List<CardexEntity>>

    @Query("DELETE FROM cardex")
    suspend fun limpiar()

    @Query("SELECT MAX(ultimaActualizacion) FROM cardex")
    fun obtenerUltimaActualizacion(): Flow<Long?>
}