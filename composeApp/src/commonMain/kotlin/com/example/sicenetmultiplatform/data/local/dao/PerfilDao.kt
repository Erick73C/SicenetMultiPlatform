package com.example.sicenetmultiplatform.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sicenetmultiplatform.data.local.entity.PerfilEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PerfilDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPerfil(perfil: PerfilEntity)

    @Query("SELECT * FROM perfil WHERE matricula = :matricula")
    fun obtenerPerfil(matricula: String): Flow<PerfilEntity?>

    @Query("DELETE FROM perfil")
    suspend fun limpiar()
}