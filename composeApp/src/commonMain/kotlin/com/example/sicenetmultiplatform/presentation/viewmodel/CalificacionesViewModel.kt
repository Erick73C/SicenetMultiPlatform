package com.example.sicenetmultiplatform.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sicenetmultiplatform.data.local.entity.CalificacionFinalEntity
import com.example.sicenetmultiplatform.data.local.entity.CalificacionUnidadEntity
import com.example.sicenetmultiplatform.data.repository.LocalRepository
import com.example.sicenetmultiplatform.data.repository.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel de calificaciones finales y por unidad.
 * Reemplaza CalificacionesViewModel.kt del proyecto Android original.
 * Se elimina WorkManager, la sincronización se hace con coroutines.
 *
 * @author Erick Omar Pérez González
 */
class CalificacionesViewModel(
    private val localRepository: LocalRepository,
    private val networkRepository: NetworkRepository,
    private val matricula: String
) : ViewModel() {

    private val cargaAcademica = localRepository.obtenerCargaAcademica(matricula)
    private val finalesRaw     = localRepository.obtenerCalificacionesFinales()
    private val unidadesRaw    = localRepository.obtenerCalificacionesUnidad()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Finales combinadas con carga académica
    // Si no hay calificación guardada para una materia muestra NP
    val calificacionesFinales: StateFlow<List<CalificacionFinalEntity>> =
        combine(cargaAcademica, finalesRaw) { carga, finales ->
            carga.map { materia ->
                finales.find {
                    it.materia.equals(materia.nombreMateria, ignoreCase = true)
                } ?: CalificacionFinalEntity(
                    materia             = materia.nombreMateria,
                    calificacionFinal   = 0,
                    acreditado          = "NP",
                    ultimaActualizacion = 0L
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Unidades combinadas con carga académica
    val calificacionesUnidad: StateFlow<List<CalificacionUnidadEntity>> =
        combine(cargaAcademica, unidadesRaw) { carga, unidades ->
            val lista = mutableListOf<CalificacionUnidadEntity>()
            carga.forEach { materia ->
                val unidadesMateria = unidades.filter {
                    it.materia.equals(materia.nombreMateria, ignoreCase = true)
                }
                if (unidadesMateria.isNotEmpty()) {
                    lista.addAll(unidadesMateria)
                } else {
                    lista.add(
                        CalificacionUnidadEntity(
                            materia     = materia.nombreMateria,
                            unidad      = 1,
                            calificacion = 0,
                            ultimaActualizacion = 0L
                        )
                    )
                }
            }
            lista
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Verifica si es necesario sincronizar y lo hace si no hay datos.
     */
    fun verificarYSincronizar() {
        viewModelScope.launch {
            val finales = finalesRaw.first()
            if (finales.isEmpty()) sincronizar()
        }
    }

    /**
     * Sincroniza calificaciones finales y por unidad desde la red.
     */
    private fun sincronizar() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val resultado = networkRepository.obtenerCalificaciones()

                val entities_finales = resultado.finales.map {
                    CalificacionFinalEntity(
                        materia           = it.materia,
                        calificacionFinal = it.calificacionFinal,
                        acreditado        = it.acreditado,
                        ultimaActualizacion = it.ultimaActualizacion
                    )
                }

                val entities_unidades = resultado.unidades.map {
                    CalificacionUnidadEntity(
                        materia      = it.materia,
                        unidad       = it.unidad,
                        calificacion = it.calificacion,
                        ultimaActualizacion = it.ultimaActualizacion
                    )
                }

                localRepository.guardarCalificacionesFinales(entities_finales)
                localRepository.guardarCalificacionesUnidad(entities_unidades)
                println("[CALIF_VM] Calificaciones sincronizadas")
            } catch (e: Exception) {
                println("[CALIF_VM] Error sincronizando: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}