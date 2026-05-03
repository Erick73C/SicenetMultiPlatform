package com.example.sicenetmultiplatform.data.repository

import com.example.sicenetmultiplatform.data.mapper.CalificacionesXmlParser
import com.example.sicenetmultiplatform.data.mapper.CardexXmlParser
import com.example.sicenetmultiplatform.data.mapper.CargaAcademicaXmlParser
import com.example.sicenetmultiplatform.data.model.AlumnoPerfil
import com.example.sicenetmultiplatform.data.model.LoginResult
import com.example.sicenetmultiplatform.data.network.SicenetService
import com.example.sicenetmultiplatform.utils.extraerContenidoXml
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Repositorio de red que maneja todas las peticiones al WebService de SICENET.
 * Reemplaza NetworSNRepository.kt del proyecto Android original.
 * Usa Ktor en lugar de Retrofit + OkHttp.
 *
 * @author Erick Omar Pérez González
 */
class NetworkRepository(
    private val service: SicenetService = SicenetService
) {

    /**
     * Autentica al alumno en SICENET.
     *
     * @param matricula Matrícula del alumno
     * @param password Contraseña del alumno
     * @return LoginResult con el resultado de la autenticación
     */
    suspend fun login(matricula: String, password: String): LoginResult {
        return try {
            val xml = service.login(matricula, password)
            println("[NETWORK_REPO] Login XML: ${xml.take(200)}")

            val accesoCorrecto = xml.contains("\"acceso\":true")

            if (accesoCorrecto) {
                LoginResult(success = true, message = "Login correcto")
            } else {
                LoginResult(success = false, message = "Credenciales inválidas")
            }
        } catch (e: Exception) {
            println("[NETWORK_REPO] Error en login: ${e.message}")
            LoginResult(success = false, message = e.message ?: "Error de conexión")
        }
    }

    /**
     * Obtiene el perfil académico del alumno autenticado.
     *
     * @return AlumnoPerfil con los datos del alumno
     */
    suspend fun obtenerPerfil(): AlumnoPerfil {
        val xml = service.obtenerPerfil()

        val jsonString = extraerContenidoXml(xml, "getAlumnoAcademicoResult")
            ?: throw Exception("No se encontró el perfil en la respuesta")

        println("[NETWORK_REPO] Perfil JSON: ${jsonString.take(200)}")

        val json = Json.Default.parseToJsonElement(jsonString).jsonObject

        return AlumnoPerfil(
            matricula = json["matricula"]?.jsonPrimitive?.content ?: "",
            nombre = json["nombre"]?.jsonPrimitive?.content ?: "",
            carrera = json["carrera"]?.jsonPrimitive?.content ?: "",
            especialidad = json["especialidad"]?.jsonPrimitive?.content ?: "",
            estatus = json["estatus"]?.jsonPrimitive?.content ?: "",
            semActual = json["semActual"]?.jsonPrimitive?.intOrNull ?: 0,
            cdtosAcumulados = json["cdtosAcumulados"]?.jsonPrimitive?.intOrNull ?: 0,
            cdtosActuales = json["cdtosActuales"]?.jsonPrimitive?.intOrNull ?: 0,
            fechaReins = json["fechaReins"]?.jsonPrimitive?.content ?: "",
            modEducativo = json["modEducativo"]?.jsonPrimitive?.intOrNull ?: 0,
            urlFoto = json["urlFoto"]?.jsonPrimitive?.content ?: "",
            inscrito = json["inscrito"]?.jsonPrimitive?.booleanOrNull ?: false
        )
    }

    /**
     * Obtiene la carga académica del alumno autenticado.
     *
     * @param matricula Matrícula del alumno para asociar los datos
     * @param semestre Semestre actual del alumno
     * @return Lista de CargaAcademica
     */
    suspend fun obtenerCargaAcademica(matricula: String, semestre: Int) =
        CargaAcademicaXmlParser.parse(
            xml       = service.obtenerCargaAcademica(),
            matricula = matricula,
            semestre  = semestre
        )

    /**
     * Obtiene el kardex completo del alumno autenticado.
     *
     * @return Lista de Cardex
     */
    suspend fun obtenerCardex() =
        CardexXmlParser.parse(
            xml = service.obtenerCardex()
        )

    /**
     * Obtiene las calificaciones finales y por unidad del alumno autenticado.
     *
     * @return Resultado con listas de CalificacionFinal y CalificacionUnidad
     */
    suspend fun obtenerCalificaciones() =
        CalificacionesXmlParser.parse(
            unidadesXml = service.obtenerCalificacionesUnidades(),
            finalesXml  = service.obtenerCalificacionesFinales()
        )
}