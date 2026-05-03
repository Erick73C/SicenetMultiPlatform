package com.example.sicenetmultiplatform.data.network

import io.ktor.client.request.post
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Define y ejecuta las peticiones SOAP al WebService de SICENET.
 * Reemplaza SICENETWService.kt (Retrofit) del proyecto Android original.
 *
 * @author Erick Omar Pérez González
 */
object SicenetService {

    private const val BASE_URL = "https://sicenet.surguanajuato.tecnm.mx/ws/wsalumnos.asmx"
    private val client = KtorClientProvider.client

    /**
     * Autentica al alumno en SICENET.
     *
     * @param matricula Matrícula del alumno
     * @param password Contraseña del alumno
     * @return String con la respuesta XML del servidor
     */
    suspend fun login(matricula: String, password: String): String {
        return client.post(BASE_URL) {
            header("SOAPAction", "http://tempuri.org/accesoLogin")
            contentType(ContentType.parse("text/xml; charset=utf-8"))
            setBody(SoapRequestBuilder.login(matricula, password))
        }.bodyAsText()
    }

    /**
     * Obtiene el perfil académico del alumno autenticado.
     *
     * @return String con la respuesta XML del servidor
     */
    suspend fun obtenerPerfil(): String {
        return client.post(BASE_URL) {
            header("SOAPAction", "http://tempuri.org/getAlumnoAcademico")
            contentType(ContentType.parse("text/xml; charset=utf-8"))
            setBody(SoapRequestBuilder.perfil())
        }.bodyAsText()
    }

    /**
     * Obtiene la carga académica del alumno autenticado.
     *
     * @return String con la respuesta XML del servidor
     */
    suspend fun obtenerCargaAcademica(): String {
        return client.post(BASE_URL) {
            header("SOAPAction", "http://tempuri.org/getCargaAcademicaByAlumno")
            contentType(ContentType.parse("text/xml; charset=utf-8"))
            setBody(SoapRequestBuilder.cargaAcademica())
        }.bodyAsText()
    }

    /**
     * Obtiene el kardex completo del alumno autenticado.
     *
     * @return String con la respuesta XML del servidor
     */
    suspend fun obtenerCardex(): String {
        return client.post(BASE_URL) {
            header("SOAPAction", "http://tempuri.org/getAllKardexConPromedioByAlumno")
            contentType(ContentType.parse("text/xml; charset=utf-8"))
            setBody(SoapRequestBuilder.cardex())
        }.bodyAsText()
    }

    /**
     * Obtiene las calificaciones por unidad del alumno autenticado.
     *
     * @return String con la respuesta XML del servidor
     */
    suspend fun obtenerCalificacionesUnidades(): String {
        return client.post(BASE_URL) {
            header("SOAPAction", "http://tempuri.org/getCalifUnidadesByAlumno")
            contentType(ContentType.parse("text/xml; charset=utf-8"))
            setBody(SoapRequestBuilder.calificacionesUnidades())
        }.bodyAsText()
    }

    /**
     * Obtiene las calificaciones finales del alumno autenticado.
     * Intenta primero con modEducativo 1, si no tiene datos intenta con 0.
     *
     * @return String con la respuesta XML del servidor
     */
    suspend fun obtenerCalificacionesFinales(): String {

        // Intenta primero con modEducativo 1
        val respuesta = client.post(BASE_URL) {
            header("SOAPAction", "http://tempuri.org/getAllCalifFinalByAlumnos")
            contentType(ContentType.parse("text/xml; charset=utf-8"))
            setBody(SoapRequestBuilder.calificacionesFinales(1))
        }.bodyAsText()

        // Si no contiene datos intenta con modEducativo 0
        // igual que SicenetCalificacionesWorker del proyecto original
        if (!respuesta.contains("lstCalif") && !respuesta.contains("[{")) {
            println("[SICENET_SERVICE] No hay finales con modEducativo 1, intentando con 0")
            return client.post(BASE_URL) {
                header("SOAPAction", "http://tempuri.org/getAllCalifFinalByAlumnos")
                contentType(ContentType.parse("text/xml; charset=utf-8"))
                setBody(SoapRequestBuilder.calificacionesFinales(0))
            }.bodyAsText()
        }

        return respuesta
    }
}