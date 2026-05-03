package com.example.sicenetmultiplatform.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sicenetmultiplatform.data.local.entity.CardexEntity
import com.example.sicenetmultiplatform.data.repository.LocalRepository
import com.example.sicenetmultiplatform.data.repository.NetworkRepository
import com.example.sicenetmultiplatform.utils.getCurrentTimeMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel del kardex académico.
 * Reemplaza CardexViewModel.kt del proyecto Android original.
 * Se elimina WorkManager, la sincronización se hace con coroutines.
 *
 * @author Erick Omar Pérez González
 */
class CardexViewModel(
    private val localRepository: LocalRepository,
    private val networkRepository: NetworkRepository
) : ViewModel() {

    // Kardex desde Room
    val cardex: StateFlow<List<CardexEntity>> =
        localRepository.obtenerCardex()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val ultimaActualizacion: StateFlow<Long?> =
        localRepository.obtenerUltimaActualizacionCardex()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Verifica si es necesario sincronizar y lo hace si:
     * - No hay datos guardados
     * - Nunca se sincronizó
     * - Pasó más de 1 hora desde la última sincronización
     */
    fun verificarYSincronizar() {
        viewModelScope.launch {
            val datos = cardex.first()
            val ultima = ultimaActualizacion.first()
            val ahora = getCurrentTimeMillis()

            val necesitaSincronizar =
                datos.isEmpty() ||
                        ultima == null ||
                        (ahora - ultima) > 1000 * 60 * 60 // 1 hora

            if (necesitaSincronizar) sincronizar()
        }
    }

    /**
     * Sincroniza el kardex desde la red y lo guarda localmente.
     */
    private fun sincronizar() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val lista = networkRepository.obtenerCardex()

                val entities = lista.map {
                    CardexEntity(
                        materia     = it.materia,
                        calificacion = it.calificacion,
                        acreditado  = it.acreditado,
                        periodo     = it.periodo,
                        ultimaActualizacion = it.ultimaActualizacion
                    )
                }

                localRepository.guardarCardex(entities)
                println("[CARDEX_VM] Kardex sincronizado (${entities.size})")
            } catch (e: Exception) {
                println("[CARDEX_VM] Error sincronizando: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}