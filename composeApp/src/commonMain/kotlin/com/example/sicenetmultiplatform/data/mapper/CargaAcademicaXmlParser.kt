package com.example.sicenetmultiplatform.data.mapper

import com.example.sicenetmultiplatform.data.model.CargaAcademica
import com.example.sicenetmultiplatform.utils.extraerContenidoXml
import com.example.sicenetmultiplatform.utils.getCurrentTimeMillis
import kotlinx.serialization.json.*

object CargaAcademicaXmlParser {

    fun parse(
        xml: String,
        matricula: String,
        semestre: Int
    ): List<CargaAcademica> {

        val lista = mutableListOf<CargaAcademica>()

        val jsonString = extraerContenidoXml(xml, "getCargaAcademicaByAlumnoResult")

        if (jsonString.isNullOrBlank()) {
            println("[CARGA_PARSE] No se encontró contenido en el XML")
            return emptyList()
        }

        println("[CARGA_PARSE] Contenido extraído: ${jsonString.take(100)}...")

        try {
            val timestamp = getCurrentTimeMillis()
            val elemento = Json.parseToJsonElement(jsonString.trim())

            // El JSON viene como array directo
            // Ejemplo: [ { "Materia": ..., "Grupo": ..., "Lunes": ... }, ... ]
            if (elemento is JsonArray) {
                elemento.forEach { item ->
                    lista.add(mapToCargaAcademica(item.jsonObject, matricula, semestre, timestamp))
                }
            } else {
                println("[CARGA_PARSE] Formato inesperado, se esperaba un array")
            }

        } catch (e: Exception) {
            println("[CARGA_PARSE] Error parseando JSON: ${e.message}")
        }

        return lista
    }

    private fun mapToCargaAcademica(
        item: JsonObject,
        matricula: String,
        semestre: Int,
        timestamp: Long
    ): CargaAcademica {

        // Construye el horario concatenando los días que no estén vacíos
        val horario = listOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado")
            .mapNotNull { dia ->
                item[dia]?.jsonPrimitive?.contentOrNull
                    ?.takeIf { it.isNotBlank() }
                    ?.let { "$dia: $it" }
            }
            .joinToString(" | ")

        return CargaAcademica(
            matricula     = matricula,
            claveMateria  = item["clvOficial"]?.jsonPrimitive?.contentOrNull ?: "",
            nombreMateria = item["Materia"]?.jsonPrimitive?.contentOrNull ?: "Desconocida",
            grupo         = item["Grupo"]?.jsonPrimitive?.contentOrNull ?: "",
            docente       = item["Docente"]?.jsonPrimitive?.contentOrNull ?: "",
            creditos      = item["CreditosMateria"]?.jsonPrimitive?.intOrNull ?: 0,
            horario       = horario,
            semestre      = semestre,
            ultimaActualizacion = timestamp
        )
    }


}