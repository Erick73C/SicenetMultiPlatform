package com.example.sicenetmultiplatform.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sicenetmultiplatform.data.local.entity.PerfilEntity
import com.example.sicenetmultiplatform.data.repository.LocalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel del perfil del alumno.
 * Reemplaza PerfilViewModel.kt del proyecto Android original.
 * Se elimina WorkManager, el perfil se sincroniza desde LoginViewModel.
 *
 * @author Erick Omar Pérez González
 */
class PerfilViewModel(
    private val localRepository: LocalRepository,
    matricula: String
) : ViewModel() {

    /**
     * Perfil del alumno observado desde la base de datos local.
     * Se actualiza automáticamente cuando cambian los datos en Room.
     */
    val perfil: StateFlow<PerfilEntity?> =
        localRepository.obtenerPerfil(matricula)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
}