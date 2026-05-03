package com.example.sicenetmultiplatform.data.di

import com.example.sicenetmultiplatform.data.local.db.getDatabaseBuilder
import com.example.sicenetmultiplatform.data.repository.LocalRepository
import com.example.sicenetmultiplatform.data.repository.NetworkRepository

object AppContainer {

    private val database by lazy {
        getDatabaseBuilder()
    }

    val localRepository by lazy {
        LocalRepository(
            perfilDao = database.perfilDao(),
            cargaAcademicaDao = database.cargaAcademicaDao(),
            cardexDao = database.cardexDao(),
            calificacionDao = database.calificacionDao()
        )
    }

    val networkRepository by lazy {
        NetworkRepository()
    }
}