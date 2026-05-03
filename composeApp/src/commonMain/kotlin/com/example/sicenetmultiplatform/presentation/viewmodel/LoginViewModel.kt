package com.example.sicenetmultiplatform.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sicenetmultiplatform.SessionManager
import com.example.sicenetmultiplatform.data.repository.LocalRepository
import com.example.sicenetmultiplatform.data.repository.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel de autenticación. Maneja el login online y offline.
 * Reemplaza LoginViewModel.kt del proyecto Android original.
 * Se elimina WorkManager, la sincronización del perfil se hace
 * directamente con coroutines.
 *
 * @author Erick Omar Pérez González
 */
class LoginViewModel(
    private val networkRepository: NetworkRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    // Estados de la UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Intenta autenticar al alumno.
     * Si hay conexión: autentica en red y sincroniza perfil.
     * Si no hay conexión: busca datos locales guardados.
     *
     * @param usuario Matrícula del alumno
     * @param password Contraseña del alumno
     * @param onSuccess Callback cuando el login es exitoso
     */
    fun login(
        usuario: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val usuarioNormalizado = usuario.trim().uppercase()

            try {
                // Intento online
                val resultado = networkRepository.login(usuarioNormalizado, password)

                if (resultado.success) {
                    SessionManager.iniciarSesion(usuarioNormalizado)

                    // Sincroniza el perfil inmediatamente después del login
                    sincronizarPerfil(usuarioNormalizado)

                    onSuccess()
                } else {
                    _error.value = resultado.message ?: "Credenciales inválidas"
                }

            } catch (e: Exception) {
                // Fallback offline
                println("[LOGIN_VM] Sin conexión, buscando datos locales: ${e.message}")

                val perfilLocal = localRepository
                    .obtenerPerfil(usuarioNormalizado)
                    .first()

                if (perfilLocal != null) {
                    SessionManager.iniciarSesion(usuarioNormalizado)
                    onSuccess()
                } else {
                    _error.value = "Sin conexión y sin datos guardados"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Sincroniza el perfil del alumno desde la red y lo guarda localmente.
     *
     * @param matricula Matrícula del alumno
     */
    private suspend fun sincronizarPerfil(matricula: String) {
        try {
            val perfil = networkRepository.obtenerPerfil()
            localRepository.guardarPerfil(
                com.example.sicenetmultiplatform.data.local.entity.PerfilEntity(
                    matricula       = perfil.matricula,
                    nombre          = perfil.nombre,
                    carrera         = perfil.carrera,
                    especialidad    = perfil.especialidad,
                    estatus         = perfil.estatus,
                    semActual       = perfil.semActual,
                    cdtosAcumulados = perfil.cdtosAcumulados,
                    cdtosActuales   = perfil.cdtosActuales,
                    fechaReins      = perfil.fechaReins,
                    modEducativo    = perfil.modEducativo,
                    urlFoto         = perfil.urlFoto,
                    inscrito        = perfil.inscrito,
                    ultimaActualizacion = com.example.sicenetmultiplatform.utils.getCurrentTimeMillis()
                )
            )
            println("[LOGIN_VM] Perfil sincronizado correctamente")
        } catch (e: Exception) {
            println("[LOGIN_VM] Error sincronizando perfil: ${e.message}")
        }
    }

    /**
     * Limpia el mensaje de error.
     */
    fun limpiarError() {
        _error.value = null
    }
}