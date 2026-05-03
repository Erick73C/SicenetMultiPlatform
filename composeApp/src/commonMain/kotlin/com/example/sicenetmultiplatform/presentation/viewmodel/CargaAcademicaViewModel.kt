package com.example.sicenetmultiplatform.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sicenetmultiplatform.data.local.entity.CargaAcademicaEntity
import com.example.sicenetmultiplatform.data.mapper.CargaAcademicaXmlParser
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
 * ViewModel de carga académica.
 * Reemplaza CargaAcademicaViewModel.kt del proyecto Android original.
 * Se elimina WorkManager, la sincronización se hace con coroutines.
 *
 * @author Erick Omar Pérez González
 */
class CargaAcademicaViewModel(
    private val localRepository: LocalRepository,
    private val networkRepository: NetworkRepository,
    private val matricula: String
) : ViewModel() {

    // ── Carga académica desde Room ─────────────────────────────────
    val carga: StateFlow<List<CargaAcademicaEntity>> =
        localRepository.obtenerCargaAcademica(matricula)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val ultimaActualizacion: StateFlow<Long?> =
        localRepository.obtenerUltimaActualizacionCarga(matricula)
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
            val datos = carga.first()
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
     * Sincroniza la carga académica desde la red y la guarda localmente.
     */
    private fun sincronizar() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Necesitamos el semestre del perfil
                val perfil = localRepository.obtenerPerfil(matricula).first()
                val semestre = perfil?.semActual ?: 0

                val lista = networkRepository.obtenerCargaAcademica(matricula, semestre)

                // Convierte modelos a entities
                val entities = lista.map {
                    CargaAcademicaEntity(
                        matricula     = it.matricula,
                        claveMateria  = it.claveMateria,
                        nombreMateria = it.nombreMateria,
                        grupo         = it.grupo,
                        docente       = it.docente,
                        creditos      = it.creditos,
                        horario       = it.horario,
                        semestre      = it.semestre,
                        ultimaActualizacion = it.ultimaActualizacion
                    )
                }

                localRepository.guardarCargaAcademica(matricula, entities)
                println("[CARGA_VM] Carga académica sincronizada (${entities.size})")
            } catch (e: Exception) {
                println("[CARGA_VM] Error sincronizando: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}