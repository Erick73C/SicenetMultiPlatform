package com.example.sicenetmultiplatform.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sicenetmultiplatform.data.local.dao.CalificacionDao
import com.example.sicenetmultiplatform.data.local.dao.CardexDao
import com.example.sicenetmultiplatform.data.local.dao.CargaAcademicaDao
import com.example.sicenetmultiplatform.data.local.dao.PerfilDao
import com.example.sicenetmultiplatform.data.local.entity.CalificacionFinalEntity
import com.example.sicenetmultiplatform.data.local.entity.CalificacionUnidadEntity
import com.example.sicenetmultiplatform.data.local.entity.CardexEntity
import com.example.sicenetmultiplatform.data.local.entity.CargaAcademicaEntity
import com.example.sicenetmultiplatform.data.local.entity.PerfilEntity

@Database(
    entities = [
        PerfilEntity::class,
        CargaAcademicaEntity::class,
        CardexEntity::class,
        CalificacionFinalEntity::class,
        CalificacionUnidadEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SicenetDatabase : RoomDatabase() {
    abstract fun perfilDao(): PerfilDao
    abstract fun cargaAcademicaDao(): CargaAcademicaDao
    abstract fun cardexDao(): CardexDao
    abstract fun calificacionDao(): CalificacionDao
}