package com.example.sicenetmultiplatform


/**
 * Maneja la sesión del alumno autenticado durante el ciclo de vida
 * de la aplicación. Almacena la matrícula del alumno activo.
 *
 * @author Erick Omar Pérez González
 */
object SessionManager {

    private var _matricula: String? = null

    /**
     * Matrícula del alumno actualmente autenticado.
     * Lanza IllegalStateException si no hay sesión activa.
     */
    val matricula: String
        get() = _matricula
            ?: throw IllegalStateException("No hay sesión activa")

    /**
     * Inicia la sesión guardando la matrícula del alumno.
     *
     * @param matricula Matrícula del alumno autenticado
     */
    fun iniciarSesion(matricula: String) {
        _matricula = matricula.trim().uppercase()
    }

    /**
     * Cierra la sesión limpiando la matrícula guardada.
     */
    fun cerrarSesion() {
        _matricula = null
    }

    /**
     * Indica si hay una sesión activa.
     *
     * @return true si hay un alumno autenticado
     */
    fun estaLogueado(): Boolean = _matricula != null
}